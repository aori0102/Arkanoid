package game.Voltraxis.Object.PowerCore;

import game.Entity.EntityHealth;
import game.Entity.EntityStat;
import game.Voltraxis.Voltraxis;
import game.Voltraxis.VoltraxisData;
import org.Event.EventHandler;
import org.GameObject.GameObject;

public final class PowerCoreHealth extends EntityHealth {

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PowerCoreHealth(GameObject owner) {
        super(owner);
    }

    @Override
    protected Class<? extends EntityStat> getStatComponentClass() {
        return PowerCoreStat.class;
    }

    /**
     * Get the power core's max HP.
     *
     * @return The power core's max HP.
     */
    @Override
    public int getMaxHealth() {
        return (int) (VoltraxisData.BASE_MAX_HEALTH * VoltraxisData.POWER_CORE_PROPORTIONAL_HEALTH);
    }

}