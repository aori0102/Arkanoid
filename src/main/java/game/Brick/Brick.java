package game.Brick;

import game.Effect.StatusEffect;
import game.Rank.ExperienceHolder;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Physics.BoxCollider;
import org.Physics.CollisionData;
import org.Rendering.SpriteRenderer;
import org.Rendering.ImageAsset;
import utils.Vector2;

// TODO: Refactor
public class Brick extends MonoBehaviour {

    private static final Vector2 BRICK_SIZE = new Vector2(64, 32);

    private final SpriteRenderer spriteRenderer = addComponent(SpriteRenderer.class);
    private final ExperienceHolder experienceHolder = addComponent(ExperienceHolder.class);
    private final BrickHealth brickHealth = addComponent(BrickHealth.class);
    private final BrickStat brickStat = addComponent(BrickStat.class);
    private final BrickEffectController brickEffectController = addComponent(BrickEffectController.class);

    private BrickType brickType = BrickType.Normal;
    private boolean isJustDamaged = false;

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
        spriteRenderer.setImage(brickType.imageIndex.getImage());
        spriteRenderer.setSize(BrickPrefab.BRICK_SIZE);
        System.out.println("awake " + gameObject);

        experienceHolder.setExp(brickType.exp);
    }

    @Override
    public void start() {
        brickEffectController.onEffectInflicted.addListener(this::brickEffectController_onEffectInflicted);
        brickEffectController.onEffectCleared.addListener(this::brickEffectController_onEffectCleared);
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

    private void onCollisionEnter(CollisionData data) {
        onAnyBrickHit.invoke(this, null);
        isJustDamaged = true;
    }

    /**
     * Called when {@link BrickEffectController#onEffectInflicted} is invoked.<br><br>
     * This function changes the visual of the brick based on the effect inflicted.
     *
     * @param sender Event caller {@link BrickEffectController}.
     * @param e      Event argument indicating the effect that was inflicted.
     */
    private void brickEffectController_onEffectInflicted(Object sender, StatusEffect e) {
        spriteRenderer.setImage(switch (e) {
            case Burn -> ImageAsset.ImageIndex.OrangeBrick.getImage();
            case FrostBite -> ImageAsset.ImageIndex.CyanBrick.getImage();
            case Electrified -> ImageAsset.ImageIndex.PurpleBrick.getImage();
            default -> ImageAsset.ImageIndex.CyanBrick.getImage();
        });
        spriteRenderer.setSize(BRICK_SIZE);
    }

    /**
     * Called when {@link BrickEffectController#onEffectCleared} is invoked.<br><br>
     * This function changes the visual of the brick back to normal as the effect is removed.
     *
     * @param sender Event callers {@link BrickEffectController}.
     * @param e      Event argument indicating the effect that was removed.
     */
    private void brickEffectController_onEffectCleared(Object sender, StatusEffect e) {
        if (!brickEffectController.hasAnyEffect()) {
            spriteRenderer.setImage(brickType.imageIndex.getImage());
            spriteRenderer.setSize(BRICK_SIZE);
        }
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
        BrickVisual.setBrightness(idx, this);
    }

    public void maxBrightness() {
        BrickVisual.setBrightnessMax(this);
    }

    public void setRedRender() {
        BrickVisual.setRed(this);
    }

    public void setYellowRender() {
        BrickVisual.setYellow(this);
    }

    public void resetRenderColor() {
        BrickVisual.resetRender(this);
    }

}