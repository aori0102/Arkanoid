package game.Brick;

import game.Damagable.DamageInfo;
import game.Damagable.DamageType;
import game.Effect.StatusEffect;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Layer.Layer;
import org.Rendering.SpriteRenderer;
import org.Rendering.ImageAsset;
import utils.Time;
import utils.Vector2;

import static game.Brick.BrickVisual.setBrightness;

public class Brick extends MonoBehaviour {

    private static final double BURN_TIME = 3.0;
    private static final double FROSTBITE_TIME = 3.0;
    private static final int BURN_EXISTED_TICKS = 1;
    private static final Vector2 BRICK_SIZE = new Vector2(64, 32);
    private static final int BASE_DAMAGE_MULTIPLIER = 1;

    private int health = 100;
    private BrickType brickType = BrickType.Normal;
    private SpriteRenderer spriteRenderer = null;
    private double burnStartTime = 0.0;
    private double frostStartTime = 0.0;
    private int damageMultiplier = 1;
    private BrickDamageAcceptor brickDamageAcceptor = null;
    private boolean isJustDamaged = false;

    public EventHandler<OnBrickDestroyedEventArgs> onBrickDestroyed = new EventHandler<>(Brick.class);
    private Time.CoroutineID burnCoroutineID = null;

    public static class OnBrickDestroyedEventArgs {
        public Vector2 brickPosition;
    }

    public StatusEffect statusBrickEffect = StatusEffect.None;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public Brick(GameObject owner) {
        super(owner);
        spriteRenderer = addComponent(SpriteRenderer.class);
        owner.setLayer(Layer.Brick);

    }

    public BrickType getBrickType() {
        return brickType;
    }

    @Override
    protected void onDestroy() {
        var onBrickDestroyedEventArgs = new OnBrickDestroyedEventArgs();
        onBrickDestroyedEventArgs.brickPosition = getTransform().getGlobalPosition();
        onBrickDestroyed.invoke(this, onBrickDestroyedEventArgs);

        Time.removeCoroutine(burnCoroutineID);
    }

    @Override
    public void awake() {
        brickDamageAcceptor = getComponent(BrickDamageAcceptor.class);
    }

    public void damage(int amount) {
        health -= damageMultiplier * amount;
        isJustDamaged = true;

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
        brickDamageAcceptor.takeDamage(damageInfo);
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
        renderer.setImage(ImageAsset.ImageIndex.BrickNormal.getImage());
        renderer.setSize(BRICK_SIZE);
        renderer.setPivot(new Vector2(0.5, 0.5));
    }

    public StatusEffect getStatusBrickEffect() {
        return statusBrickEffect;
    }

    public int getHealth() {
        return health;
    }

    public void resetJustDamaged() {
        isJustDamaged = false;
    }

    public boolean isJustDamaged() {
        return isJustDamaged;
    }

    public void setWaveIndex(int idx) {
        if (idx == -2) {
            return;
        }
        setBrightness(idx, this);
    }

}