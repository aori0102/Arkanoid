package game.PowerUp;

import game.Ball.BallsManager;
import game.PowerUp.Index.PowerUpIndex;
import game.PowerUp.Index.PowerUpManager;
import game.Ball.Ball;
import org.Event.EventActionID;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;
import utils.Vector2;

import java.util.HashSet;

/**
 * DuplicateBall is a type of power-up that duplicates all currently existing balls.
 *
 * When applied, for each existing ball, it spawns a new ball whose direction
 * is slightly rotated (45 degrees) relative to the original ball's direction.
 * After applying its effect, the power-up destroys itself.
 */
public class DuplicateBall extends MultipleBall {


    /**
     * Create this MonoBehaviour.
     *
     * @param owner The GameObject that owns this component.
     */
    public DuplicateBall(GameObject owner) {
        super(owner);
    }

    /**
     * Initialize the DuplicateBall power-up.
     * Sets the PowerUpIndex to DuplicateBall.
     */
    @Override
    public void awake() {
        setPowerUpIndex(PowerUpIndex.DuplicateBall);
    }

    /**
     * Override the handleOnMultipleRequest method from MultipleBall base class.
     * Spawns a new ball for each existing ball. The new ball's direction is rotated
     * 45 degrees relative to the current ball's direction.
     */
    @Override
    protected void handleOnMultipleRequest() {
        HashSet<Ball> ballHashSet = new HashSet<>(BallsManager.getInstance().getBallSet());

        for (var ball : ballHashSet) {
            Vector2 normalVector = new Vector2(-ball.getDirection().y, ball.getDirection().x).normalize();
            Vector2 direction = ball.getDirection().add(normalVector).normalize();

            var newBall = PrefabManager.instantiatePrefab(PrefabIndex.Ball)
                    .getComponent(Ball.class);
            newBall.getTransform().setGlobalPosition(ball.getTransform().getGlobalPosition());
            newBall.setDirection(direction);
        }
    }

    /**
     * Called when the power-up is applied.
     * Executes the duplication logic and destroys this power-up GameObject.
     */
    @Override
    public void onApplied() {
        handleOnMultipleRequest();
        GameObjectManager.destroy(gameObject);
    }

}
