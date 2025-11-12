package game.GameManager;

import game.Level.LevelManager;
import game.MapGenerator.BrickMapManager;
import game.Player.Player;
import game.Player.PlayerData.DataManager;
import game.Rank.RankManager;
import game.Score.ScoreManager;
import javafx.application.Platform;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Scene.SceneKey;
import org.Scene.SceneManager;
import utils.Time;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Class that manages every aspect of the game and exists across all scenes.
 * <p>
 * Below is all of its function:
 * <ul>
 *     <li>Save, load player data.</li>
 *     <li>Save, load player config.</li>
 *     <li>Jump between scenes.</li>
 *     <li>Quit game</li>
 * </ul>
 * </p>
 */
public class GameManager extends MonoBehaviour {

    public static final Path PLAYER_DATA_DIRECTORY = Paths.get(System.getenv("APPDATA"), "Arkanoid");

    public enum GameLoadingState {
        WaitForSpawn,
        LoadProgress,
        StartGame,
        None,
    }

    private static GameManager instance = null;

    private GameLoadingState gameLoadingState = GameLoadingState.None;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public GameManager(GameObject owner) {
        super(owner);
        if (instance != null) {
            throw new ReinitializedSingletonException("GameManager is a singleton");
        }
        instance = this;
        verifyDataIntegrity();
    }

    @Override
    public void update() {
        switch (gameLoadingState) {

            case WaitForSpawn:
                gameLoadingState = GameLoadingState.LoadProgress;
                break;

            case LoadProgress:
                ScoreManager.getInstance().loadProgress();
                RankManager.getInstance().loadProgress();
                BrickMapManager.getInstance().loadProgress();
                LevelManager.getInstance().loadProgress();
                Player.getInstance().getPlayerPaddle().getPaddleStat().loadProgress();
                Player.getInstance().getPlayerPaddle().getPaddleHealth().loadProgress();
                Player.getInstance().getPlayerLives().loadProgress();
                gameLoadingState = GameLoadingState.StartGame;
                break;

            case StartGame:
                LevelManager.getInstance().startGame();
                gameLoadingState = GameLoadingState.None;
                break;

        }
    }

    @Override
    public void onDestroy() {
        instance = null;
    }

    public static GameManager getInstance() {
        return instance;
    }

    /**
     * @aaa
     */
    public void startNewGame() {
        startGameScene();
        DataManager.getInstance().resetSave();
    }

    /**
     * @aaaa
     */
    public void continueGame() {
        startGameScene();
    }

    private void startGameScene() {
        SceneManager.loadScene(SceneKey.InGame);
        gameLoadingState = GameLoadingState.WaitForSpawn;
    }

    public void quitToMainMenu() {
        SceneManager.loadScene(SceneKey.Menu);
        Time.setTimeScale(1.0);
    }

    public void quitGame() {
        Platform.exit();
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

    private void verifyDataIntegrity() {
        try {
            Files.createDirectories(PLAYER_DATA_DIRECTORY);
        } catch (IOException e) {
            System.err.println("[GameManager] Error while verifying integrity: " + e.getMessage());
        }
    }

}