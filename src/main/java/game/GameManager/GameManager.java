package game.GameManager;

import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Scene.SceneKey;
import org.Scene.SceneManager;

/// dit me dui
public class GameManager extends MonoBehaviour {
    public static GameManager instance;
    private static GameState gameState = GameState.MainMenu;
    public EventHandler<Void> OnGameStarted = new EventHandler<>(this);
    public EventHandler<Void> OnGameOver = new EventHandler<>(this);
    public EventHandler<Void> OnGamePaused = new EventHandler<>(this);
    public EventHandler<Void> OnGameResumed = new EventHandler<>(this);


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


    public void startGame(){
        SceneManager.loadScene(SceneKey.MainGame);
        gameState = GameState.MainMenu;

        OnGameStarted.invoke(this, null);
    }

    public void gameOver(){
        gameState = GameState.GameOver;

        OnGameOver.invoke(this, null);
    }

    public void pauseGame(){
        OnGamePaused.invoke(this, null);
    }

    public void resumeGame(){
        OnGameResumed.invoke(this, null);
    }

    @Override
    protected void destroyComponent() {

    }
}
