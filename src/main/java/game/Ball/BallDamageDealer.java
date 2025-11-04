package game.Ball;

import game.Brick.BrickHealth;
import game.Effect.StatusEffectInfo;
import game.Entity.EntityDamageDealer;
import game.Entity.EntityEffectController;
import game.Entity.EntityHealth;
import game.Entity.EntityStat;
import game.Voltraxis.Object.PowerCore.PowerCoreHealth;
import game.Voltraxis.VoltraxisHealth;
import org.GameObject.GameObject;

public final class BallDamageDealer extends EntityDamageDealer {

    private Ball ball = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public BallDamageDealer(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        ball = getComponent(Ball.class);
    }

    @Override
    protected void onDamageDealt(EntityHealth entityHealth) {
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
        return BallStat.class;
    }

    @Override
    protected void onEffectInflicted(EntityEffectController effectController) {
        ball.getBallEffectController().removeAllEffect();
    }

    @Override
    protected StatusEffectInfo getStatusEffectInfo() {
        var effectList = ball.getBallEffectController().getEffectList();
        if (!effectList.isEmpty()) {
            var statusInfo = new StatusEffectInfo();
            statusInfo.effect = effectList.getFirst();
            statusInfo.duration = BallEffectController.EFFECT_INFLICTED_TIME;
            return statusInfo;
        }
        return null;
    }

}