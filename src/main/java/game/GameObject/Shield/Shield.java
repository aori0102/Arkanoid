package game.GameObject.Shield;

import game.Brick.Brick;
import org.Annotation.LinkViaPrefab;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Physics.BoxCollider;
import utils.Time;
import utils.Vector2;

import javax.management.Descriptor;

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

    private static final double SHIELD_DURATION = 7.0;

    private static Shield instance;

    @LinkViaPrefab
    private GameObject shieldVisual = null;

    private Time.CoroutineID turnOff_coroutineID = null;

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
    protected void onDestroy() {
        Time.removeCoroutine(turnOff_coroutineID);
    }

    public void turnOn() {
        gameObject.setActive(true);
        shieldVisual.setActive(true);

        if (Time.hasCoroutine(turnOff_coroutineID)) {
            Time.resetCoroutine(turnOff_coroutineID);
        } else {
            turnOff_coroutineID = Time.addCoroutine(this::turnOff, SHIELD_DURATION);
        }
    }

    /**
     * Deactivate the shield and reset timer.
     */
    public void turnOff() {
        gameObject.setActive(false);
        shieldVisual.setActive(false);
    }

    /**
     * Link the shield visual GameObject.
     * <p><b><i><u>NOTE</u>:</i></b> Only use during prefab linking process.</p>
     *
     * @param shieldVisual The visual GameObject representing the shield.
     */
    public void linkShieldVisual(GameObject shieldVisual) {
        this.shieldVisual = shieldVisual;
    }
}
