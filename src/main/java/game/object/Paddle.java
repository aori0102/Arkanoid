package game.object;

import ecs.*;
import org.ActionMap;
import utils.Vector2;

public class Paddle extends MonoBehaviour {

    private SpriteRenderer sprite;
    private float paddleSpeed;
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
     * Initialize paddle specs
     */
    public void awake() {
        actionMap = gameObject.getComponent(ActionMap.class);
        sprite = gameObject.getComponent(SpriteRenderer.class);

        sprite.setImage("/paddle.png");
        transform().setPosition(new Vector2(600, 600));
        transform().setScale(new Vector2(0.2, 0.2));


    }

    public void update()
    {
        handleMovement();
    }

    public void handleMovement()
    {
        switch(actionMap.currentAction)
        {
            case GoLeft -> System.out.println("Go Left");
            case GoRight -> System.out.println("");
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