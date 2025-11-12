package game.GameObject.Shield;

import game.Player.Player;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Physics.BoxCollider;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.Time;
import utils.Vector2;

/**
 * Singleton class that represents the player's shield.
 * <p>
 * Responsibilities:
 * <ul>
 *     <li>Activate/deactivate the shield.</li>
 *     <li>Track shield duration.</li>
 *     <li>Link shield visual.</li>
 * </ul>
 * </p>
 */
public class Shield extends MonoBehaviour {

    private static final double DURATION = 5.0; // Shield active duration in seconds
    private static Shield instance; // Singleton instance

    private boolean isExisted = false; // Whether shield is currently active
    private double counter = 0; // Timer for shield duration
    private GameObject shieldVisual; // Visual representation of shield

    /**
     * Constructor.
     *
     * @param owner The owner GameObject of this MonoBehaviour.
     */
    public Shield(GameObject owner) {
        super(owner);
        instance = this;
        addComponent(BoxCollider.class);
    }

    /**
     * Get the singleton instance.
     *
     * @return Shield instance.
     */
    public static Shield getInstance() {
        return instance;
    }

    @Override
    public void awake() {
        var collider = getComponent(BoxCollider.class);
        collider.setLocalSize(new Vector2(20000.0, 1.0));
        getTransform().setGlobalPosition(new Vector2(1190, 720));

        turnOff(); // Ensure shield starts inactive
    }

    @Override
    public void update() {
        if (isExisted) {
            handleShieldDuration(DURATION);
        }
    }

    /**
     * Handle shield duration and automatically turn off when expired.
     *
     * @param duration Duration of shield in seconds.
     */
    private void handleShieldDuration(double duration) {
        if (!Player.getInstance().getPlayerPaddle().canBeDamaged()) {
            return;
        }
        counter += Time.getDeltaTime();

        if (counter >= duration) {
            turnOff();
        }
    }

    /**
     * Activate the shield.
     */
    public void turnOn() {
        gameObject.setActive(true);
        shieldVisual.setActive(true);
        isExisted = true;
    }

    /**
     * Deactivate the shield and reset timer.
     */
    public void turnOff() {
        gameObject.setActive(false);
        shieldVisual.setActive(false);
        isExisted = false;
        counter = 0;
    }

    /**
     * Link the shield visual GameObject.
     * <p><b><i><u>NOTE</u>:</i></b> Only use during prefab linking process.</p>
     *
     * @param shieldVisual The visual GameObject representing the shield.
     */
    public void linkSpriteRenderer(GameObject shieldVisual) {
        this.shieldVisual = shieldVisual;
    }
}
