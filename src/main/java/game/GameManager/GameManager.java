package game.GameManager;

import game.MapGenerator.BrickMapManager;
import org.Event.EventActionID;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;
import org.Scene.SceneKey;
import org.Scene.SceneManager;
import utils.Time;

/**
 * Class that manages every aspect of game levels, decides what to do when starting,
 * continuing or losing.
 */
public class GameManager extends MonoBehaviour {

    public static final double LEVEL_INTRODUCTION_TIME = 3.2;
    public static final double LEVEL_CONCLUDING_TIME = 3.2;

    /**
     * <b>Read-only. Write via {@link #setGameState}.</b>
     */
    private GameState _gameState = GameState.IntroducingLevel;

    /**
     * Setter for read-only field {@link #_gameState}
     *
     * @param gameState The value to set.
     */
    private void setGameState(GameState gameState) {
        this._gameState = gameState;
    }

    private int currentLevel = 1;
    private boolean hasSave = false;

    private EventActionID brickMapManager_onMapCleared_ID = null;

    private Time.CoroutineID enterPlaying_coroutineID = null;

    private static GameManager instance = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public GameManager(GameObject owner) {
        super(owner);
        if (instance == null) {
            instance = this;
        }
    }

    public static GameManager getInstance() {
        return instance;
    }

    @Override
    public void start() {
        brickMapManager_onMapCleared_ID = BrickMapManager.getInstance().onMapCleared
                .addListener(this::brickMapManager_onMapCleared);
        startNewGame();
    }

    @Override
    public void onDestroy() {
        if (BrickMapManager.getInstance() != null) {
            BrickMapManager.getInstance().onMapCleared
                    .removeListener(brickMapManager_onMapCleared_ID);
        }
        Time.removeCoroutine(enterPlaying_coroutineID);
    }

    public GameState getGameState() {
        return _gameState;
    }

    /**
     * Called when {@link BrickMapManager#onMapCleared} is invoked.<br><br>
     * This function loads a new map upon the old map is cleared.
     *
     * @param sender Event caller {@link BrickMapManager}.
     * @param e      Empty event argument.
     */
    private void brickMapManager_onMapCleared(Object sender, Void e) {
        concludeLevel();
    }

    public void startNewGame() {
        currentLevel = 0;
        loadNextLevel();
    }

    public void continueGame() {
        if (hasSave) {
            System.out.println("[GameManager] Continuing Game");

            loadNextLevel();
            setGameState(GameState.Playing);
        }

        System.out.println("[GameManager] No progress has been saved!");

    }

    public void restartGame() {
        System.out.println("[GameManager] Restarting Game");

        loadNextLevel();
    }

    public void returnToMainMenu() {
        System.out.println("[GameManager] Returning to Main Menu");

        SceneManager.loadScene(SceneKey.Menu);
    }

    private void spawnBoss() {
        PrefabManager.instantiatePrefab(PrefabIndex.Voltraxis);
    }

    /**
     * Load the next level.
     * <p>
     * This function displays a level notification for the next level, building
     * the level before allowing the player to play.
     * </p>
     */
    public void loadNextLevel() {

        setGameState(GameState.IntroducingLevel);
        currentLevel++;

        // Display level
        var levelNotificationUI = PrefabManager.instantiatePrefab(PrefabIndex.LevelNotification)
                .getComponent(LevelNotificationUI.class);
        levelNotificationUI.setLevel(currentLevel);

        // Build map
        BrickMapManager.getInstance().generateMap();

        // Delay before allowing to play
        enterPlaying_coroutineID
                = Time.addCoroutine(this::enterPlaying, Time.getTime() + LEVEL_INTRODUCTION_TIME);

    }

    /**
     * Handles when the level is completed.
     */
    public void concludeLevel() {

        setGameState(GameState.ConcludingLevel);

        // Display cleared
        var levelNotificationUI = PrefabManager.instantiatePrefab(PrefabIndex.LevelNotification)
                .getComponent(LevelNotificationUI.class);
        levelNotificationUI.setLevelClear(currentLevel);

        // Delay before loading new level
        enterPlaying_coroutineID
                = Time.addCoroutine(this::loadNextLevel, Time.getTime() + LEVEL_INTRODUCTION_TIME);

    }

    private void enterPlaying() {
        setGameState(GameState.Playing);
    }

    public void gameOver() {
        SceneManager.loadScene(SceneKey.Menu);
    }

    public void pauseGame() {
        Time.setTimeScale(0);
        //showPauseUI
    }

    public void resumeGame() {
        Time.setTimeScale(1);
        //hidePauseUI
    }

    public void quitGame() {
    }

    public void giveUp() {
        System.out.println("[GameManager] Giving Up");

        hasSave = false;
        SceneManager.loadScene(SceneKey.Menu);
    }

}