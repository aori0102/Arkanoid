package game.PowerUp;

import game.GameObject.BallsManager;
import game.PowerUp.Index.PowerUpIndex;
import game.PowerUp.Index.PowerUpManager;
import game.GameObject.Ball;
import game.GameObject.Paddle;
import org.GameObject.GameObject;
import utils.Vector2;

import java.util.HashSet;

/**
 * Duplicate the number of the balls currently existing
 */
public class DuplicateBall extends MultipleBall {

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

    @Override
    public void start() {
        PowerUpManager.instance.onDuplicateBall.addListener((sender, args) -> {
            handleOnMultipleRequest();
        });
    }

    /**
     * Override the handleOnMultipleRequest method from MultipleBall base class
     * Will spawn a ball which direction makes with the current ball direction a
     * 45'degree angle
     */
    @Override
    protected void handleOnMultipleRequest() {
        HashSet<Ball> ballHashSet = new HashSet<>(BallsManager.instance.getBallSet());

        for (var ball : ballHashSet) {
            Vector2 normalVector = new Vector2(-ball.getDirection().y, ball.getDirection().x).normalize();
            Vector2 direction = ball.getDirection().add(normalVector).normalize();

            var newBall = spawnBall(ball.getTransform());
            newBall.setPaddle(paddle.getComponent(Paddle.class));
            BallsManager.instance.addBall(newBall);
            newBall.setDirection(direction);
        }
    }

}
