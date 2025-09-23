package game.object;

import ecs.*;
import org.ActionMap;
import utils.Time;
import utils.Vector2;

public class Paddle extends MonoBehaviour {

    private SpriteRenderer sprite;
    private double paddleSpeed = 1000;
    private ActionMap actionMap;

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
        sprite = gameObject.getComponent(SpriteRenderer.class);

        sprite.setImage("/paddle.png");
        transform().setPosition(new Vector2(600, 600));
        transform().setScale(new Vector2(0.2, 0.2));


    }

    public void update() {
        handleMovement();
    }

    /**
     * Handle the movement of the paddle
     * Using Action defined in Action map to decide move direction
     */
    public void handleMovement() {
        switch (actionMap.currentAction) {
            case GoLeft -> transform().translate(new Vector2(-paddleSpeed * Time.deltaTime, 0));
            case GoRight -> transform().translate(new Vector2(paddleSpeed * Time.deltaTime, 0));
            //default -> System.out.println("Unknown Action");
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