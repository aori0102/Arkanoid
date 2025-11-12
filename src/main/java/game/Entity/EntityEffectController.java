package game.Entity;

import game.Effect.StatusEffect;
import game.Effect.StatusEffectInfo;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import utils.Time;

import java.util.EnumMap;
import java.util.List;

/**
 * Base class that manages all {@link StatusEffect} applied to an entity.
 *
 * <p>
 * Handles adding, updating, and removing status effects over time, including damage-over-time (DoT)
 * behavior. This class uses {@link Time} coroutines to automatically trigger effect expiration and
 * repeated damage ticks.
 * </p>
 *
 * <p>
 * Subclasses must define the behavior for what effects can be inflicted,
 * whether multiple effects can coexist, and which components represent
 * health and stats.
 * </p>
 */
public abstract class EntityEffectController extends MonoBehaviour {

    private final EnumMap<StatusEffect, StatusEffectInfo> statusMap = new EnumMap<>(StatusEffect.class);
    private final EnumMap<StatusEffect, Time.CoroutineID> removingCoroutineID = new EnumMap<>(StatusEffect.class);
    private final EnumMap<StatusEffect, Time.CoroutineID> inflictingCoroutineID = new EnumMap<>(StatusEffect.class);

    private EntityHealth entityHealth = null;
    private EntityStat entityStat = null;

    /** Event triggered when a new {@link StatusEffect} is applied. */
    public EventHandler<StatusEffect> onEffectInflicted = new EventHandler<>(EntityEffectController.class);

    /** Event triggered when a {@link StatusEffect} is removed or expires. */
    public EventHandler<StatusEffect> onEffectCleared = new EventHandler<>(EntityEffectController.class);

    /**
     * Creates this MonoBehaviour.
     *
     * @param owner The {@link GameObject} that owns this component.
     */
    public EntityEffectController(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        entityHealth = getComponent(getHealthComponentClass());
        entityStat = getComponent(getStatComponentClass());
    }

    @Override
    public void onDestroy() {
        removeAllEffect();
    }

    /**
     * Inflicts a {@link StatusEffect} on this entity with the given {@link StatusEffectInfo}.
     *
     * <p>
     * This method validates whether the effect can be applied, manages duration and DoT timing
     * through coroutines, and fires {@link #onEffectInflicted}.
     * </p>
     *
     * @param info The {@link StatusEffectInfo} describing the effect to apply.
     */
    public final void inflictEffect(StatusEffectInfo info) {

        if (info == null || !canBeInflictedWith(info.effect)) {
            return;
        }

        // Remove existing effects if only one can be held
        if (!canHoldMultipleEffect()) {
            removeAllEffect();
        }

        // Remove existing instance of the same effect
        if (statusMap.containsKey(info.effect)) {
            removeEffect(info.effect);
        }

        // Register effect and schedule removal + damage ticks
        statusMap.put(info.effect, info);
        removingCoroutineID.put(
                info.effect,
                Time.addCoroutine(() -> removeEffect(info.effect), Time.getTime() + info.duration)
        );
        inflictingCoroutineID.put(
                info.effect,
                Time.addCoroutine(() -> dealDamageOverTime(info.effect), Time.getTime() + info.effect.damageDelay)
        );

        onEffectInflicted.invoke(this, info.effect);
    }

    /**
     * Checks if this entity currently has any active {@link StatusEffect}.
     *
     * @return {@code true} if at least one effect is active, otherwise {@code false}.
     */
    public final boolean hasAnyEffect() {
        return !statusMap.isEmpty();
    }

    /**
     * Removes a specific {@link StatusEffect} from this entity.
     *
     * <p>
     * Cancels all scheduled coroutines related to the effect, clears its data from maps,
     * and triggers {@link #onEffectCleared} if it was present.
     * </p>
     *
     * @param effect The effect to remove.
     * @return {@code true} if the effect was successfully removed, otherwise {@code false}.
     */
    public final boolean removeEffect(StatusEffect effect) {
        var statusInfo = statusMap.remove(effect);
        Time.removeCoroutine(removingCoroutineID.remove(effect));
        Time.removeCoroutine(inflictingCoroutineID.remove(effect));
        if (statusInfo != null) {
            onEffectCleared.invoke(this, effect);
        }
        return statusInfo != null;
    }

    /**
     * Deals damage over time (DoT) for a given active {@link StatusEffect}.
     *
     * <p>
     * This method applies damage periodically according to the effect’s base damage
     * and damage delay. It automatically reschedules itself until the effect expires.
     * </p>
     *
     * @param effect The active status effect to process.
     */
    private void dealDamageOverTime(StatusEffect effect) {
        if (entityHealth == null) {
            return;
        }
        var damage = (int) (entityStat.getDamageTakenMultiplier() * effect.baseDamageOverTime);
        entityHealth.alterHealth(effect.damageType, null, damage);
        if (gameObject.isDestroyed()) {
            return;
        }
        inflictingCoroutineID.put(
                effect,
                Time.addCoroutine(() -> dealDamageOverTime(effect), Time.getTime() + effect.damageDelay)
        );
    }

    /**
     * Removes all active {@link StatusEffect} instances from this entity.
     */
    public final void removeAllEffect() {
        var effectList = getEffectList();
        for (var effect : effectList) {
            removeEffect(effect);
        }
    }

    /**
     * Returns a list of all currently active {@link StatusEffect}.
     *
     * @return A list of active status effects.
     */
    public final List<StatusEffect> getEffectList() {
        return statusMap.keySet().stream().toList();
    }

    /**
     * Determines whether this entity can be inflicted with a given {@link StatusEffect}.
     *
     * <p>
     * Override this method to define restrictions — for example, certain entities
     * might be immune to specific elemental or crowd-control effects.
     * </p>
     *
     * @param effect The {@link StatusEffect} to test.
     * @return {@code true} if the effect can be applied, otherwise {@code false}.
     */
    protected abstract boolean canBeInflictedWith(StatusEffect effect);

    /**
     * Determines whether this entity can hold multiple status effects at once.
     *
     * <p>
     * If this method returns {@code false}, any new effect will remove all
     * existing effects before being applied.
     * </p>
     *
     * @return {@code true} if multiple effects can coexist, otherwise {@code false}.
     */
    protected abstract boolean canHoldMultipleEffect();

    /**
     * Returns the class type of the {@link EntityHealth} component
     * used by this entity.
     *
     * @return The class of the health component.
     */
    protected abstract Class<? extends EntityHealth> getHealthComponentClass();

    /**
     * Returns the class type of the {@link EntityStat} component
     * used by this entity.
     *
     * @return The class of the stat component.
     */
    protected abstract Class<? extends EntityStat> getStatComponentClass();

}
