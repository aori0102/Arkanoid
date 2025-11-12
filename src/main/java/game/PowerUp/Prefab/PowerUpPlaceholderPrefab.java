package game.PowerUp.Prefab;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Layer.Layer;
import org.Physics.BoxCollider;
import org.Prefab.Prefab;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

/**
 * PowerUpPlaceholderPrefab is a generic prefab used as the base for all power-ups.
 * It sets up the GameObject with essential components such as:
 * - BoxCollider (for collision detection)
 * - SpriteRenderer (for visuals)
 *
 * This prefab can then be further customized for specific power-ups.
 */
public final class PowerUpPlaceholderPrefab extends Prefab {

    // Default size of the power-up
    private static final Vector2 POWER_UP_SIZE = new Vector2(65.0, 65.0);

    /**
     * Instantiate the PowerUp prefab.
     * - Adds BoxCollider and SpriteRenderer components.
     * - Configures the collider as a trigger.
     * - Sets the layer to PowerUp.
     *
     * @return The instantiated GameObject representing the power-up.
     */
    @Override
    public GameObject instantiatePrefab() {

        // Create the base GameObject with a collider and renderer
        var powerUpObject = GameObjectManager.instantiate("PowerUp")
                .addComponent(BoxCollider.class)
                .addComponent(SpriteRenderer.class)
                .getGameObject();

        // Set the layer to PowerUp for collision filtering
        powerUpObject.setLayer(Layer.PowerUp);

        // Configure the sprite renderer
        var spriteRenderer = powerUpObject.getComponent(SpriteRenderer.class);
        spriteRenderer.setPivot(new Vector2(0.5, 0.5));  // Center pivot
        spriteRenderer.setSize(POWER_UP_SIZE);           // Set size

        // Configure the box collider
        var boxCollider = powerUpObject.addComponent(BoxCollider.class);
        boxCollider.setLocalSize(POWER_UP_SIZE);  // Match the visual size
        boxCollider.setTrigger(true);             // Collider acts as a trigger

        return powerUpObject;

    }
}
