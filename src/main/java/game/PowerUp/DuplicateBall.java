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

    @Override
    public void start() {
        duplicateBallEventActionID = PowerUpManager.getInstance().onDuplicateBall.
                addListener((_, _) -> handleOnMultipleRequest());
    }

    /**
     * Override the handleOnMultipleRequest method from MultipleBall base class
     * Will spawn a ball which direction makes with the current ball direction a
     * 45'degree angle
     */
    @Override
    protected void handleOnMultipleRequest() {
        /*HashSet<Ball> ballHashSet = new HashSet<>(BallsManager.instance.getBallSet());

        for (var ball : ballHashSet) {
            Vector2 normalVector = new Vector2(-ball.getDirection().y, ball.getDirection().x).normalize();
            Vector2 direction = ball.getDirection().add(normalVector).normalize();

            var newBall = spawnBall(ball.getTransform());
            newBall.setPaddle(paddle.getComponent(PlayerPaddle.class));
            BallsManager.instance.addBall(newBall);
            newBall.setDirection(direction);
        }

         */
        // TODO : Fix this shit
    }

    @Override
    public void onApplied() {
        PowerUpManager.getInstance().onDuplicateBall.removeListener(duplicateBallEventActionID);
        GameObjectManager.destroy(gameObject);
    }

    @Override
    protected void destroyComponent() {
        duplicateBallEventActionID = null;
    }
}
