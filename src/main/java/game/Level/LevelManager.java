package game.Level;

import game.Ball.BallsManager;
import game.GameManager.GameManager;
import game.GameManager.LevelState;
import game.MapGenerator.BrickMapManager;
import game.Perks.Index.PerkManager;
import game.Player.Player;
import game.Player.PlayerLives;
import game.PlayerData.DataManager;
import game.Voltraxis.Voltraxis;
import org.Event.EventActionID;
import org.Event.EventHandler;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;
import utils.Time;

/**
 * LevelManager is responsible for managing all aspects of the game levels.
 * <p>
 * Responsibilities:
 * <ul>
 *     <li>Load and progress levels based on predefined LEVEL_DATA.</li>
 *     <li>Handle level start, end, and transitions.</li>
 *     <li>Manage events like player losing a life, boss defeated, map cleared, or perk selection.</li>
 *     <li>Spawn balls, handle level notifications, and trigger game over.</li>
 * </ul>
 * </p>
 */
public final class LevelManager extends MonoBehaviour {

    public static final double LEVEL_INTRODUCING_TIME = 3.2; // Delay before gameplay starts
    public static final double LEVEL_CONCLUDING_TIME = 3.0;  // Delay before next level
    public static final double BALL_REMOVAL_DELAY = 0.01;    // Delay between removing balls at end of level

    private static final String LEVEL_PREFIX = "Level ";
    private static final String FRENZY_LABEL = "F R E N Z Y !";
    private static final String CLEARED_SUFFIX = " cleared!";
    private static final LevelType[] LEVEL_DATA = {
            LevelType.Normal, LevelType.Normal, LevelType.Frenzy,
            LevelType.Normal, LevelType.Showdown, LevelType.Frenzy,
            LevelType.Normal, LevelType.Frenzy, LevelType.Normal,
            LevelType.Showdown
    };

    private static LevelManager instance = null;

    /**
     * <b>Read-only. Write via {@link #setLevelState}.</b>
     */
    private LevelState _levelState = LevelState.IntroducingLevel;

    private int levelIndex = 0;           // Current level index
    private int clearedLevel = 0;         // Number of levels cleared
    private double mapClearingStartTick = 0.0; // Timestamp when map cleaning started

    private EventActionID brickMapManager_onMapCleared_ID = null;
    private EventActionID voltraxis_onBossDestroyed_ID = null;
    private EventActionID playerLives_onLivesReachZero_ID = null;
    private EventActionID playerLives_onLivesDecreased_ID = null;
    private EventActionID perkManager_onPerkSelectionCompleted_ID = null;

    private Time.CoroutineID enablePlaying_coroutineID = null;
    private Time.CoroutineID progressToNextLevel_coroutineID = null;
    private Time.CoroutineID destroyRandomBall_coroutineID = null;

    public EventHandler<Void> onLevelCleared = new EventHandler<>(LevelManager.class);
    public EventHandler<Void> onPerkSelectionRequested = new EventHandler<>(LevelManager.class);
    public EventHandler<Void> onLevelConcluded = new EventHandler<>(LevelManager.class);
    public EventHandler<Void> onGameOver = new EventHandler<>(LevelManager.class);

    public EventHandler<OnLevelLoadedEventArgs> onLevelLoaded = new EventHandler<>(LevelManager.class);

    /**
     * Event argument for level loaded event.
     */
    public static class OnLevelLoadedEventArgs {
        public int index;         // Level index
        public LevelType type;    // Type of level
    }

    /**
     * Constructor.
     *
     * @param owner The owner GameObject of this MonoBehaviour.
     */
    public LevelManager(GameObject owner) {
        super(owner);
        if (instance != null) {
            throw new ReinitializedSingletonException("LevelManager is a singleton!");
        }
        instance = this;
    }

    @Override
    public void onDestroy() {
        // Cleanup listeners and coroutines
        instance = null;
        if (Voltraxis.getInstance() != null) {
            Voltraxis.getInstance().onBossDestroyed.removeListener(voltraxis_onBossDestroyed_ID);
        }
        if (BrickMapManager.getInstance() != null) {
            BrickMapManager.getInstance().onMapCleared.removeListener(brickMapManager_onMapCleared_ID);
        }
        if (Player.getInstance() != null && Player.getInstance().getPlayerLives() != null) {
            Player.getInstance().getPlayerLives().onLivesReachZero.removeListener(playerLives_onLivesReachZero_ID);
            Player.getInstance().getPlayerLives().onLivesReachZero.removeListener(playerLives_onLivesDecreased_ID);
        }
        if (PerkManager.getInstance() != null) {
            PerkManager.getInstance().onPerkSelectionCompleted
                    .removeListener(perkManager_onPerkSelectionCompleted_ID);
        }
        Time.removeCoroutine(enablePlaying_coroutineID);
        Time.removeCoroutine(progressToNextLevel_coroutineID);
        Time.removeCoroutine(destroyRandomBall_coroutineID);
    }

    @Override
    public void start() {
        // Register listeners for game events
        brickMapManager_onMapCleared_ID = BrickMapManager.getInstance().onMapCleared
                .addListener(this::brickMapManager_onMapCleared);
        playerLives_onLivesReachZero_ID = Player.getInstance().getPlayerLives().onLivesReachZero
                .addListener(this::playerLives_onLivesReachZero);
        playerLives_onLivesDecreased_ID = Player.getInstance().getPlayerLives().onLivesDecreased
                .addListener(this::playerLives_onLivesDecreased);
        perkManager_onPerkSelectionCompleted_ID = PerkManager.getInstance().onPerkSelectionCompleted
                .addListener(this::perkManager_onPerkSelectionCompleted);
    }

    public static LevelManager getInstance() {
        return instance;
    }

