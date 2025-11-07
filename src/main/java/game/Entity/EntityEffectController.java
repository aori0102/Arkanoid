package game.Entity;

import game.Effect.StatusEffect;
import game.Effect.StatusEffectInfo;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import utils.Time;

import java.util.EnumMap;
import java.util.List;

public abstract class EntityEffectController extends MonoBehaviour {

    private final EnumMap<StatusEffect, StatusEffectInfo> statusMap = new EnumMap<>(StatusEffect.class);
    private final EnumMap<StatusEffect, Time.CoroutineID> removingCoroutineID = new EnumMap<>(StatusEffect.class);
    private final EnumMap<StatusEffect, Time.CoroutineID> inflictingCoroutineID = new EnumMap<>(StatusEffect.class);

    private EntityHealth entityHealth = null;
    private EntityStat entityStat = null;

    public EventHandler<StatusEffect> onEffectInflicted = new EventHandler<>(EntityEffectController.class);
    public EventHandler<StatusEffect> onEffectCleared = new EventHandler<>(EntityEffectController.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
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

    public final void inflictEffect(StatusEffectInfo info) {

        if (info == null || !canBeInflictedWith(info.effect)) {
            return;
        }

        if (!canHoldMultipleEffect()) {
            removeAllEffect();
        }

        if (statusMap.containsKey(info.effect)) {
            removeEffect(info.effect);
        }

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

    public final boolean hasAnyEffect() {
        return !statusMap.isEmpty();
    }

    public final boolean removeEffect(StatusEffect effect) {
        var statusInfo = statusMap.remove(effect);
        Time.removeCoroutine(removingCoroutineID.remove(effect));
        Time.removeCoroutine(inflictingCoroutineID.remove(effect));
        if (statusInfo != null) {
            onEffectCleared.invoke(this, effect);
        }
        return statusInfo != null;
    }

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

    public final void removeAllEffect() {
        var effectList = getEffectList();
        for (var effect : effectList) {
            removeEffect(effect);
        }
    }

    public final List<StatusEffect> getEffectList() {
        return statusMap.keySet().stream().toList();
    }

    protected abstract boolean canBeInflictedWith(StatusEffect effect);

    protected abstract boolean canHoldMultipleEffect();

    protected abstract Class<? extends EntityHealth> getHealthComponentClass();

    protected abstract Class<? extends EntityStat> getStatComponentClass();

}