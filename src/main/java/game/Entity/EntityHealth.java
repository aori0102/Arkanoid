package game.Entity;

import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import utils.Vector2;

/**
 * Base class that holds information about an entity's health, as well as functions to allow
 * increasing or decreasing this entity's health.
 */
public abstract class EntityHealth extends MonoBehaviour {

    /**
     * <b>Only write via {@link #alterHealth}</b>
     */
    private int _health = 0;

    private final EntityStat entityStat = addComponent(getStatComponentClass());

    public EventHandler<OnHealthChangedEventArgs> onHealthChanged = new EventHandler<>(EntityHealth.class);

    public static class OnHealthChangedEventArgs {
        /**
         * The type of the HP's change. See more {@link EntityHealthAlterType}.
         */
        public EntityHealthAlterType alterType;
        /**
         * The second type of HP's change, used to combine with {@link #alterType} in case
         * of combines damage/healing input.
         */
        public EntityHealthAlterType secondaryAlterType;
        /**
         * The current HP after change.
         */
        public int health;
        /**
         * The absolute difference between the previous and current HP.
         */
        public int delta;
        /**
         * The amount that was sent to be altered without clamping;
         */
        public int alterAmount;
        /**
         * The global position of the object that has these changes.
         */
        public Vector2 position;
    }

    public EventHandler<Void> onHealthReachesZero = new EventHandler<>(EntityHealth.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public EntityHealth(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        _health = entityStat.getMaxHealth();
    }

    /**
     * Setter for read-only field {@link #_health}
     *
     * @param alterType          The health altering type.
     * @param secondaryAlterType The secondary health altering type, can be {@code null} if
     *                           not combining two damaging/healing input.
     * @param amount             The value to set.
     */
    public final void alterHealth(
            EntityHealthAlterType alterType,
            EntityHealthAlterType secondaryAlterType,
            int amount) {

        var previous = _health;

        if (isAlterTypeHealing(alterType)) {
            var actualHealAmount = (int) (amount * entityStat.getRegenerationMultiplier());
            _health += actualHealAmount;
        } else {
            var actualDamage = Math.max(0, amount - entityStat.getActualDefense());
            _health -= actualDamage;
        }

        _health = Math.clamp(_health, 0, entityStat.getMaxHealth());

        var onHealthChangedEventArgs = new OnHealthChangedEventArgs();
        onHealthChangedEventArgs.delta = Math.abs(_health - previous);
        onHealthChangedEventArgs.alterType = alterType;
        onHealthChangedEventArgs.alterAmount = amount;
        onHealthChangedEventArgs.secondaryAlterType = secondaryAlterType;
        onHealthChangedEventArgs.health = _health;
        onHealthChangedEventArgs.position = getTransform().getGlobalPosition();
        onHealthChanged.invoke(this, onHealthChangedEventArgs);

        if (_health == 0) {
            onHealthReachesZero.invoke(this, null);
        }

    }

    /**
     * Get this entity's current health point.
     *
     * @return this entity's current health point.
     */
    public final int getHealth() {
        return _health;
    }

    /**
     * Check if the given {@link EntityHealthAlterType} is a healing type, which means
     * {@link EntityHealthAlterType#Regeneration}.
     *
     * @param alterType The health alter type to check for.
     * @return {@code true} if the alter type is for healing, otherwise {@code false}.
     */
    private boolean isAlterTypeHealing(EntityHealthAlterType alterType) {
        return alterType == EntityHealthAlterType.Regeneration;
    }

    /**
     * Get the corresponding class stat holder class of this entity.
     * <p>
     * <b><i><u>NOTE</u> : Every class that inherits from this must create a corresponding class for
     * holding stat which inherits from {@link EntityStat}.</i></b>
     * </p>
     *
     * @return The class signature of the stat holding class.
     */
    protected abstract Class<? extends EntityStat> getStatComponentClass();

    public final void resetHealth() {
        _health = entityStat.getMaxHealth();
    }

}