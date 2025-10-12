package game.PowerUp.powerUpDrop;

import game.PowerUp.PowerUpIndex;
import game.PowerUp.PowerUpManager;
import game.object.Ball;
import game.object.Paddle;
import org.*;
import utils.Random;
import utils.Vector2;

import java.util.HashSet;

public class TriplicateBall extends MultipleBall {

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public TriplicateBall(GameObject owner) {
        super(owner);
    }

    public void awake() {
        setPowerUpIndex(PowerUpIndex.TriplicateBall);
    }

    public void start() {
        PowerUpManager.instance.onTriplicateBall.addListener((sender, args) -> {
            handleOnMultipleRequest();
        });
    }

    public void update() {
        handleDroppingMovement(TRAVEL_SPEED);
    }

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
