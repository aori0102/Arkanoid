package game.Entity;

import game.Player.Paddle.PaddleHealth;
import game.Player.PlayerLives;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Physics.BoxCollider;
import org.Physics.PhysicsManager;
import utils.Random;

/**
 * Base class that represents entity's ability to deal damage.
 * <p>
 * This class automatically checks for
 * collision with any objects that contain {@link EntityHealth} and perform dealing damage on the
 * target.
 * </p>
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
        handleHealthManipulation();
    }

    /**
     * Handle collision with any objects with {@link EntityHealth} and deal damage accordingly.
     */
    private void handleHealthManipulation() {

        if (!canDealDamage()) {
            return;
        }

        var overlappedList = PhysicsManager.getOverlapColliders(boxCollider, true);
        for (var collider : overlappedList) {
            var healthObject = collider.getComponent(EntityHealth.class);
            if (healthObject != null && isDamageTarget(healthObject)) {

                boolean critical = Random.range(0.0, 1.0) < entityStat.getCriticalChange();
                EntityHealthAlterType healthAlteringType;
                if (isPlayerHealth(healthObject)) {
                    healthAlteringType = EntityHealthAlterType.PlayerTakeDamage;
                } else {
                    healthAlteringType
                            = critical ? EntityHealthAlterType.CriticalDamage : EntityHealthAlterType.NormalDamage;
                }

                var damage
                        = (int) (entityStat.getActualAttack() * (1 + (critical ? entityStat.getCriticalChange() : 0)));
                healthObject.alterHealth(healthAlteringType, damage);
                onDamageDealt();
                if (gameObject.isDestroyed()) {
                    return;
                }

            }
        }

    }

    private boolean isPlayerHealth(EntityHealth entityHealth) {
        return entityHealth instanceof PaddleHealth;
    }

    /**
     * Handles what happen after this object deals damage.
     */
    protected abstract void onDamageDealt();

    /**
     * Check whether this object can deal damage when called.
     *
     * @return {@code true} if this object can deal damage, otherwise {@code false}.
     */
    protected abstract boolean canDealDamage();

    /**
     * Check whether the given {@link EntityHealth} belongs to an object that this entity
     * can deal damage on.
     *
     * @param entityHealth The {@link EntityHealth} component of the object to check.
     * @return {@code true} if this object can deal damage on the given object,
     * *                                 otherwise {@code false}.
     */
    protected abstract boolean isDamageTarget(EntityHealth entityHealth);

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

}