package game.Obstacle;

import org.*;
import utils.Time;
import utils.Vector2;

public class Laser extends Obstacle {

    private final double laserSpeed = 1000;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public Laser(GameObject owner) {
        super(owner);
    }

    public void update() {
        handleMovement();
    }

    public void awake() {
        collider = getComponent(BoxCollider.class);
        collider.setLocalCenter(new Vector2(0, 0));
        collider.setLocalSize(new Vector2(30, 90));
        collider.setIncludeLayer(8);

        collider.setOnCollisionEnterCallback(collider -> {
            handleInteraction();
        });

    }

    @Override
    protected void handleMovement() {
        getTransform().translate(Vector2.up().multiply(laserSpeed * Time.deltaTime));
        if (getTransform().getGlobalPosition().x < 0) {
            GameObjectManager.destroy(gameObject);
        }
    }


}
