package game.PowerUp.powerUpDrop;

import game.PowerUp.PowerUpIndex;
import game.PowerUp.PowerUpManager;
import game.object.Ball;
import game.object.Paddle;
import org.*;
import utils.Random;
import utils.Vector2;

import java.util.HashSet;

public class DuplicateBall extends MultipleBall {


    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public DuplicateBall(GameObject owner) {
        super(owner);

    }

    public void awake() {
        setPowerUpIndex(PowerUpIndex.DuplicateBall);
    }

    public void start() {
        PowerUpManager.instance.onDuplicateBall.addListener((sender, args) -> {
            handleOnMultipleRequest();
        });
    }

    public void update() {
        handleDroppingMovement(TRAVEL_SPEED);
    }

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
