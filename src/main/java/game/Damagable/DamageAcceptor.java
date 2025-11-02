package game.Damagable;

import game.Effect.StatusEffect;
import game.Effect.StatusEffectInfo;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Physics.BoxCollider;
import org.Physics.PhysicsManager;
import utils.Time;

public abstract class DamageAcceptor extends MonoBehaviour {

    private static final double DAMAGE_TAKEN_DELAY = 0.47;

    private double previousDamagedTick = 0.0;
    private BoxCollider boxCollider = null;

    public EventHandler<OnDamageTakenEventArgs> onDamageTaken = new EventHandler<>(DamageAcceptor.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public DamageAcceptor(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        boxCollider = getComponent(BoxCollider.class);
        if (boxCollider == null || boxCollider.isTrigger()) {
            throw new IllegalStateException("Object with DamageAcceptor must have a non-trigger BoxCollider");
        }

        var damageVisualizer = DamageVisualizerPrefab.instantiatePrefab();
        damageVisualizer.linkDamageAcceptor(this);

    }

    @Override
    public void update() {

        var overlappedList = PhysicsManager.getOverlapColliders(boxCollider, true);
        for (var collider : overlappedList) {
            var damageObject = collider.getComponent(ICanDealDamage.class);
            if (damageObject != null && damageObject.isDamageTarget(this)) {
                if (Time.getTime() > previousDamagedTick + DAMAGE_TAKEN_DELAY) {

                    var damageInfo = damageObject.getDamageInfo();
                    if (damageInfo != null) {

                        // Apply onDamaged action
                        damageObject.onDamaged();

                        // Apply damage
                        takeDamage(damageInfo); // Potentially kills object

                        if (gameObject.isDestroyed()) {
                            return;
                        }
                        previousDamagedTick = Time.getTime();
                    }

                    // Apply effect
                    var effectInfo = damageObject.getEffect();
                    if (effectInfo != null) {
                        applyEffect(effectInfo);
                        if (gameObject.isDestroyed()) {
                            return;
                        }
                    }

                }
            }
        }

    }

    protected void takeDamage(DamageInfo damageInfo) {
        if (gameObject.isDestroyed()) {
            return;
        }
        // Fire damaged event
        var onDamageTakenEventArgs = new OnDamageTakenEventArgs();
        onDamageTakenEventArgs.damageInfo = damageInfo;
        onDamageTakenEventArgs.position = getTransform().getGlobalPosition();
        onDamageTaken.invoke(this, onDamageTakenEventArgs);
    }

    protected abstract void applyEffect(StatusEffect effect);

}