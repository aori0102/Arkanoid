package game.PowerUp.Index;

import game.Player.PlayerPaddle;
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
        boxCollider.setTrigger(true);
        boxCollider.setOnTriggerEnterCallback(e -> {
            if (e.otherCollider.getComponent(PlayerPaddle.class) != null) {
                isMoving = false;
            }
        });

        gameObject.setLayer(Layer.PowerUp);

        addComponent(SpriteRenderer.class).setPivot(new Vector2(0.5, 0.5));

    }

    @Override
    public void update() {
        handleDroppingMotion();
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
                .setImage(powerUpIndex.getImageIndex().getImage());
    }

    /**
     * Called when this power up is being applied.
     */
    public void onApplied() {
        GameObjectManager.destroy(gameObject);
    }

}