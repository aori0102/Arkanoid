package game.GameObject.Shield;

import org.Annotation.LinkViaPrefab;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Physics.BoxCollider;
import utils.Time;
import utils.Vector2;

// TODO: Doc + Prefab
public class Shield extends MonoBehaviour {

    private static final double SHIELD_DURATION = 7.0;

    private static Shield instance;

    @LinkViaPrefab
    private GameObject shieldVisual = null;

    private Time.CoroutineID turnOff_coroutineID = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
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

        turnOff();
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

    public void turnOff() {
        gameObject.setActive(false);
        shieldVisual.setActive(false);
    }

    /**
     * Link this shield's visual<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link }
     * as part of component linking process.</i></b>
     *
     * @param shieldVisual The visual of this shield.
     */
    public void linkShieldVisual(GameObject shieldVisual) {
        this.shieldVisual = shieldVisual;
    }
}
