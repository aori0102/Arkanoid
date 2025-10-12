package game.PowerUp;

import game.object.Paddle;
import org.*;
import utils.Time;
import utils.Vector2;

/**
 * The power up for player, including Double, Triple balls, Shield, etc.
 */
public class PowerUp extends MonoBehaviour {

    protected static final double TRAVEL_SPEED = 100;

    private PowerUpIndex powerUpIndex = PowerUpIndex.None;
    protected boolean isMoving = true;
    protected boolean shouldBeDestroyed = false;

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        var clone = new PowerUp(newOwner);
        clone.powerUpIndex = powerUpIndex;
        return clone;
    }

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PowerUp(GameObject owner) {

        super(owner);
        var boxCollider = addComponent(BoxCollider.class);
        boxCollider.setLocalSize(new Vector2(100.0, 100.0));
        boxCollider.isTrigger = true;
        boxCollider.setOnTriggerEnter(e ->{
            if (e.otherCollider.getComponent(Paddle.class) != null) {
                isMoving = false;
                shouldBeDestroyed = true;
            }
        });

        gameObject.setLayer(Layer.PowerUp);

        addComponent(SpriteRenderer.class);

    }

    @Override
    protected void destroyComponent() {

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
                .setImage(PowerUpData.POWER_UP_IMAGE_PATH_MAP.get(powerUpIndex).getImage());
    }

    /**
     * Called when this power up is being applied.
     */
    public void onApplied() {
        shouldBeDestroyed = true;
    }

}