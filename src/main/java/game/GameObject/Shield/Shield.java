package game.GameObject.Shield;

import game.Player.Player;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Physics.BoxCollider;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.Time;
import utils.Vector2;

// TODO: Doc + Prefab
public class Shield extends MonoBehaviour {

    private static final double DURATION = 5.0;
    private static Shield instance;

    private boolean isExisted = false;
    private double counter = 0;
    private GameObject shieldVisual;

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

    public void update() {
        if (isExisted) {
            handleShieldDuration(DURATION);
        }
    }

    private void handleShieldDuration(double duration) {
        if (!Player.getInstance().getPlayerPaddle().canBeDamaged()) {
            return;
        }
        counter += Time.getDeltaTime();

        if (counter >= duration) {
            turnOff();
        }
    }

    public void turnOn() {
        gameObject.setActive(true);
        shieldVisual.setActive(true);
        isExisted = true;
    }

    public void turnOff() {
        gameObject.setActive(false);
        shieldVisual.setActive(false);
        isExisted = false;
        counter = 0;
    }

    /**
     * <br><br>
     * <b><i><u>NOTE</u> : Only use within {@link }
     * as part of component linking process.</i></b>
     *
     * @param shieldVisual .
     */
    public void linkSpriteRenderer(GameObject shieldVisual) {
        this.shieldVisual = shieldVisual;
    }
}
