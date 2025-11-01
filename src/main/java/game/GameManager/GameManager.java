package game.GameManager;

import game.MapGenerator.BrickMapManager;
import javafx.application.Platform;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Scene.SceneKey;
import org.Scene.SceneManager;
import utils.Time;

public class GameManager extends MonoBehaviour {

    private static GameState gameState = GameState.MainMenu;
    private int currentLevel = 1;
    private boolean hasSave = false;

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

    @Override
    public void start() {
        System.out.println("Starting GameManager");
        BrickMapManager.getInstance().onMapCleared
                .addListener(this::brickMapManager_onMapCleared);

        startNewGame();
    }

    /**
     * Called when {@link BrickMapManager#onMapCleared} is invoked.<br><br>
     * This function loads a new map upon the old map is cleared.
     *
     * @param sender Event caller {@link BrickMapManager}.
     * @param e      Empty event argument.
     */
    private void brickMapManager_onMapCleared(Object sender, Void e) {
        System.out.println("brickMapManager_onMapCleared");
        loadNextLevel();
    }

    public static GameManager getInstance() {
        return instance;
    }

    public void startNewGame() {
        currentLevel = 0;
        loadNextLevel();
    }

    public void continueGame() {
        if (hasSave) {
            System.out.println("[GameManager] Continuing Game");

            loadNextLevel();
            gameState = GameState.Playing;
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

    public void loadNextLevel() {
        currentLevel++;

        var levelNotificationUI = PrefabManager.instantiatePrefab(PrefabIndex.LevelNotification)
                .getComponent(LevelNotificationUI.class);
        levelNotificationUI.setLevel(currentLevel);
        BrickMapManager.getInstance().generateMap();
    }

    public void onLevelCompleted() {
        System.out.println("[GameManager] Level Completed");
    }

    public void gameOver() {
        gameState = GameState.GameOver;
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

    public void toNextLevel() {
        System.out.println("[GameManager] To Next Level");

        currentLevel++;
        loadNextLevel();
    }

    private void brickManager_onLevelComplete() {
        System.out.println("[GameManager] Brick Manager onLevelComplete");

    }
}
