package game.object;

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

    public EventHandler<Vector2> onMouseReleased = new EventHandler<Vector2>();

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


        line = new Line();
        line.setStroke(Color.RED);
        line.setStrokeWidth(5);
        playerInput.getRoot().getChildren().add(line);
    }

    public void update() {
        handleMovement();
        drawCollider();
    }

    @Override
    protected void destroyMono() {

    }

    /**
     * Handle the movement of the paddle
     * Using Action defined in Action map to decide move direction
     */
    public void handleMovement() {

        transform().translate(movementVector);
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
                    onMouseReleased.invoke(fireDirection);
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
            fireDirection = ((transform().getGlobalPosition()
                    .add(new Vector2(50, 50)))
                    .subtract(mousePos)).normalize();
            Vector2 direction = transform().getGlobalPosition()
                    .add(fireDirection.multiply(100));

            line.setStartX(transform().getGlobalPosition().x);
            line.setStartY(transform().getGlobalPosition().y);
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
        Vector2 center = transform().getGlobalPosition().add(boxCollider.getLocalCenter());
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
    protected void clear() {

    }
}