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
 * Manages global game state and persists across scenes.
 *
 * <p>This class is a singleton that handles:</p>
 * <ul>
 *     <li>Loading and saving player data/config.</li>
 *     <li>Switching between scenes.</li>
 *     <li>Game state transitions (start, pause, resume, quit).</li>
 * </ul>
 */
public class GameManager extends MonoBehaviour {

    /** Directory where player data is stored. */
    public static final Path PLAYER_DATA_DIRECTORY = Paths.get(System.getenv("APPDATA"), "Arkanoid");

    /**
     * Represents different stages of loading the game.
     */
    public enum GameLoadingState {
        WaitForSpawn,
        LoadProgress,
        StartGame,
        None,
    }

    private static GameManager instance = null;
    private GameLoadingState gameLoadingState = GameLoadingState.None;

    /**
     * Constructs the GameManager instance.
     * Ensures that only one instance exists (singleton pattern).
     *
     * @param owner The GameObject that owns this component.
     * @throws ReinitializedSingletonException if an instance already exists.
     */
    public GameManager(GameObject owner) {
        super(owner);
        if (instance != null) {
            throw new ReinitializedSingletonException("GameManager is a singleton");
        }
        instance = this;
        verifyDataIntegrity();
    }

    /**
     * Called once per frame. Handles the current loading state of the game.
     */
    @Override
    public void update() {
        switch (gameLoadingState) {
            case WaitForSpawn -> gameLoadingState = GameLoadingState.LoadProgress;
            case LoadProgress -> {
                ScoreManager.getInstance().loadProgress();
                RankManager.getInstance().loadProgress();
                BrickMapManager.getInstance().loadProgress();
                LevelManager.getInstance().loadProgress();
                Player.getInstance().getPlayerPaddle().getPaddleStat().loadProgress();
                Player.getInstance().getPlayerPaddle().getPaddleHealth().loadProgress();
                Player.getInstance().getPlayerLives().loadProgress();
                gameLoadingState = GameLoadingState.StartGame;
            }
            case StartGame -> {
                LevelManager.getInstance().startGame();
                gameLoadingState = GameLoadingState.None;
            }
        }
    }

    /**
     * Called when this object is destroyed. Clears the singleton instance.
     */
    @Override
    public void onDestroy() {
        instance = null;
    }

    /**
     * Gets the singleton instance of GameManager.
     *
     * @return The current GameManager instance.
     */
    public static GameManager getInstance() {
        return instance;
    }

    /**
     * Starts a completely new game, resetting all player data.
     */
    public void startNewGame() {
        startGameScene();
        DataManager.getInstance().resetSave();
    }

    /**
     * Continues the game using the last saved progress.
     */
    public void continueGame() {
        startGameScene();
    }

    /**
     * Loads the in-game scene and prepares game loading.
     */
    private void startGameScene() {
        SceneManager.loadScene(SceneKey.InGame);
        gameLoadingState = GameLoadingState.WaitForSpawn;
    }

    /**
     * Returns to the main menu and resets time scale.
     */
    public void quitToMainMenu() {
        SceneManager.loadScene(SceneKey.Menu);
        Time.setTimeScale(1.0);
    }

    /**
     * Exits the entire game application.
     */
    public void quitGame() {
        Platform.exit();
    }

    /**
     * Pauses the game by setting the time scale to zero.
     */
    public void pauseGame() {
        Time.setTimeScale(0);
    }

    /**
     * Resumes the game by setting the time scale back to normal.
     */
    public void resumeGame() {
        Time.setTimeScale(1);
    }

    /**
     * Verifies that the player data directory exists and creates it if missing.
     */
    private void verifyDataIntegrity() {
        try {
            Files.createDirectories(PLAYER_DATA_DIRECTORY);
        } catch (IOException e) {
            System.err.println("[GameManager] Error while verifying integrity: " + e.getMessage());
        }
    }
}
