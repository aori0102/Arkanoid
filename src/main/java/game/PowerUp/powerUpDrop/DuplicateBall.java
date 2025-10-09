package game.PowerUp.powerUpDrop;

import game.Interface.ICanDrop;
import game.PowerUp.PowerUp;
import game.PowerUp.PowerUpIndex;
import game.PowerUp.PowerUpManager;
import game.object.Ball;
import game.object.Paddle;
import org.*;
import utils.Time;
import utils.Vector2;

public class DuplicateBall extends PowerUp implements ICanDrop {

    private Paddle paddle;
    private int index = 1;
    private boolean isMoving = false;

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
        PowerUpManager.instance.onMultipleRequest.addListener((sender, multipleNumber) -> {
            handleOnMultipleRequest(multipleNumber);
        });
    }

    public void update() {
        handleDroppingMovement(TRAVEL_SPEED);
    }


    private void handleOnMultipleRequest(int multipleNumber) {
        int tries = multipleNumber - 1;
        for (int i = 0; i < tries; i++) {
            var ball = spawnBall();

            ball.getTransform().setGlobalPosition(paddle.getTransform().getGlobalPosition());
            ball.setPaddle(paddle);
            ball.setDirection(Vector2.up());
        }
    }

    private Ball spawnBall() {
        var ball = GameObjectManager.instantiate(ballNameBuilder());
        ball.addComponent(Ball.class);
        ball.addComponent(BoxCollider.class);
        ball.getComponent(Ball.class).setPaddle(paddle.getComponent(Paddle.class));
        ball.getTransform().setGlobalScale(new Vector2(1.25, 1.25));

        var ballVisual = GameObjectManager.instantiate(ballVisualBuilder());
        ballVisual.getTransform().setParent(ball.getTransform());
        ballVisual.addComponent(SpriteRenderer.class).setImage(ImageAsset.ImageIndex.Ball.getImage());

        return ball.getComponent(Ball.class);
    }

    private String ballNameBuilder() {
        return "Ball" + index;
    }

    private String ballVisualBuilder() {
        return "BallVisual" + index++;
    }

    public void setPaddle(Paddle paddle) {
        this.paddle = paddle;
    }

    @Override
    public void handleDroppingMovement(double droppingSpeed) {
        if (!isMoving) return;

        getTransform().translate(Vector2.down().multiply(TRAVEL_SPEED * Time.deltaTime));
    }
}
