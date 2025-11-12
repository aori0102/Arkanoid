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
 * Triplicate Ball power-up.
 *
 * When applied, this power-up creates two additional balls for each existing ball,
 * forming a 45-degree angle relative to the original ball's direction.
 * This effectively triples the number of balls in play.
 */
public class TriplicateBall extends MultipleBall {

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The GameObject that owns this component.
     */
    public TriplicateBall(GameObject owner) {
        super(owner);
        System.out.println("TriplicateBall constructor " + this);
    }

    @Override
    public void awake() {
        setPowerUpIndex(PowerUpIndex.TriplicateBall);
    }

    /**
     * Handles the ball multiplication logic.
     * <p>
     * For each existing ball, this method spawns two new balls at the same position.
     * The new balls' directions form ±45° angles with the original ball's direction.
     */
    @Override
    protected void handleOnMultipleRequest() {
        HashSet<Ball> ballHashSet = new HashSet<>(BallsManager.getInstance().getBallSet());

        for (Ball ball : ballHashSet) {
            Vector2 normalVector = new Vector2(-ball.getDirection().y, ball.getDirection().x).normalize();
            Vector2 firstDirection = ball.getDirection().add(normalVector).normalize();
            Vector2 secondDirection = ball.getDirection().add(normalVector.multiply(-1)).normalize();

            var firstBall = PrefabManager.instantiatePrefab(PrefabIndex.Ball)
                    .getComponent(Ball.class);
            firstBall.getTransform().setGlobalPosition(ball.getTransform().getGlobalPosition());
            firstBall.setDirection(firstDirection);

            var secondBall = PrefabManager.instantiatePrefab(PrefabIndex.Ball)
                    .getComponent(Ball.class);
            secondBall.getTransform().setGlobalPosition(ball.getTransform().getGlobalPosition());
            secondBall.setDirection(secondDirection);
        }
    }

    /**
     * Called when this power-up is applied.
     * <p>
     * This method triggers the ball multiplication and then destroys
     * the power-up object to remove it from the game.
     */
    @Override
    public void onApplied() {
        handleOnMultipleRequest();
        GameObjectManager.destroy(gameObject);
    }

}
