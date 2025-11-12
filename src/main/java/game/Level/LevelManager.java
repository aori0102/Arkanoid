package game.Level;

import game.Ball.BallsManager;
import game.GameManager.GameManager;
import game.GameManager.LevelState;
import game.GameOver.GameOverManager;
import game.MapGenerator.BrickMapManager;
import game.Perks.Index.PerkManager;
import game.Player.Player;
import game.Player.PlayerData.DataManager;
import game.Player.PlayerData.IPlayerProgressHolder;
import game.Player.PlayerLives;
import game.Voltraxis.Voltraxis;
import org.Event.EventActionID;
import org.Event.EventHandler;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;
import utils.Time;

public final class LevelManager extends MonoBehaviour implements
        IPlayerProgressHolder {

    public static final double LEVEL_INTRODUCING_TIME = 3.2;
    public static final double LEVEL_CONCLUDING_TIME = 3.0;
    public static final double BALL_REMOVAL_DELAY = 0.01;

    private static final String LEVEL_PREFIX = "Level ";
    private static final String FRENZY_LABEL = "F R E N Z Y !";
    private static final String CLEARED_SUFFIX = " cleared!";
    private static final LevelType[] LEVEL_DATA = {
            LevelType.Normal,
            LevelType.Normal,
            LevelType.Frenzy,
            LevelType.Normal,
            LevelType.Showdown,
            LevelType.Frenzy,
            LevelType.Normal,
            LevelType.Frenzy,
            LevelType.Normal,
            LevelType.Showdown
    };

    private static LevelManager instance = null;

    /**
     * <b>Read-only. Write via {@link #setLevelState}.</b>
     */
    private LevelState _levelState = LevelState.IntroducingLevel;

    private int levelIndex = 0;
    private int clearedLevel = 0;
    private double mapClearingStartTick = 0.0;

    private EventActionID brickMapManager_onMapCleared_ID = null;
    private EventActionID voltraxis_onBossDestroyed_ID = null;
    private EventActionID playerLives_onLivesReachZero_ID = null;
    private EventActionID playerLives_onLivesDecreased_ID = null;
    private EventActionID perkManager_onPerkSelectionCompleted_ID = null;
    private EventActionID gameOverManager_onRetryRequested_ID = null;
    private EventActionID gameOverManager_onMainMenuRequested_ID = null;

    private Time.CoroutineID enablePlaying_coroutineID = null;
    private Time.CoroutineID progressToNextLevel_coroutineID = null;
    private Time.CoroutineID destroyRandomBall_coroutineID = null;

    public EventHandler<Void> onLevelCleared = new EventHandler<>(LevelManager.class);
    public EventHandler<Void> onPerkSelectionRequested = new EventHandler<>(LevelManager.class);
    public EventHandler<Void> onLevelConcluded = new EventHandler<>(LevelManager.class);
    public EventHandler<Void> onGameOver = new EventHandler<>(LevelManager.class);

    public static EventHandler<OnLevelLoadedEventArgs> onAnyLevelLoaded = new EventHandler<>(LevelManager.class);

    public static class OnLevelLoadedEventArgs {
        public int index;
        public LevelType type;
    }

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
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
        if (GameOverManager.getInstance() != null) {
            GameOverManager.getInstance().onRetryRequested
                    .removeListener(gameOverManager_onRetryRequested_ID);
            GameOverManager.getInstance().onMainMenuRequested
                    .removeListener(gameOverManager_onMainMenuRequested_ID);
        }
        Time.removeCoroutine(enablePlaying_coroutineID);
        Time.removeCoroutine(progressToNextLevel_coroutineID);
        Time.removeCoroutine(destroyRandomBall_coroutineID);
    }

    @Override
    public void start() {
        brickMapManager_onMapCleared_ID = BrickMapManager.getInstance().onMapCleared
                .addListener(this::brickMapManager_onMapCleared);
        playerLives_onLivesReachZero_ID = Player.getInstance().getPlayerLives().onLivesReachZero
                .addListener(this::playerLives_onLivesReachZero);
        playerLives_onLivesDecreased_ID = Player.getInstance().getPlayerLives().onLivesChanged
                .addListener(this::playerLives_onLivesDecreased);
        perkManager_onPerkSelectionCompleted_ID = PerkManager.getInstance().onPerkSelectionCompleted
                .addListener(this::perkManager_onPerkSelectionCompleted);
        gameOverManager_onRetryRequested_ID = GameOverManager.getInstance().onRetryRequested
                .addListener(this::gameOverManager_onRetryRequested);
        gameOverManager_onMainMenuRequested_ID = GameOverManager.getInstance().onMainMenuRequested
                .addListener(this::gameOverManager_onMainMenuRequested);
    }

    public static LevelManager getInstance() {
        return instance;
    }

    @Override
    public void loadProgress() {
        levelIndex = DataManager.getInstance().getProgress().getLevel();
    }

    /**
     * Setter for read-only field {@link #_levelState}
     *
     * @param levelState The value to set.
     */
    private void setLevelState(LevelState levelState) {
        this._levelState = levelState;
    }

    /**
     * Called when {@link BrickMapManager#onMapCleared} is invoked.<br><br>
     * This function ends a level as a map is cleared.
     *
     * @param sender Event caller {@link BrickMapManager}.
     * @param e      Empty event argument.
     */
    private void brickMapManager_onMapCleared(Object sender, Void e) {
        endLevel();
    }

    /**
     * Called when {@link PlayerLives#onLivesReachZero} is invoked.<br><br>
     * Ends the game as player's live reaches zero.
     *
     * @param sender Event caller {@link PlayerLives}.
     * @param e      Empty event argument.
     */
    private void playerLives_onLivesReachZero(Object sender, Void e) {
        onGameOver();
    }

    /**
     * Called when {@link PlayerLives#onLivesChanged} is invoked.<br><br>
     * This function creates a ball as player loses a live.
     *
     * @param sender Event caller {@link PlayerLives}.
     * @param e      Empty event argument.
     */
    private void playerLives_onLivesDecreased(Object sender, Void e) {
        BallsManager.getInstance().destroyAllBalls();
        BallsManager.getInstance().spawnInitialBall();
    }

    /**
     * Called when {@link Voltraxis#onBossDestroyed} is invoked.<br><br>
     * This function ends a boss level when {@link Voltraxis} is defeated.
     *
     * @param sender Event caller {@link Voltraxis}.
     * @param e      Empty event argument.
     */
    private void voltraxis_onBossDestroyed(Object sender, Void e) {
        Voltraxis.getInstance().onBossDestroyed.removeListener(voltraxis_onBossDestroyed_ID);
        endLevel();
    }

    /**
     * Called when {@link PerkManager#onPerkSelectionCompleted} is invoked.<br><br>
     * This function ends perk selection to load new round.
     *
     * @param sender Event caller {@link PerkManager}.
     * @param e      Empty event argument.
     */
    private void perkManager_onPerkSelectionCompleted(Object sender, Void e) {
        if (_levelState != LevelState.PerkSelection) {
            throw new RuntimeException("Perk selection done not within " + LevelState.PerkSelection);
        }
        progressToNextLevel();
    }

    /**
     * Called when {@link GameOverManager#onRetryRequested} is invoked.<br><br>
     * This function starts the game again after game over
     *
     * @param sender Event caller {@link GameOverManager}.
     * @param e      Empty event argument.
     */
    private void gameOverManager_onRetryRequested(Object sender, Void e) {
        startGame();
    }

    /**
     * Called when {@link GameOverManager#onMainMenuRequested} is invoked.<br><br>
     * This function returns to the main menu after game over
     *
     * @param sender Event caller {@link GameOverManager}.
     * @param e      Empty event argument.
     */
    private void gameOverManager_onMainMenuRequested(Object sender, Void e) {
        GameManager.getInstance().quitToMainMenu();
    }

    public void startGame() {
        loadCurrentLevel();
    }

    private void endLevel() {
        onLevelCleared.invoke(this, null);
        clearedLevel = levelIndex + 1;
        setLevelState(LevelState.ConcludingLevel);
        cleanUpMap();
        displayLevelCleared();
    }

    private void progressToNextLevel() {
        DataManager.getInstance().updateSave();
        levelIndex++;
        loadCurrentLevel();
    }

    private void displayLevelEnter() {
        var notificationUI = PrefabManager.instantiatePrefab(PrefabIndex.LevelNotificationUI)
                .getComponent(LevelNotificationUI.class);
        if (LEVEL_DATA[levelIndex] == LevelType.Frenzy) {
            notificationUI.setNotification(FRENZY_LABEL);
        } else {
            notificationUI.setNotification(LEVEL_PREFIX + (levelIndex + 1));
        }
    }

    private void displayLevelCleared() {
        var notificationUI = PrefabManager.instantiatePrefab(PrefabIndex.LevelNotificationUI)
                .getComponent(LevelNotificationUI.class);
        notificationUI.setNotification(LEVEL_PREFIX + (levelIndex + 1) + CLEARED_SUFFIX);
    }

    private void loadCurrentLevel() {

        if (levelIndex >= LEVEL_DATA.length) {
            throw new RuntimeException("All level cleared!");
        }
        if (LEVEL_DATA[levelIndex] == LevelType.Showdown) {
            loadBoss();
        } else {
            BrickMapManager.getInstance().generateMap();
        }

        // Display notification
        displayLevelEnter();

        setLevelState(LevelState.IntroducingLevel);

        var onLevelLoadedEventArgs = new OnLevelLoadedEventArgs();
        onLevelLoadedEventArgs.index = levelIndex;
        onLevelLoadedEventArgs.type = LEVEL_DATA[levelIndex];
        onAnyLevelLoaded.invoke(this, onLevelLoadedEventArgs);

        BallsManager.getInstance().spawnInitialBall();

        // Delay before enable playing
        enablePlaying_coroutineID
                = Time.addCoroutine(this::enablePlaying, LEVEL_INTRODUCING_TIME);

    }

    private void enablePlaying() {
        setLevelState(LevelState.Playing);
    }

    private void loadBoss() {
        PrefabManager.instantiatePrefab(PrefabIndex.Voltraxis);
        voltraxis_onBossDestroyed_ID = Voltraxis.getInstance().onBossDestroyed
                .addListener(this::voltraxis_onBossDestroyed);
    }

    private void cleanUpMap() {
        destroyRandomBall_coroutineID
                = Time.addCoroutine(this::destroyRandomBall, BALL_REMOVAL_DELAY);
        mapClearingStartTick = Time.getTime();
    }

    private void destroyRandomBall() {
        if (BallsManager.getInstance().removeRandomBall()) {
            destroyRandomBall_coroutineID
                    = Time.addCoroutine(this::destroyRandomBall, BALL_REMOVAL_DELAY);
        } else {
            onLevelConcluded.invoke(this, null);
            var clearTime = Time.getTime() - mapClearingStartTick;
            progressToNextLevel_coroutineID
                    = Time.addCoroutine(
                    this::selectPerk,
                    Math.max(0.0, LEVEL_CONCLUDING_TIME - clearTime)
            );
        }
    }

    private void selectPerk() {
        setLevelState(LevelState.PerkSelection);
        onPerkSelectionRequested.invoke(this, null);
    }

    private void onGameOver() {
        DataManager.getInstance().updateSave();
        DataManager.getInstance().generateNewRecord();
        DataManager.getInstance().resetSave();
        onGameOver.invoke(this, null);
    }

    public LevelState getLevelState() {
        return _levelState;
    }

    public int getClearedLevel() {
        return clearedLevel;
    }

}