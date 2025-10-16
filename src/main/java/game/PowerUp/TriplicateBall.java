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
 * Triplicate the number of the balls currently existing
 */
public class TriplicateBall extends MultipleBall {

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public TriplicateBall(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        setPowerUpIndex(PowerUpIndex.TriplicateBall);
    }

    @Override
    public void start() {
        PowerUpManager.instance.onTriplicateBall.addListener((sender, args) -> {
            handleOnMultipleRequest();
        });
    }

    /**
     * Override the handleOnMultipleRequest method from MultipleBall base class
     * Will spawn two balls which direction makes with the current ball direction a
     * 45'degree angle
     */
    @Override
    protected void handleOnMultipleRequest() {
        HashSet<Ball> ballHashSet = new HashSet<>(BallsManager.instance.getBallSet());

        for(Ball ball : ballHashSet) {
            Vector2 normalVector = new Vector2(-ball.getDirection().y, ball.getDirection().x).normalize();
            Vector2 firstDirection = ball.getDirection().add(normalVector).normalize();
            Vector2 secondDirection = ball.getDirection().add(normalVector.multiply(-1)).normalize();

            var ball1 = spawnBall(ball.getTransform());
            ball1.setPaddle(paddle.getComponent(Paddle.class));
            BallsManager.instance.addBall(ball1);
            ball1.setDirection(firstDirection);

            var ball2 = spawnBall(ball.getTransform());
            ball2.setPaddle(paddle.getComponent(Paddle.class));
            BallsManager.instance.addBall(ball2);
            ball2.setDirection(secondDirection);
        }
    }


}
