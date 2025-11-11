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
 * Duplicate the number of the balls currently existing
 */
public class DuplicateBall extends MultipleBall {

    private EventActionID duplicateBallEventActionID = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public DuplicateBall(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        setPowerUpIndex(PowerUpIndex.DuplicateBall);
    }

    /**
     * Override the handleOnMultipleRequest method from MultipleBall base class
     * Will spawn a ball which direction makes with the current ball direction a
     * 45'degree angle
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

    @Override
    public void onApplied() {
        handleOnMultipleRequest();
        GameObjectManager.destroy(gameObject);
    }

}
