package game.Voltraxis.Object.UltimateLaser;

import game.Entity.EntityDamageDealer;
import game.Entity.EntityHealth;
import game.Entity.EntityStat;
import game.Player.Paddle.PaddleHealth;
import org.GameObject.GameObject;
import utils.Time;

public final class UltimateLaserDamageDealer extends EntityDamageDealer {

    private static final double DAMAGE_DELAY = 0.33;

    private double lastDamageTick = 0.0;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public UltimateLaserDamageDealer(GameObject owner) {
        super(owner);
    }

    @Override
    protected void onDamageDealt() {
        lastDamageTick = Time.getTime();
    }

    @Override
    protected boolean canDealDamage() {
        return Time.getTime() > lastDamageTick + DAMAGE_DELAY;
    }

    @Override
    protected boolean isDamageTarget(EntityHealth entityHealth) {
        return entityHealth instanceof PaddleHealth;
    }

    @Override
    protected Class<? extends EntityStat> getStatComponentClass() {
        return UltimateLaserStat.class;
    }

}