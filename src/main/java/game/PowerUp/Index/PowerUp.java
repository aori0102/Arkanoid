package game.PowerUp.Index;

import game.GameObject.Paddle;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Layer.Layer;
import org.Physics.BoxCollider;
import org.Rendering.SpriteRenderer;
import utils.Time;
import utils.Vector2;

/**
 * The power up for player, including Double, Triple balls, Shield, etc.
 */
public class PowerUp extends MonoBehaviour {

    protected static final double TRAVEL_SPEED = 100;

    private PowerUpIndex powerUpIndex = PowerUpIndex.None;
    protected boolean isMoving = true;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PowerUp(GameObject owner) {

        super(owner);
        var boxCollider = addComponent(BoxCollider.class);
        boxCollider.setLocalSize(new Vector2(100.0, 100.0));
        // TODO: WTF is this offset my guy - Aori to Kine
        boxCollider.getTransform().setLocalPosition(new Vector2(50, 50));
        boxCollider.isTrigger = true;
        boxCollider.setOnTriggerEnter(e -> {
            if (e.otherCollider.getComponent(Paddle.class) != null) {
                isMoving = false;
            }
        });

        gameObject.setLayer(Layer.PowerUp);

        addComponent(SpriteRenderer.class);

    }

    @Override
    protected void destroyComponent() {

    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        var clone = new PowerUp(newOwner);
        clone.powerUpIndex = powerUpIndex;
        return clone;
    }

    @Override
    public void update() {
        handleDroppingMotion();
    }

    private void handleDroppingMotion() {
        getTransform().translate(Vector2.down().multiply(TRAVEL_SPEED * Time.deltaTime));
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
                .setImage(powerUpIndex.getImageIndex().getImage());
    }

    /**
     * Called when this power up is being applied.
     */
    public void onApplied() {
        GameObjectManager.destroy(gameObject);
    }

}