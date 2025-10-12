package game.PowerUp.powerUpDrop;

import game.Interface.ICanDrop;
import game.Player.PlayerPowerUpHandler;
import game.PowerUp.PowerUp;
import game.PowerUp.PowerUpIndex;
import game.PowerUp.PowerUpManager;
import game.object.Ball;
import game.object.Paddle;
import org.*;
import utils.Random;
import utils.Time;
import utils.Vector2;

import java.util.HashSet;

public class DuplicateBall extends PowerUp implements ICanDrop {

    private BoxCollider collider;
    private PlayerPowerUpHandler playerPowerUpHandler;
    private Paddle paddle;

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
        playerPowerUpHandler.onBallMultiplyRequested.addListener((sender, args) -> {
            handleOnMultipleRequest();
        });
    }

    public void update() {
        handleDroppingMovement(TRAVEL_SPEED);
    }


    private void handleOnMultipleRequest() {
        HashSet<Ball> ballHashSet = new HashSet<>(BallsManager.instance.getBallSet());

        for (var ball : ballHashSet) {
            var newBall = spawnBall(ball.getTransform());
            newBall.setPaddle(paddle.getComponent(Paddle.class));
            BallsManager.instance.addBall(newBall);
            double x = Random.Range(-1, 1);
            Vector2 direction = new Vector2(x, ball.getDirection().y);
            newBall.setDirection(direction);
        }
    }

    private Ball spawnBall(Transform spawnPos) {
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
}
