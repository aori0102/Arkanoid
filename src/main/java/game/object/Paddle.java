package game.object;

import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import org.*;
import utils.Time;
import utils.Vector2;

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
        boxCollider.setLocalSize(new Vector2(100, 100));
        transform().setGlobalPosition(new Vector2(600, 600));
        transform().setGlobalScale(new Vector2(0.2, 0.2));

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
     * Handle the movement of the paddle
     * Using Action defined in Action map to decide move direction
     */
    public void handleMovement() {

        // Current action
        switch (actionMap.currentAction) {
            // Go left
            case GoLeft -> transform().translate(new Vector2(-paddleSpeed * Time.deltaTime, 0));
            // Go Right
            case GoRight -> transform().translate(new Vector2(paddleSpeed * Time.deltaTime, 0));
            // Adjust ray direction by pressing left mouse button
            case MousePressed -> {
                HandleRayDirection();
            }
            default -> {
                line.setVisible(false);
                if (playerInput.isMouseReleased) {
                    onMouseReleased.invoke(fireDirection);
                    playerInput.isMouseReleased = false;
                }
            }
        }
    }


    private void HandleRayDirection() {
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


    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
    }

    @Override
    protected void clear() {

    }
}