package game.PowerUp.Index;

import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Layer.Layer;
import org.Rendering.SpriteRenderer;
import utils.Time;
import utils.Vector2;

/**
 * The power up for player, including Double, Triple balls, Shield, etc.
 */
public class PowerUp extends MonoBehaviour {

    protected static final double TRAVEL_SPEED = 100;

    private PowerUpIndex powerUpIndex = null;

    public static EventHandler<Void> onAnyPowerUpDestroyed = new EventHandler<>(PowerUp.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PowerUp(GameObject owner) {

        super(owner);

    }

    @Override
    public void update() {
        handleDroppingMotion();
    }

    @Override
    public void onDestroy() {
        onAnyPowerUpDestroyed.invoke(this, null);
    }

    private void handleDroppingMotion() {
        getTransform().translate(Vector2.down().multiply(TRAVEL_SPEED * Time.getDeltaTime()));
    }

    /**
     * Get the index of this power up.
     *
     * @return The index of this power up.
     */
    public PowerUpIndex getPowerUpIndex() {
        return powerUpIndex;
    }

    /**
     * Set the index for this power up, also update
     * this power up's visual. This should
     * only being called upon initialization.
     *
     * @param powerUpIndex The index for this power up.
     */
    public void setPowerUpIndex(PowerUpIndex powerUpIndex) {
        this.powerUpIndex = powerUpIndex;
        getComponent(SpriteRenderer.class)
                .setImage(powerUpIndex.imageIndex.getImage());
    }

    /**
     * Called when this power up is being applied.
     */
    public void onApplied() {
        GameObjectManager.destroy(gameObject);
    }

}