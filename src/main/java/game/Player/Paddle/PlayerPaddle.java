package game.Player.Paddle;

import game.Ball.Ball;
import game.Entity.EntityHealthAlterType;
import game.GameManager.GameManager;
import game.GameManager.GameState;
import game.GameObject.Arrow;
import game.Obstacle.Index.ObstacleManager;
import game.Player.Player;
import game.Player.PlayerData;
import game.PowerUp.Index.PowerUp;
import game.PowerUp.Recovery;
import javafx.scene.input.MouseButton;
import org.Event.EventActionID;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.InputAction.ActionMap;
import org.Layer.Layer;
import game.Particle.PaddleParticle;
import org.Physics.BoxCollider;
import org.Physics.PhysicsManager;
import utils.Time;
import utils.Vector2;

import javafx.scene.input.MouseEvent;

public class PlayerPaddle extends MonoBehaviour {

    // The constant specs of the ball
    private static final double DOT_LIMIT_ANGLE_RIGHT = 30;
    private static final double DOT_LIMIT_ANGLE_LEFT = 150;
    private static final double STUNNED_TIME = 3.6;
    private static final Vector2 DIRECTION_VECTOR = new Vector2(1, 0);
    private static final int DASH_SPEED = 2500;

    private final PaddleHealth paddleHealth = addComponent(PaddleHealth.class);
    private final PaddleStat paddleStat = addComponent(PaddleStat.class);
    private final BoxCollider boxCollider = addComponent(BoxCollider.class);

    private Arrow arrow;
    private Vector2 fireDirection = new Vector2();

    private boolean canInvoke;
    private boolean canStartStunnedCounter = false;
    private boolean canReduceSpeed = true;
    private double stunnedCounter = 0;

    // Event
    public EventHandler<Vector2> onMouseReleased = new EventHandler<Vector2>(PlayerPaddle.class);
    public EventHandler<PowerUp> onPowerUpConsumed = new EventHandler<>(PlayerPaddle.class);

    private EventActionID ball_onAnyBallDestroyed_ID = null;

    public boolean isFired = false;

    private Vector2 movementVector = new Vector2(0, 0);

    private Time.CoroutineID dashCoroutineID = null;

    public PlayerPaddle(GameObject owner) {
        super(owner);
        owner.setLayer(Layer.Paddle);
    }

    /**
     * Initialize paddle specs.
     */
    @Override
    public void awake() {
        ObstacleManager.getInstance().onPaddleCollidedWithObstacle.addListener((e, voi) -> {
            canStartStunnedCounter = true;
        });

        spawnParticle();

        Player.getInstance().getPlayerController().getActionMap().
                onKeyHeld.addListener(this::handlePaddleMovement);
        Player.getInstance().getPlayerController().getActionMap().
                onMouseHeld.addListener(this::handleRayDirection);
        Player.getInstance().getPlayerController().getActionMap().
                onMouseReleased.addListener(this::handleRayReleased);
    }

    @Override
    public void start() {
        paddleHealth.onPaddleHealthReachesZero
                .addListener(this::playerPaddleHealth_onPaddleHealthReachesZero);
        ball_onAnyBallDestroyed_ID = Ball.onAnyBallDestroyed
                .addListener(this::ball_onAnyBallDestroyed);
    }

    @Override
    public void update() {
        handleCollisionWithObstacles();
        handlePowerUps();
    }

    private void handlePowerUps() {

        var overlapCollider = PhysicsManager.getOverlapColliders(boxCollider, true);
        for (var other : overlapCollider) {
            var powerUp = other.getComponent(PowerUp.class);
            if (powerUp != null) {
                if (powerUp instanceof Recovery || isFired) {
                    onPowerUpConsumed.invoke(this, powerUp);
                }
            }
        }

    }

    @Override
    public void onDestroy() {
        Ball.onAnyBallDestroyed
                .removeListener(ball_onAnyBallDestroyed_ID);
    }

    /**
     * Called when {@link Ball#onAnyBallDestroyed} is invoked.<br><br>
     * This function decreases the player's health when a ball is destroyed.
     *
     * @param sender Event caller {@link Ball}.
     * @param e      Empty event argument.
     */
    private void ball_onAnyBallDestroyed(Object sender, Void e) {
        paddleHealth.alterHealth(EntityHealthAlterType.PlayerTakeDamage, PlayerData.HEALTH_LOST_ON_BALL_DESTROYED);
    }

    /**
     * Called when {@link PaddleHealth#onPaddleHealthReachesZero} is invoked.<br><br>
     * This function destroys the paddle when paddle's health reaches zero.
     *
     * @param sender Event caller {@link PaddleHealth}.
     * @param e      Empty event argument.
     */
    private void playerPaddleHealth_onPaddleHealthReachesZero(Object sender, Void e) {
        GameObjectManager.destroy(gameObject);
    }

