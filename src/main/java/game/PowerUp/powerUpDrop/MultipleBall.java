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

    protected void handleOnMultipleRequest() {

    }

    protected Ball spawnBall(Transform spawnPos) {
        var ball = GameObjectManager.instantiate(BallsManager.instance.ballNameBuilder());
        ball.addComponent(Ball.class);
        ball.addComponent(BoxCollider.class);
        ball.getTransform().setGlobalPosition(spawnPos.getGlobalPosition());
        ball.getTransform().setGlobalScale(new Vector2(1.25, 1.25));

        var ballVisual = GameObjectManager.instantiate(BallsManager.instance.ballVisualNameBuilder());
        ballVisual.getTransform().setParent(ball.getTransform());
        ballVisual.addComponent(SpriteRenderer.class).setImage(ImageAsset.ImageIndex.Ball.getImage());

        return ball.getComponent(Ball.class);
    }

    public void linkPlayerPowerUp(PlayerPowerUpHandler playerPowerUpHandler) {
        this.playerPowerUpHandler = playerPowerUpHandler;
    }

    public void linkPaddle(Paddle paddle) {
        this.paddle = paddle;
    }

    @Override
    public void handleDroppingMovement(double droppingSpeed) {
        if (shouldBeDestroyed) {
            GameObjectManager.destroy(gameObject);
            return;
        } else
        {
            getTransform().translate(Vector2.down().multiply(TRAVEL_SPEED * Time.deltaTime));
        }

    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
    }

    @Override
    protected void destroyComponent() {

    }
}
