package game.GameObject;

import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Physics.BoxCollider;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.Time;
import utils.Vector2;

public class Shield extends MonoBehaviour {

    private static Shield instance;

    private boolean isExisted = false;
    private double counter = 0;
    private double duration = 0;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public Shield(GameObject owner) {
        super(owner);
        instance = this;
        addComponent(BoxCollider.class);
        addComponent(SpriteRenderer.class).setImage(ImageAsset.ImageIndex.PurpleBrick.getImage());
    }

    public static Shield getInstance() {
        return instance;
    }

    public void awake() {
        var collider = getComponent(BoxCollider.class);
        collider.setLocalSize(new Vector2(20000.0, 1.0));
        getTransform().setGlobalPosition(new Vector2(1190, 720));

        turnOff();
    }

    public void update() {
        if (isExisted) {
            handleShieldDuration(duration);
        }
    }

    private void handleShieldDuration(double duration) {
        counter += Time.deltaTime;

        if (counter >= duration) {
            turnOff();
        }
    }

    public void turnOn(double duration) {
        gameObject.setActive(true);
        isExisted = true;
        this.duration = duration;
    }

    public void turnOff() {
        gameObject.setActive(false);
        isExisted = false;
        counter = 0;
    }
}