    /**
     * Get the paddle's current movement vector.
     *
     * @return The paddle's current movement vector.
     */
    public Vector2 getMovementVector() {
        return movementVector;
    }

    public PaddleHealth getPaddleHealth() {
        return paddleHealth;
    }

    public PaddleStat getPaddleStat() {
        return paddleStat;
    }

    private void handlePaddleMovement(Object e, ActionMap.Action action) {

        movementVector = new Vector2(0, 0);

        switch (action) {
            case GoLeft -> movementVector = movementVector.add(new Vector2(-1, 0));
            case GoRight -> movementVector = movementVector.add(new Vector2(1, 0));
            default -> {
            }
        }

        if (!movementVector.equals(Vector2.zero())) {
            movementVector = movementVector.normalize()
                    .multiply(Player.getInstance().getCurrentSpeed() * Time.getDeltaTime());
        }

        getTransform().translate(movementVector);
    }

    private void handleRayReleased(Object e, ActionMap.Action action) {
        if (GameManager.getInstance().getGameState() != GameState.Playing) {
            return;
        }
        arrow.turnOff();
        if (isDirectionValid(fireDirection) && canInvoke) {
            onMouseReleased.invoke(this, fireDirection);
            Player.getInstance().getPlayerController().getPlayerInput().isMouseReleased = false;
        }
    }

    /**
     * Handle the direction ray.
     */
    private void handleRayDirection(Object e, ActionMap.Action action) {
        if (isFired || GameManager.getInstance().getGameState() != GameState.Playing) {
            return;
        }

        // If the mouse input is left button, then turn on the line and calculate the fire direction as well as the line's end point
        if (Player.getInstance().getPlayerController().
                getPlayerInput().getMouseEvent(MouseButton.PRIMARY) != null) {
            // Get mouse event
            MouseEvent mouseEvent = Player.getInstance().getPlayerController().
                    getPlayerInput().getMouseEvent(MouseButton.PRIMARY);

            // Get mouse position
            Vector2 mousePos = new Vector2(mouseEvent.getX(), mouseEvent.getY());

            if (mousePos.y < getTransform().getGlobalPosition().y) {
                canInvoke = false;
                return;
            } else {
                canInvoke = true;
            }

            // The direction we want the ball to follow
            Vector2 expectedDirection = ((getTransform().getGlobalPosition()
                    .subtract(mousePos)).normalize());
            // If the direction is in the range, then assigning it to fire direction
            if (isDirectionValid(expectedDirection)) {
                fireDirection = expectedDirection;
                arrow.turnOn();
                double angle = Math.toDegrees(Vector2.angle(fireDirection.normalize(), DIRECTION_VECTOR));
                arrow.handleArrowDirection(angle);
            }
        }
    }

    /**
     * Handle the movement of the paddle when an obstacle hits it. Its speed will be
     * reduced by 10 times when hit
     */
    private void handleCollisionWithObstacles() {
        if (!canStartStunnedCounter) return;
        stunnedCounter += Time.getDeltaTime();

        if (canReduceSpeed) {
            int currentSpeed = Player.getInstance().getCurrentSpeed() / 10;
            Player.getInstance().setCurrentSpeed(currentSpeed);
            canReduceSpeed = false;
        }

        if (stunnedCounter >= STUNNED_TIME) {
            Player.getInstance().setCurrentSpeed(Player.getInstance().getBaseSpeed());
            canReduceSpeed = true;
            stunnedCounter = 0;
            canStartStunnedCounter = false;
        }
    }

    /**
     * Check if the direction is in the valid range.
     *
     * @param direction : the fire direction.
     * @return true if the direction is valid.
     */
    private boolean isDirectionValid(Vector2 direction) {
        if (direction == null) return false;
        double angle = Math.toDegrees(Vector2.angle(direction.normalize(), DIRECTION_VECTOR.normalize()));
        return angle >= DOT_LIMIT_ANGLE_RIGHT && angle <= DOT_LIMIT_ANGLE_LEFT;
    }

    /**
     * Link the arrow
     *
     * @param arrow: linked arrow
     */
    public void linkArrow(Arrow arrow) {
        this.arrow = arrow;
    }

    private void spawnParticle() {
        var paddleParticle = GameObjectManager.instantiate("PaddleParticle").addComponent(PaddleParticle.class);
        paddleParticle.getGameObject().setParent(gameObject);
        paddleParticle.setPosition(new Vector2(40, 0));
        paddleParticle.setDirection(Vector2.down());
        paddleParticle.setParent(gameObject);
        paddleParticle.startEmit();

        var paddleParticle1 = GameObjectManager.instantiate("PaddleParticle").addComponent(PaddleParticle.class);
        paddleParticle1.getGameObject().setParent(gameObject);
        paddleParticle1.setPosition(new Vector2(-40, 0));
        paddleParticle1.setDirection(Vector2.down());
        paddleParticle1.setParent(gameObject);
        paddleParticle1.startEmit();
    }

}