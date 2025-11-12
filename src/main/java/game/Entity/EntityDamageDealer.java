package game.Entity;

import game.Effect.StatusEffect;
import game.Effect.StatusEffectCombination;
import game.Effect.StatusEffectInfo;
import game.Player.Paddle.PaddleHealth;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Physics.BoxCollider;
import org.Physics.PhysicsManager;
import utils.Random;

/**
 * Base class that represents an entity's ability to deal damage.
 *
 * <p>
 * This class automatically checks for collision with any objects that contain
 * {@link EntityHealth} and performs damage dealing logic on the target.
 * </p>
 *
 * <p>
 * Do note that this class only handles the listed {@link EntityHealthAlterType}:
 * <ul>
 *     <li>{@link EntityHealthAlterType#NormalDamage}</li>
 *     <li>{@link EntityHealthAlterType#CriticalDamage}</li>
 *     <li>{@link EntityHealthAlterType#PlayerTakeDamage}</li>
 * </ul>
 * Any other {@link EntityHealthAlterType} has to be manually handled elsewhere.
 * </p>
 */
public abstract class EntityDamageDealer extends MonoBehaviour {

    private final EntityStat entityStat = addComponent(getStatComponentClass());
    private final BoxCollider boxCollider = addComponent(BoxCollider.class);

    private StatusEffectCombination currentStatusEffectCombination = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public EntityDamageDealer(GameObject owner) {
        super(owner);
    }

    @Override
    public void update() {

        if (!canDealDamage()) {
            return;
        }

        var overlappedList = PhysicsManager.getOverlapColliders(boxCollider, true);

        for (var collider : overlappedList) {

            var healthObject = collider.getComponent(EntityHealth.class);
            if (healthObject == null || !isDamageTarget(healthObject)) {
                continue;
            }

            // Inflict effect
            var effectControllingObject = collider.getComponent(EntityEffectController.class);
            if (effectControllingObject != null) {

                handleEffectInflicting(effectControllingObject);
                if (gameObject.isDestroyed()) {
                    return;
                }
                onEffectInflicted(effectControllingObject);

            }

            // Deal damage
            handleHealthManipulation(healthObject);
            if (gameObject.isDestroyed()) {
                return;
            }
            onDamageDealt(healthObject);

        }
    }

    /**
     * Handles inflicting status effects on a target entity when hit.
     *
     * <p>
     * If this entity has a {@link StatusEffectInfo}, it will attempt to apply
     * the corresponding status effect on the target. If the combination of effects
     * matches a defined {@link StatusEffectCombination}, it will trigger that reaction
     * instead of applying a single effect.
     * </p>
     *
     * @param effectController The target's {@link EntityEffectController}.
     */
    private void handleEffectInflicting(EntityEffectController effectController) {

        var statusInfo = getStatusEffectInfo();
        if (statusInfo != null && effectController != null) {

            // Handle effect
            if (!handleStatusCombination(statusInfo.effect, effectController)) {
                effectController.inflictEffect(statusInfo);
            }

        }

    }

    /**
     * Handles checking and triggering possible {@link StatusEffectCombination}
     * reactions when an entity with one effect is hit by another.
     *
     * <p>
     * This method looks for a valid combination where the incoming
     * {@code statusEffect} reacts with one that the target currently has.
     * If a combination is found, the target's previous effect is removed,
     * and the combination effect is stored for later damage modification.
     * </p>
     *
     * @param statusEffect     The status effect applied by the attacker.
     * @param effectController The target's {@link EntityEffectController}.
     * @return {@code true} if a valid combination was found and triggered,
     *         otherwise {@code false}.
     */
    private boolean handleStatusCombination(StatusEffect statusEffect, EntityEffectController effectController) {

        currentStatusEffectCombination = null;
        var combinationArray = StatusEffectCombination.values();
        for (var combination : combinationArray) {

            if (combination.hitEffect == statusEffect && effectController.removeEffect(combination.bearEffect)) {
                currentStatusEffectCombination = combination;
                break;
            }

        }

        return currentStatusEffectCombination != null;

    }

