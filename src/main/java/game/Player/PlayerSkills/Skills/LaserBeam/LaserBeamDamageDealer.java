package game.Player.PlayerSkills.Skills.LaserBeam;

import game.Brick.BrickHealth;
import game.Effect.StatusEffectInfo;
import game.Entity.EntityDamageDealer;
import game.Entity.EntityEffectController;
import game.Entity.EntityHealth;
import game.Entity.EntityStat;
import game.Voltraxis.Object.PowerCore.PowerCoreHealth;
import game.Voltraxis.VoltraxisHealth;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;

public final class LaserBeamDamageDealer extends EntityDamageDealer {

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public LaserBeamDamageDealer(GameObject owner) {
        super(owner);
    }

    @Override
    protected void onDamageDealt(EntityHealth entityHealth) {
        GameObjectManager.destroy(gameObject);
    }

    @Override
    protected boolean canDealDamage() {
        return true;
    }

    @Override
    protected boolean isDamageTarget(EntityHealth entityHealth) {
        return entityHealth instanceof BrickHealth
                || entityHealth instanceof VoltraxisHealth
                || entityHealth instanceof PowerCoreHealth;
    }

    @Override
    protected Class<? extends EntityStat> getStatComponentClass() {
        return LaserBeamStat.class;
    }

    @Override
    protected void onEffectInflicted(EntityEffectController effectController) {
    }

    @Override
    protected StatusEffectInfo getStatusEffectInfo() {
        return null;
    }

}