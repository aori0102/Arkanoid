package game.GameManager;

import game.Level.LevelManager;
import javafx.application.Platform;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Scene.SceneKey;
import org.Scene.SceneManager;
import utils.Time;

/**
 * Class that manages every aspect of game levels, decides what to do when starting,
 * continuing or losing.
 */
public class GameManager extends MonoBehaviour {

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

    public static GameManager getInstance() {
        return instance;
    }

    /**
     * @aaa
     */
    public void startNewGame() {
        startGameScene(0);
    }

    /**
     * @aaaa
     */
    public void continueGame() {
        if (hasSave) {
            System.out.println("[GameManager] Continuing Game");

            startGameScene(1); // TODO: Change 1 to loaded level index
        }

        System.out.println("[GameManager] No progress has been saved!");
    }

    private void startGameScene(int startingLevel) {
        SceneManager.loadScene(SceneKey.InGame);
        LevelManager.getInstance().startGame(startingLevel);
    }

    public void quitToMainMenu() {
        SceneManager.loadScene(SceneKey.Menu);
        Time.setTimeScale(1.0);
    }

    public void quitGame() {
        Platform.exit();
    }

    public void showRecord() {
        SceneManager.loadScene(SceneKey.Record);
    }

    public void showOptions() {
        SceneManager.loadScene(SceneKey.Options);
    }

    /**
     * @aaa
     */
    public void pauseGame() {
        Time.setTimeScale(0);
    }

    /**
     * @aaa
     */
    public void resumeGame() {
        Time.setTimeScale(1);
    }

}