    /**
     * Handles applying health changes (damage) to a target entity when hit.
     *
     * <p>
     * This includes calculating the final damage amount, checking for critical hits,
     * applying damage multipliers from active {@link StatusEffectCombination},
     * and invoking {@link EntityHealth#alterHealth(EntityHealthAlterType, EntityHealthAlterType, int)}.
     * </p>
     *
     * @param healthObject The {@link EntityHealth} component of the target.
     */
    private void handleHealthManipulation(EntityHealth healthObject) {

        if (healthObject != null && isDamageTarget(healthObject)) {

            // Check for critical hit
            boolean critical = Random.range(0.0, 1.0) < entityStat.getCriticalChance();
            EntityHealthAlterType healthAlteringType;
            EntityHealthAlterType secondaryHealthAlteringType = null;
            if (isPlayerHealth(healthObject)) {
                healthAlteringType = EntityHealthAlterType.PlayerTakeDamage;
            } else {
                healthAlteringType
                        = critical ? EntityHealthAlterType.CriticalDamage : EntityHealthAlterType.NormalDamage;
            }

            // Calculate damage
            var damage = entityStat.getActualAttack();
            if (critical) {
                damage = (int) (damage * (1 + entityStat.getCriticalDamage()));
            }
            if (currentStatusEffectCombination != null) {
                damage = (int) (damage * currentStatusEffectCombination.damageMultiplier);
                healthAlteringType = currentStatusEffectCombination.hitEffect.damageType;
                secondaryHealthAlteringType = currentStatusEffectCombination.bearEffect.damageType;
                currentStatusEffectCombination = null;
            }
            healthObject.alterHealth(healthAlteringType, secondaryHealthAlteringType, damage);
            onDamageDealt(healthObject);

        }

    }

    private boolean isPlayerHealth(EntityHealth entityHealth) {
        return entityHealth instanceof PaddleHealth;
    }

    /**
     * Called whenever this entity successfully deals damage to another entity.
     *
     * <p>
     * Override this method to handle logic that should occur after a damage
     * event, such as spawning hit effects, increasing score, or applying knockback.
     * </p>
     *
     * @param entityHealth The {@link EntityHealth} component of the damaged entity.
     */
    protected abstract void onDamageDealt(EntityHealth entityHealth);

    /**
     * Determines whether this entity is currently able to deal damage.
     *
     * <p>
     * This is typically used to control when the entity should perform
     * collision-based damage checks — for instance, a projectile might
     * only deal damage once before being destroyed, or a melee attack
     * might only deal damage during a specific animation frame.
     * </p>
     *
     * @return {@code true} if this entity can deal damage in the current frame,
     *         otherwise {@code false}.
     */
    protected abstract boolean canDealDamage();

    /**
     * Determines whether the given {@link EntityHealth} belongs to a valid
     * damage target for this entity.
     *
     * <p>
     * This method allows filtering which entities can be damaged —
     * for example, preventing friendly fire or excluding inanimate objects.
     * </p>
     *
     * @param entityHealth The {@link EntityHealth} component of the potential target.
     * @return {@code true} if this entity can deal damage to the given target,
     *         otherwise {@code false}.
     */
    protected abstract boolean isDamageTarget(EntityHealth entityHealth);

    /**
     * Returns the corresponding {@link EntityStat} subclass that holds this
     * entity’s stats (e.g., attack power, critical rate, etc.).
     *
     * <p>
     * Each subclass of {@code EntityDamageDealer} must define its own
     * stat container by extending {@link EntityStat}, and return its class
     * type here so the base class can instantiate it automatically.
     * </p>
     *
     * @return The class type that extends {@link EntityStat} for this entity.
     */
    protected abstract Class<? extends EntityStat> getStatComponentClass();

    /**
     * Called immediately after this entity successfully inflicts a status effect
     * on a target through its {@link EntityEffectController}.
     *
     * <p>
     * This method can be overridden to implement additional logic such as
     * visual feedback, sound effects, or applying extra mechanics
     * (e.g., chaining reactions, triggering achievements, etc.).
     * </p>
     *
     * @param effectController The {@link EntityEffectController} of the target that received the effect.
     */
    protected abstract void onEffectInflicted(EntityEffectController effectController);

    /**
     * Retrieves the {@link StatusEffectInfo} that represents the type of
     * status effect this entity can inflict on hit.
     *
     * <p>
     * Override this method to define which effect should be applied when
     * this entity successfully hits a valid target. For example, a fireball
     * entity might return a {@code StatusEffectInfo} with {@link StatusEffect#Burn}.
     * </p>
     *
     * @return A {@link StatusEffectInfo} object describing the effect to be inflicted,
     *         or {@code null} if this entity does not apply any status effects.
     */
    protected abstract StatusEffectInfo getStatusEffectInfo();

}