    /** Load progress from saved data. */
    public void loadProgress() {
        levelIndex = DataManager.getInstance().getProgress().getLevel();
    }

    /** Setter for read-only level state. */
    private void setLevelState(LevelState levelState) {
        this._levelState = levelState;
    }

    /** Ends the level when map is cleared. */
    private void brickMapManager_onMapCleared(Object sender, Void e) {
        endLevel();
    }

    /** Ends the game when player lives reach zero. */
    private void playerLives_onLivesReachZero(Object sender, Void e) {
        onGameOver();
    }

    /** Spawns a ball when player loses a life. */
    private void playerLives_onLivesDecreased(Object sender, Void e) {
        BallsManager.getInstance().spawnInitialBall();
    }

    /** Ends boss level when boss is destroyed. */
    private void voltraxis_onBossDestroyed(Object sender, Void e) {
        Voltraxis.getInstance().onBossDestroyed.removeListener(voltraxis_onBossDestroyed_ID);
        endLevel();
    }

    /** Ends perk selection to load next level. */
    private void perkManager_onPerkSelectionCompleted(Object sender, Void e) {
        if (_levelState != LevelState.PerkSelection) {
            throw new RuntimeException("Perk selection done not within " + LevelState.PerkSelection);
        }
        progressToNextLevel();
    }

    /** Start the current level. */
    public void startGame() {
        loadCurrentLevel();
    }

    /** Ends the level, cleans map, and shows cleared notification. */
    private void endLevel() {
        onLevelCleared.invoke(this, null);
        clearedLevel = levelIndex + 1;
        setLevelState(LevelState.ConcludingLevel);
        cleanUpMap();
        displayLevelCleared();
    }

    /** Progress to the next level and save data. */
    private void progressToNextLevel() {
        DataManager.getInstance().updateSave();
        levelIndex++;
        loadCurrentLevel();
    }

    /** Display notification when entering level. */
    private void displayLevelEnter() {
        var notificationUI = PrefabManager.instantiatePrefab(PrefabIndex.LevelNotificationUI)
                .getComponent(LevelNotificationUI.class);
        if (LEVEL_DATA[levelIndex] == LevelType.Frenzy) {
            notificationUI.setNotification(FRENZY_LABEL);
        } else {
            notificationUI.setNotification(LEVEL_PREFIX + (levelIndex + 1));
        }
    }

    /** Display level cleared notification. */
    private void displayLevelCleared() {
        var notificationUI = PrefabManager.instantiatePrefab(PrefabIndex.LevelNotificationUI)
                .getComponent(LevelNotificationUI.class);
        notificationUI.setNotification(LEVEL_PREFIX + (levelIndex + 1) + CLEARED_SUFFIX);
    }

    /** Load the current level, generate map or boss, spawn ball, and start delay coroutine. */
    private void loadCurrentLevel() {
        if (levelIndex >= LEVEL_DATA.length) {
            throw new RuntimeException("All level cleared!");
        }
        if (LEVEL_DATA[levelIndex] == LevelType.Showdown) {
            loadBoss();
        } else {
            BrickMapManager.getInstance().generateMap();
        }

        displayLevelEnter();
        setLevelState(LevelState.IntroducingLevel);

        var onLevelLoadedEventArgs = new OnLevelLoadedEventArgs();
        onLevelLoadedEventArgs.index = levelIndex;
        onLevelLoadedEventArgs.type = LEVEL_DATA[levelIndex];
        onLevelLoaded.invoke(this, onLevelLoadedEventArgs);

        BallsManager.getInstance().spawnInitialBall();

        enablePlaying_coroutineID
                = Time.addCoroutine(this::enablePlaying, Time.getTime() + LEVEL_INTRODUCING_TIME);
    }

    /** Enable playing state after delay. */
    private void enablePlaying() {
        setLevelState(LevelState.Playing);
    }

    /** Load boss prefab and register listener. */
    private void loadBoss() {
        PrefabManager.instantiatePrefab(PrefabIndex.Voltraxis);
        voltraxis_onBossDestroyed_ID = Voltraxis.getInstance().onBossDestroyed
                .addListener(this::voltraxis_onBossDestroyed);
    }

    /** Clean up map at end of level by destroying balls gradually. */
    private void cleanUpMap() {
        destroyRandomBall_coroutineID
                = Time.addCoroutine(this::destroyRandomBall, Time.getTime() + BALL_REMOVAL_DELAY);
        mapClearingStartTick = Time.getTime();
    }

    /** Remove balls one by one at level end, then proceed to perk selection. */
    private void destroyRandomBall() {
        if (BallsManager.getInstance().removeRandomBall()) {
            destroyRandomBall_coroutineID
                    = Time.addCoroutine(this::destroyRandomBall, Time.getTime() + BALL_REMOVAL_DELAY);
        } else {
            onLevelConcluded.invoke(this, null);
            var clearTime = Time.getTime() - mapClearingStartTick;
            progressToNextLevel_coroutineID
                    = Time.addCoroutine(
                    this::selectPerk,
                    Time.getTime() + Math.max(0.0, LEVEL_CONCLUDING_TIME - clearTime)
            );
        }
    }

    /** Select perk after level concludes. */
    private void selectPerk() {
        setLevelState(LevelState.PerkSelection);
        onPerkSelectionRequested.invoke(this, null);
    }

    /** Handle game over: reset data and quit to main menu. */
    private void onGameOver() {
        DataManager.getInstance().resetSave();
        DataManager.getInstance().generateNewRecord();
        onGameOver.invoke(this, null);
        GameManager.getInstance().quitToMainMenu();
    }

    /** Get current level state. */
    public LevelState getLevelState() {
        return _levelState;
    }

    /** Get number of cleared levels. */
    public int getClearedLevel() {
        return clearedLevel;
    }

}
