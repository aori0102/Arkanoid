package game.Brick;

import game.PowerUp.Index.PowerUpManager;
import game.PowerUp.StatusEffect;
import game.Voltraxis.Interface.ITakePlayerDamage;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Layer.Layer;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.Time;
import utils.Vector2;

public class Brick extends MonoBehaviour implements ITakePlayerDamage {

    private static final double BURN_TIME = 3.0;
    private static final int BURN_EXISTED_TICKS = 1;
    private static final Vector2 BRICK_SIZE = new Vector2(64, 32);
    private static final int BASE_DAMAGE_MULTIPLIER = 1;

    private int health = 100;
    private BrickType brickType = BrickType.Normal;
    private double burnStartTime = 0.0;
    private int damageMultiplier = 1;

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
        setBrickType(BrickType.Normal);
        owner.setLayer(Layer.Brick);
    }

    @Override
    protected void onDestroy() {
        var onBrickDestroyedEventArgs = new OnBrickDestroyedEventArgs();
        onBrickDestroyedEventArgs.brickPosition = getTransform().getGlobalPosition();
        onBrickDestroyed.invoke(this, onBrickDestroyedEventArgs);
        PowerUpManager.getInstance().spawnPowerUp(getTransform().getGlobalPosition());

        Time.removeCoroutine(burnCoroutineID);

    }

    @Override
    public void takeDamage(int amount) {
        health -= damageMultiplier * amount;
        if (statusBrickEffect == StatusEffect.FrostBite) {
            resetStatusBrickEffect();
            return;
        }
        if (health <= 0) {
            GameObjectManager.destroy(gameObject);
        }
    }

    private void reduceHealth() {
        health -= 5;
        if (health <= 0) {
            GameObjectManager.destroy(gameObject);
        }
        if (Time.time < burnStartTime + BURN_TIME) {
            burnCoroutineID = Time.addCoroutine(this::reduceHealth, Time.time + BURN_EXISTED_TICKS);
        } else {
            resetStatusBrickEffect();
        }
    }

    public void setBrickType(BrickType brickType) {
        this.brickType = brickType;
        this.health = brickType.maxHealth;
    }

    public void setStatusBrickEffect(StatusEffect statusBrickEffect) {
        this.statusBrickEffect = statusBrickEffect;
        handleStatusEffect(statusBrickEffect);
        changeBrickVisual(statusBrickEffect);
        System.out.println(statusBrickEffect);
    }

    private void handleStatusEffect(StatusEffect statusEffect) {
        switch (statusEffect) {
            case Burn -> {
                burnCoroutineID = Time.addCoroutine(this::reduceHealth, Time.time + 2);
                burnStartTime = Time.time;
            }
            case FrostBite -> {
                damageMultiplier = 2;
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