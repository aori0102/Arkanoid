package game.PowerUp.powerUpDrop;

import game.Interface.ICanDrop;
import game.Player.PlayerPowerUpHandler;
import game.PowerUp.PowerUp;
import game.object.Ball;
import game.object.Paddle;
import org.*;
import utils.Time;
import utils.Vector2;

import java.util.function.BiConsumer;

/**
 * Base class for all the multiple ball power up
 */
public class MultipleBall extends PowerUp implements ICanDrop {

    protected PlayerPowerUpHandler playerPowerUpHandler;
    protected Paddle paddle;
    private BiConsumer<Object, Integer> multiplyListener;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public MultipleBall(GameObject owner) {
        super(owner);
    }

    public void start() {
    }

    /**
     * Handle the onMultipleRequest event. This will be called when the paddle
     * collided with a power up. The child class will override this method.
     * Will throw an exception if the child does not override it
     */
    protected void handleOnMultipleRequest() {
        throw new UnsupportedOperationException(
                this.getClass().getSimpleName() + " must override handleOnMultipleRequest()!"
        );
    }

    /**
     * Spawning the ball
     * @param spawnPos : the spawn position
     * @return the spawned ball
     */
    protected Ball spawnBall(Transform spawnPos) {
        var ball = GameObjectManager.instantiate(BallsManager.instance.ballNameBuilder());
        ball.addComponent(Ball.class);
        ball.addComponent(BoxCollider.class);
        ball.getTransform().setGlobalPosition(spawnPos.getGlobalPosition());
        ball.getTransform().setGlobalScale(new Vector2(1.25, 1.25));

        var ballVisual = GameObjectManager.instantiate(BallsManager.instance.ballVisualNameBuilder());
        ballVisual.setParent(ball);
        ballVisual.addComponent(SpriteRenderer.class).setImage(ImageAsset.ImageIndex.Ball.getImage());

        return ball.getComponent(Ball.class);
    }

    /**
     * Link the player power up
     * @param playerPowerUpHandler : the linked playerPowerUpHandler
     */
    public void linkPlayerPowerUp(PlayerPowerUpHandler playerPowerUpHandler) {
        this.playerPowerUpHandler = playerPowerUpHandler;
    }

    /**
     * Link the paddle
     * @param paddle: the linked paddle
     */
    public void linkPaddle(Paddle paddle) {
        this.paddle = paddle;
    }

    /**
     * Handle dropping movement. This method is override from
     * the handleDroppingMovement method in ICanDrop interface
     * @param droppingSpeed
     */
    @Override
    public void handleDroppingMovement(double droppingSpeed) {
        getTransform().translate(Vector2.down().multiply(TRAVEL_SPEED * Time.deltaTime));
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
    }

    @Override
    protected void destroyComponent() {

    }
}
