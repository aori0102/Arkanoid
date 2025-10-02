package game.object;

import game.PowerUp.PowerUp;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import org.*;
import utils.Time;
import utils.Vector2;
import javafx.scene.shape.Rectangle;


import javafx.scene.input.MouseEvent;

import java.awt.*;

public class Paddle extends MonoBehaviour {

    private final double paddleSpeed = 1000;

    public EventHandler<Vector2> onMouseReleased = new EventHandler<>(this);
    public EventHandler<PowerUp> onPowerUpConsumed = new EventHandler<>(this);

    private ActionMap actionMap;
    private PlayerInput playerInput;
    private BoxCollider boxCollider;
    private Vector2 fireDirection;
    private Line line;

    public boolean isFired = false;
    public Vector2 movementVector = new Vector2(0, 0);

    private Rectangle colliderRect;

    /**
     * Get the game object this MonoBehaviour is attached to.
     *
     * @param owner The owner of this component.
     */
    public Paddle(GameObject owner) {
        super(owner);
    }

    /**
     * Initialize paddle specs.
     */
    public void awake() {
        actionMap = gameObject.getComponent(ActionMap.class);
        boxCollider = gameObject.getComponent(BoxCollider.class);
        playerInput = gameObject.getComponent(PlayerInput.class);
        boxCollider.setLocalCenter(new Vector2(0, 0));
        boxCollider.setLocalSize(new Vector2(80, 20));
        boxCollider.setOnTriggerEnter(this::onTriggerEnter);

        line = new Line();
        line.setStroke(Color.RED);
        line.setStrokeWidth(5);
        playerInput.getRoot().getChildren().add(line);
    }

    private void onTriggerEnter(CollisionData collisionData) {

        var powerUp = collisionData.otherCollider.getComponent(PowerUp.class);
        if (powerUp != null) {
            onPowerUpConsumed.invoke(this, powerUp);
        }

    }

    public void update() {
        handleMovement();
        drawCollider();
    }

    /**
     * Handle the movement of the paddle
     * Using Action defined in Action map to decide move direction
     */
    public void handleMovement() {

        getTransform().translate(movementVector);
        // Current action
        switch (actionMap.currentAction) {
            // Go left
            case GoLeft -> movementVector = new Vector2(-paddleSpeed * Time.deltaTime, 0);
            // Go Right
            case GoRight -> movementVector = new Vector2(paddleSpeed * Time.deltaTime, 0);
            // Adjust ray direction by pressing left mouse button
            case MousePressed -> {
                HandleRayDirection();
            }
            default -> {
                movementVector = new Vector2(0, 0);
                line.setVisible(false);
                if (playerInput.isMouseReleased) {
                    onMouseReleased.invoke(this, fireDirection);
                    playerInput.isMouseReleased = false;
                }
            }
        }
    }

    /**
     *
     */
    private void HandleRayDirection() {
        if (isFired) {
            return;
        }

        if (playerInput.getMouseEvent(MouseButton.PRIMARY) != null) {
            line.setVisible(true);
            MouseEvent mouseEvent = playerInput.getMouseEvent(MouseButton.PRIMARY);

            Vector2 mousePos = new Vector2(mouseEvent.getX(), mouseEvent.getY());
            fireDirection = ((getTransform().getGlobalPosition()
                    .add(new Vector2(50, 50)))
                    .subtract(mousePos)).normalize();
            Vector2 direction = getTransform().getGlobalPosition()
                    .add(fireDirection.multiply(100));

            line.setStartX(getTransform().getGlobalPosition().x);
            line.setStartY(getTransform().getGlobalPosition().y);
            line.setEndX(direction.x);
            line.setEndY(direction.y);
        }
    }

    public void drawCollider() {
        if (colliderRect == null) {
            colliderRect = new Rectangle();
            colliderRect.setStroke(Color.BLUE);   // màu viền
            colliderRect.setFill(Color.TRANSPARENT); // không tô màu
            colliderRect.setStrokeWidth(2);
            playerInput.getRoot().getChildren().add(colliderRect);
        }

        // Lấy center và size từ BoxCollider
        Vector2 center = getTransform().getGlobalPosition().add(boxCollider.getLocalCenter());
        Vector2 size = boxCollider.getLocalSize();

        colliderRect.setX(center.x - size.x / 2);
        colliderRect.setY(center.y - size.y / 2);
        colliderRect.setWidth(size.x);
        colliderRect.setHeight(size.y);
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
    }

    @Override
    protected void destroyComponent() {

    }

}