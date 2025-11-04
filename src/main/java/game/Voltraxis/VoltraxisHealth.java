package game.Voltraxis;

import game.Entity.EntityHealth;
import game.Entity.EntityStat;
import org.GameObject.GameObject;

public final class VoltraxisHealth extends EntityHealth {

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public VoltraxisHealth(GameObject owner) {
        super(owner);
    }

    @Override
    protected Class<? extends EntityStat> getStatComponentClass() {
        return VoltraxisStat.class;
    }

    @Override
    public int getMaxHealth() {
        return VoltraxisData.BASE_MAX_HEALTH;
    }

}