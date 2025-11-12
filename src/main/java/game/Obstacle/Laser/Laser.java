package game.Obstacle.Laser;

import game.Entity.EntityEffectController;
import game.Obstacle.Index.Obstacle;
import org.GameObject.GameObject;
import org.Layer.Layer;
import org.Physics.BoxCollider;
import utils.Time;
import utils.Vector2;

/**
 * Represents a Laser obstacle in the game.
 * <p>
 * A Laser is a vertical moving obstacle that travels downwards
 * and can interact with the player's paddle. It uses a BoxCollider
 * as a trigger to detect collisions without physically blocking movement.
 * </p>
 */
public class Laser extends Obstacle {

    /** Speed at which the laser moves downwards (units per second). */
    private static final double LASER_SPEED = 1000;

    /**
     * Create this Laser obstacle.
     *
     * @param owner The owner GameObject of this component.
     */
    public Laser(GameObject owner) {
        super(owner);
    }

    /**
     * Awake is called when the Laser is instantiated.
     * <p>
     * Sets up the collider size, position, collision layer, and trigger behavior.
     * </p>
     */
    @Override
    public void awake() {
        collider = getComponent(BoxCollider.class);
        collider.setLocalCenter(new Vector2(0, 0));
        collider.setLocalSize(new Vector2(30, 90));
        collider.setIncludeLayer(Layer.Paddle.getUnderlyingValue());
        collider.setTrigger(true);
        collider.setOnTriggerEnterCallback(this::handleInteraction);
    }

    /**
     * Called every frame.
     * <p>
     * Updates the obstacle logic and moves the laser.
     * </p>
     */
    @Override
    public void update() {
        super.update();
        handleMovement();
    }

    /**
     * Moves the laser downwards at a fixed speed.
     */
    @Override
    protected void handleMovement() {
        getTransform().translate(Vector2.down().multiply(LASER_SPEED * Time.getDeltaTime()));
    }
}
