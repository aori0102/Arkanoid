package game.GameManager;

import game.Player.Player;
import javafx.application.Platform;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Scene.SceneKey;
import org.Scene.SceneManager;

public class GameManager extends MonoBehaviour {

    private static GameState gameState = GameState.MainMenu;
    private int currentLevel = 1;
    private boolean hasSave =  false;

    private static GameManager instance=null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public GameManager(GameObject owner) {
        super(owner);
        if(instance == null) {
            instance = this;
        }
    }

    public static GameManager getInstance() {
        return instance;
    }

    @Override
    public void awake() {
        Player.getInstance().onLivesReachZero.addListener(this::player_onNodeHealthReachZero);
    }
    private void player_onNodeHealthReachZero(Object sender, Void e) {
        gameOver();
    }

    public void startNewGame(){
        System.out.println("[GameManager] Starting New Game");

        currentLevel = 1;
        loadLevel(currentLevel);
        gameState = GameState.Playing;

        hasSave = true;
    }

    public void continueGame(){
        if(hasSave) {
            System.out.println("[GameManager] Continuing Game");

            loadLevel(currentLevel);
            gameState = GameState.Playing;
        }

        System.out.println("[GameManager] No progress has been saved!");

    }

    public void restartGame(){
        System.out.println("[GameManager] Restarting Game");

        loadLevel(currentLevel);
    }

    public void returnToMainMenu(){
        System.out.println("[GameManager] Returning to Main Menu");

        SceneManager.loadScene(SceneKey.Menu);
    }

    public void loadLevel(int level){
        System.out.println("[GameManager] Loading Level " + level);

        currentLevel = level;
        SceneManager.loadScene(SceneKey.values()[currentLevel]);
    }

    public void onLevelCompleted(){
        System.out.println("[GameManager] Level Completed");
    }

    public void gameOver(){
        gameState = GameState.GameOver;
    }

    public void pauseGame(){
        // TODO: lam di thang ngu
    }

    public void resumeGame(){
        // TODO: lam di thang ngu

    }

    public void quitGame(){
        Platform.exit();
    }

    public void giveUp(){
        System.out.println("[GameManager] Giving Up");

        hasSave = false;
        SceneManager.loadScene(SceneKey.Menu);
    }

    public void toNextLevel(){
        System.out.println("[GameManager] To Next Level");

        currentLevel++;
        loadLevel(currentLevel);
    }

}
