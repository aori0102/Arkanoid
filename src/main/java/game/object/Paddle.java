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
    private final double dotLimitAngle = 60;

    public EventHandler<Vector2> onMouseReleased = new EventHandler<Vector2>();

    private ActionMap actionMap;
    private PlayerInput playerInput;
    private BoxCollider boxCollider;
    private Vector2 fireDirection;
    private Line line;

    public boolean isFired = false;
    public Vector2 movementVector = new Vector2(0, 0);

    private Vector2 normalVector = new Vector2(0, -1);

    public Paddle(GameObject owner) {
        super(owner);
    }

    /**
     * Initialize paddle specs.
     */
    public void awake() {
        // Assign components
        actionMap = gameObject.getComponent(ActionMap.class);
        boxCollider = gameObject.getComponent(BoxCollider.class);
        playerInput = gameObject.getComponent(PlayerInput.class);

        //Assign collider specs
        boxCollider.setLocalCenter(new Vector2(0, 0));
        boxCollider.setLocalSize(new Vector2(80, 20));

        //Assign line specs
        line = new Line();
        line.setStroke(Color.RED);
        line.setStrokeWidth(5);
        playerInput.getRoot().getChildren().add(line);
    }

    public void update() {
        handleMovement();
    }

    @Override
    protected void destroyMono() {

    }

    /**
     * Handle the movement of the paddle.
     * Using Action defined in Action map to decide move direction.
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
                // Set the movement vector to zero to stop the paddle
                movementVector = new Vector2(0, 0);

                // Turn off the line
                line.setVisible(false);

                //Fire the ball if the ball is not fired
                if (playerInput.isMouseReleased) {
                    if (isDirectionValid(fireDirection)) {
                        onMouseReleased.invoke(fireDirection);
                        playerInput.isMouseReleased = false;
                    }
                }
            }
        }
    }

    /**
     * Handle the direction ray.
     */
    private void HandleRayDirection() {
        if (isFired) {
            return;
        }

        // If the mouse input is left button, then turn on the line and calculate the fire direction as well as the line's end point
        if (playerInput.getMouseEvent(MouseButton.PRIMARY) != null) {
            line.setVisible(true);
            // Get mouse event
            MouseEvent mouseEvent = playerInput.getMouseEvent(MouseButton.PRIMARY);

            // Get mouse position
            Vector2 mousePos = new Vector2(mouseEvent.getX(), mouseEvent.getY());

            // Calculate fire direction and direction
            fireDirection = ((transform().getGlobalPosition()
                    .subtract(mousePos)).normalize());
            Vector2 lineEndPoint = transform().getGlobalPosition()
                    .add(fireDirection.multiply(100));

            // If the direction is in the valid range then draw it
            if (isDirectionValid(fireDirection)) {
                line.setStartX(transform().getGlobalPosition().x);
                line.setStartY(transform().getGlobalPosition().y);
                line.setEndX(lineEndPoint.x);
                line.setEndY(lineEndPoint.y);
            } else {
                line.setVisible(false);
            }
        }
    }

    /**
     * Check if the direction is in the valid range.
     * @param direction : the fire direction.
     * @return true if the direction is valid.
     */
    private boolean isDirectionValid(Vector2 direction) {
        if (direction == null) return false;
        double angle = Vector2.angle(direction.normalize(), normalVector);
        return Math.toDegrees(angle) <= dotLimitAngle;
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
    }

    @Override
    protected void clear() {

    }
}