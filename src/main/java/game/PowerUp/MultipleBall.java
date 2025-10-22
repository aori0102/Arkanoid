package game.PowerUp;

import game.GameObject.BallsManager;
import game.Player.PlayerPowerUpHandler;
import game.PowerUp.Index.PowerUp;
import game.GameObject.Ball;
import game.GameObject.Paddle;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.Transform;
import org.Physics.BoxCollider;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

/**
 * Base class for all the multiple ball power up
 */
public abstract class MultipleBall extends PowerUp {

    protected PlayerPowerUpHandler playerPowerUpHandler;
    protected Paddle paddle;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public MultipleBall(GameObject owner) {
        super(owner);
    }

    /**
     * Handle the onMultipleRequest event. This will be called when the paddle
     * collided with a power up. The child class will override this method.
     */
    protected abstract void handleOnMultipleRequest();

    /**
     * Spawning the ball
     *
     * @param spawnPos : the spawn position
     * @return the spawned ball
     */
    protected Ball spawnBall(Transform spawnPos) {
        var ball = GameObjectManager.instantiate(BallsManager.instance.ballNameBuilder());
        ball.addComponent(Ball.class).setPendingEffect(BallsManager.instance.getCurrentEffect());
        ball.addComponent(BoxCollider.class);
        ball.getTransform().setGlobalPosition(spawnPos.getGlobalPosition());
        ball.getTransform().setGlobalScale(new Vector2(1.25, 1.25));

        var ballVisual = GameObjectManager.instantiate(BallsManager.instance.ballVisualNameBuilder());
        ballVisual.setParent(ball);
        ballVisual.addComponent(SpriteRenderer.class).setImage(ImageAsset.ImageIndex.Ball.getImage());
        ballVisual.getComponent(SpriteRenderer.class).setPivot(new Vector2(0.5, 0.5));

        return ball.getComponent(Ball.class);
    }

    /**
     * Link the player power up
     *
     * @param playerPowerUpHandler : the linked playerPowerUpHandler
     */
    public void linkPlayerPowerUp(PlayerPowerUpHandler playerPowerUpHandler) {
        this.playerPowerUpHandler = playerPowerUpHandler;
    }

    /**
     * Link the paddle
     *
     * @param paddle: the linked paddle
     */
    public void linkPaddle(Paddle paddle) {
        this.paddle = paddle;
    }

    @Override
    protected void destroyComponent() {
        playerPowerUpHandler = null;
        paddle = null;
    }

}