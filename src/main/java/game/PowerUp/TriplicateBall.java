package game.PowerUp;

import game.GameObject.BallsManager;
import game.PowerUp.Index.PowerUpIndex;
import game.PowerUp.Index.PowerUpManager;
import game.GameObject.Ball;
import game.Player.PlayerPaddle;
import org.Event.EventActionID;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import utils.Vector2;

import java.util.HashSet;

/**
 * Triplicate the number of the balls currently existing
 */
public class TriplicateBall extends MultipleBall {

    private EventActionID triplicateBallEventActionID = null;

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
        triplicateBallEventActionID = PowerUpManager.getInstance().onTriplicateBall.addListener((sender, args) -> {
            System.out.println("TriplicateBall start");
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
        HashSet<Ball> ballHashSet = new HashSet<>(BallsManager.getInstance().getBallSet());

        for(Ball ball : ballHashSet) {
            Vector2 normalVector = new Vector2(-ball.getDirection().y, ball.getDirection().x).normalize();
            Vector2 firstDirection = ball.getDirection().add(normalVector).normalize();
            Vector2 secondDirection = ball.getDirection().add(normalVector.multiply(-1)).normalize();

            var ball1 = spawnBall(ball.getTransform());
            BallsManager.getInstance().addBall(ball1);
            ball1.setDirection(firstDirection);

            var ball2 = spawnBall(ball.getTransform());
            BallsManager.getInstance().addBall(ball2);
            ball2.setDirection(secondDirection);
        }

        // TODO : fix this shit too
    }

    @Override
    public void onApplied() {
        System.out.println("TriplicateBall onApplied");
        PowerUpManager.getInstance().onTriplicateBall.removeListener(triplicateBallEventActionID);
        GameObjectManager.destroy(gameObject);
    }

}
