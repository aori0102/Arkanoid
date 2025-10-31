package game.GameManager.Score;

import game.Brick.BrickType;
import game.MapGenerator.BrickMapManager;
import org.Event.EventHandler;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

public final class ScoreManager extends MonoBehaviour {

    private static ScoreManager instance = null;

    private int score = 0;
    private int combo = 0;

    public EventHandler<Integer> onScoreChanged = new EventHandler<>(ScoreManager.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public ScoreManager(GameObject owner) {
        super(owner);
        if (instance != null) {
            throw new ReinitializedSingletonException("ScoreManager is a singleton");
        }
        instance = this;
    }

    @Override
    public void start() {
        BrickMapManager.getInstance().onBrickDestroyed
                .addListener(this::brickMapManager_onBrickDestroyed);
    }

    /**
     * Called when {@link BrickMapManager#onBrickDestroyed} is invoked.<br><br>
     * This function handles score after a brick is destroyed.
     *
     * @param sender Event caller {@link BrickMapManager}.
     * @param e      Event argument indicating the type of brick.
     */
    private void brickMapManager_onBrickDestroyed(Object sender, BrickType e) {
        combo++;
        score += e.score + combo;
        onScoreChanged.invoke(this, score);
    }

    public static ScoreManager getInstance() {
        return instance;
    }
}