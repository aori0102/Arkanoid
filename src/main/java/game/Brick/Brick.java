package game.Brick;

import game.Damagable.DamageInfo;
import game.Damagable.DamageType;
import game.Effect.StatusEffect;
import game.Rank.ExperienceHolder;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Layer.Layer;
import org.Rendering.SpriteRenderer;
import org.Rendering.ImageAsset;
import utils.Time;
import utils.Vector2;

public class Brick extends MonoBehaviour {

    private static final double BURN_TIME = 3.0;
    private static final double FROSTBITE_TIME = 3.0;
    private static final int BURN_EXISTED_TICKS = 1;
    private static final Vector2 BRICK_SIZE = new Vector2(64, 32);
    private static final int BASE_DAMAGE_MULTIPLIER = 1;

    private final SpriteRenderer spriteRenderer = addComponent(SpriteRenderer.class);
    private final ExperienceHolder experienceHolder = addComponent(ExperienceHolder.class);

    private int health = 100;
    private BrickType brickType = BrickType.Normal;
    private double burnStartTime = 0.0;
    private double frostStartTime = 0.0;
    private int damageMultiplier = 1;
    private BrickDamageAcceptor brickDamageAcceptor = null;

    private Time.CoroutineID burnCoroutineID = null;

    public static EventHandler<OnBrickDestroyedEventArgs> onAnyBrickDestroyed = new EventHandler<>(Brick.class);

    public static class OnBrickDestroyedEventArgs {
        public Vector2 brickPosition;
        public BrickType brickType;
    }

    public StatusEffect statusBrickEffect = StatusEffect.None;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public Brick(GameObject owner) {
        super(owner);
        owner.setLayer(Layer.Brick);
    }

    public BrickType getBrickType() {
        return brickType;
    }

    @Override
    protected void onDestroy() {
        var onBrickDestroyedEventArgs = new OnBrickDestroyedEventArgs();
        onBrickDestroyedEventArgs.brickPosition = getTransform().getGlobalPosition();
        onBrickDestroyedEventArgs.brickType = brickType;
        onAnyBrickDestroyed.invoke(this, onBrickDestroyedEventArgs);

        Time.removeCoroutine(burnCoroutineID);
    }

    @Override
    public void awake() {
        brickDamageAcceptor = getComponent(BrickDamageAcceptor.class);
    }

    public void damage(int amount) {
        health -= damageMultiplier * amount;

        if (health <= 0) {
            GameObjectManager.destroy(gameObject);
        }
    }

    @Override
    public void update() {
        if (statusBrickEffect == StatusEffect.FrostBite) {
            if (Time.getTime() > frostStartTime + FROSTBITE_TIME) {
                resetStatusBrickEffect();
            }
        }
    }

    private void takeBurnDamage() {
        var damageInfo = new DamageInfo();
        damageInfo.type = DamageType.Burn;
        damageInfo.amount = 5;
        brickDamageAcceptor.takeDamage(damageInfo, null);
        if (gameObject.isDestroyed()) {
            return;
        }
        if (Time.getTime() < burnStartTime + BURN_TIME) {
            burnCoroutineID = Time.addCoroutine(this::takeBurnDamage, Time.getTime() + BURN_EXISTED_TICKS);
        } else {
            resetStatusBrickEffect();
        }
    }

    public void setBrickType(BrickType brickType) {
        this.brickType = brickType;
        this.health = brickType.maxHealth;
        spriteRenderer.setImage(brickType.imageIndex.getImage());
        spriteRenderer.setSize(BrickPrefab.BRICK_SIZE);
        experienceHolder.setExp(brickType.exp);
    }

    public void setStatusBrickEffect(StatusEffect statusBrickEffect) {
        this.statusBrickEffect = statusBrickEffect;
        handleStatusEffect(statusBrickEffect);
        changeBrickVisual(statusBrickEffect);
    }

    public void handleStatusEffect(StatusEffect statusEffect) {
        switch (statusEffect) {
            case Burn -> {
                burnCoroutineID = Time.addCoroutine(this::takeBurnDamage, Time.getTime() + 2);
                burnStartTime = Time.getTime();
            }
            case FrostBite -> {
                damageMultiplier = 2;
                frostStartTime = Time.getTime();
            }
        }
    }

    private void changeBrickVisual(StatusEffect statusEffect) {
        switch (statusEffect) {
            case Burn -> {
                SpriteRenderer renderer = getComponent(SpriteRenderer.class);
                renderer.setImage(ImageAsset.ImageIndex.OrangeBrick.getImage());
                renderer.setSize(BRICK_SIZE);
                renderer.setPivot(new Vector2(0.5, 0.5));
            }

            case FrostBite -> {
                SpriteRenderer renderer = getComponent(SpriteRenderer.class);
                renderer.setImage(ImageAsset.ImageIndex.PurpleBrick.getImage());
                renderer.setSize(BRICK_SIZE);
                renderer.setPivot(new Vector2(0.5, 0.5));
            }
        }
    }

    public void resetStatusBrickEffect() {
        this.statusBrickEffect = StatusEffect.None;
        damageMultiplier = BASE_DAMAGE_MULTIPLIER;
        SpriteRenderer renderer = getComponent(SpriteRenderer.class);
        renderer.setImage(ImageAsset.ImageIndex.GreenBrick.getImage());
        renderer.setSize(BRICK_SIZE);
        renderer.setPivot(new Vector2(0.5, 0.5));
    }

    public StatusEffect getStatusBrickEffect() {
        return statusBrickEffect;
    }

}