package game.Brick;

import game.Rank.ExperienceHolder;
import javafx.scene.paint.Color;
import org.Annotation.LinkViaPrefab;
import org.Audio.AudioManager;
import org.Audio.SFXAsset;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Physics.BoxCollider;
import org.Physics.CollisionData;
import utils.Vector2;

// TODO: Refactor

/**
 * Central class of a brick.
 */
public class Brick extends MonoBehaviour {

    public static final Vector2 BRICK_SIZE = new Vector2(64, 32);

    private final ExperienceHolder experienceHolder = addComponent(ExperienceHolder.class);
    private final BrickHealth brickHealth = addComponent(BrickHealth.class);
    private final BrickStat brickStat = addComponent(BrickStat.class);
    private final BrickEffectController brickEffectController = addComponent(BrickEffectController.class);

    @LinkViaPrefab
    private BrickVisual brickVisual = null;

    private BrickType brickType = BrickType.Normal;
    private boolean isJustDamaged = false;      // TODO: this can be removed

    public static EventHandler<OnBrickDestroyedEventArgs> onAnyBrickDestroyed = new EventHandler<>(Brick.class);

    public static class OnBrickDestroyedEventArgs {
        public Vector2 brickPosition;
        public BrickType brickType;
    }

    public static EventHandler<Void> onAnyBrickHit = new EventHandler<>(Brick.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public Brick(GameObject owner) {
        super(owner);
        addComponent(BoxCollider.class).setOnCollisionEnterCallback(this::onCollisionEnter);
    }

    @Override
    public void awake() {
        brickVisual.applyDefaultImage();
        experienceHolder.setExp(brickType.exp);
    }

    @Override
    public void onDestroy() {
        var onBrickDestroyedEventArgs = new OnBrickDestroyedEventArgs();
        onBrickDestroyedEventArgs.brickPosition = getTransform().getGlobalPosition();
        onBrickDestroyedEventArgs.brickType = brickType;
        onAnyBrickDestroyed.invoke(this, onBrickDestroyedEventArgs);
    }

    @Override
    public void lateUpdate() {
        isJustDamaged = false;
    }

    public BrickType getBrickType() {
        return brickType;
    }

    public BrickHealth getBrickHealth() {
        return brickHealth;
    }

    public BrickStat getBrickStat() {
        return brickStat;
    }

    public BrickVisual getBrickVisual() {
        return brickVisual;
    }

    public BrickEffectController getBrickEffectController() {
        return brickEffectController;
    }

    /**
     * Handles upon collision with other objects.
     *
     * @param data The information of the collision.
     */
    private void onCollisionEnter(CollisionData data) {
        onAnyBrickHit.invoke(this, null);
        isJustDamaged = true;
    }

    public void setBrickType(BrickType brickType) {
        this.brickType = brickType;
    }

    public boolean isJustDamaged() {
        return isJustDamaged;
    }

    public void setWaveIndex(int idx) {
        if (idx == -2) {
            return;
        }
        OLDBrickVisual.setBrightness(idx, brickVisual);
    }

    public void maxBrightness() {
        OLDBrickVisual.setBrightnessMax(brickVisual);
    }

    public void setRedRender() {
        brickVisual.setOverlayColor(Color.RED);
    }

    public void setYellowRender() {
        brickVisual.setOverlayColor(Color.YELLOW);
    }

    /**
     * Link the brick visual<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link BrickPrefab}
     * as part of component linking process.</i></b>
     *
     * @param brickVisual The brick visual.
     */
    public void linkBrickVisual(BrickVisual brickVisual) {
        this.brickVisual = brickVisual;
    }

}