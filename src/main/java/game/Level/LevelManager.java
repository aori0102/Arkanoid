package game.Level;

import game.Ball.BallsManager;
import game.GameManager.GameManager;
import game.GameManager.LevelState;
import game.MapGenerator.BrickMapManager;
import game.Player.Player;
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

public final class LevelManager extends MonoBehaviour {

    public static final double LEVEL_INTRODUCING_TIME = 3.2;
    public static final double LEVEL_CONCLUDING_TIME = 3.0;

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

    private EventActionID brickMapManager_onMapCleared_ID = null;
    private EventActionID voltraxis_onBossDestroyed_ID = null;
    private EventActionID playerLives_onLivesReachZero_ID = null;
    private EventActionID playerLives_onLivesDecreased_ID = null;

    private Time.CoroutineID enablePlaying_coroutineID = null;
    private Time.CoroutineID progressToNextLevel_coroutineID = null;

    public EventHandler<Void> onLevelCleared = new EventHandler<>(LevelManager.class);

    public EventHandler<OnLevelLoadedEventArgs> onLevelLoaded = new EventHandler<>(LevelManager.class);

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
        Time.removeCoroutine(enablePlaying_coroutineID);
        Time.removeCoroutine(progressToNextLevel_coroutineID);
    }

    @Override
    public void start() {
        brickMapManager_onMapCleared_ID = BrickMapManager.getInstance().onMapCleared
                .addListener(this::brickMapManager_onMapCleared);
        playerLives_onLivesReachZero_ID = Player.getInstance().getPlayerLives().onLivesReachZero
                .addListener(this::playerLives_onLivesReachZero);
        playerLives_onLivesDecreased_ID = Player.getInstance().getPlayerLives().onLivesDecreased
                .addListener(this::playerLives_onLivesDecreased);
    }

    public static LevelManager getInstance() {
        return instance;
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
     * Called when {@link PlayerLives#onLivesDecreased} is invoked.<br><br>
     * This function creates a ball as player loses a live.
     *
     * @param sender Event caller {@link PlayerLives}.
     * @param e      Empty event argument.
     */
    private void playerLives_onLivesDecreased(Object sender, Void e) {
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

    public void startGame(int levelIndex) {
        this.levelIndex = levelIndex;
        progressToNextLevel();
    }

    private void endLevel() {
        onLevelCleared.invoke(this, null);
        setLevelState(LevelState.ConcludingLevel);
        cleanUpMap();
        displayLevelCleared();

        // Delay for conclusion
        progressToNextLevel_coroutineID
                = Time.addCoroutine(this::progressToNextLevel, Time.getTime() + LEVEL_CONCLUDING_TIME);

    }

    private void progressToNextLevel() {

        levelIndex++;
        loadCurrentLevel();

        // Display notification
        displayLevelEnter();

    }

    private void displayLevelEnter() {
        var notificationUI = PrefabManager.instantiatePrefab(PrefabIndex.LevelNotificationUI)
                .getComponent(LevelNotificationUI.class);
        if (LEVEL_DATA[levelIndex] == LevelType.Frenzy) {
            notificationUI.setNotification(FRENZY_LABEL);
        } else {
            notificationUI.setNotification(LEVEL_PREFIX + levelIndex);
        }
    }

    private void displayLevelCleared() {
        var notificationUI = PrefabManager.instantiatePrefab(PrefabIndex.LevelNotificationUI)
                .getComponent(LevelNotificationUI.class);
        notificationUI.setNotification(LEVEL_PREFIX + levelIndex + CLEARED_SUFFIX);
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

        setLevelState(LevelState.IntroducingLevel);

        var onLevelLoadedEventArgs = new OnLevelLoadedEventArgs();
        onLevelLoadedEventArgs.index = levelIndex;
        onLevelLoadedEventArgs.type = LEVEL_DATA[levelIndex];
        onLevelLoaded.invoke(this, onLevelLoadedEventArgs);

        BallsManager.getInstance().spawnInitialBall();

        // Delay before enable playing
        enablePlaying_coroutineID
                = Time.addCoroutine(this::enablePlaying, Time.getTime() + LEVEL_INTRODUCING_TIME);

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
        BallsManager.getInstance().removeAllBall();
    }

    private void onGameOver() {
        System.out.println("GAME OVER");
        GameManager.getInstance().quitToMainMenu();
    }

    public LevelState getLevelState() {
        return _levelState;
    }
}