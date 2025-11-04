package game.Voltraxis.Object.ElectricBall;

import game.Effect.StatusEffect;
import game.Effect.StatusEffectInfo;
import game.Entity.EntityDamageDealer;
import game.Entity.EntityEffectController;
import game.Entity.EntityHealth;
import game.Entity.EntityStat;
import game.Player.Paddle.PaddleHealth;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;

public final class ElectricBallDamageDealer extends EntityDamageDealer {

    private static final double ELECTRIFIED_TIME = 4.3;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public ElectricBallDamageDealer(GameObject owner) {
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
        return entityHealth instanceof PaddleHealth;
    }

    @Override
    protected Class<? extends EntityStat> getStatComponentClass() {
        return ElectricBallStat.class;
    }

    @Override
    protected void onEffectInflicted(EntityEffectController effectController) {

    }

    @Override
    protected StatusEffectInfo getStatusEffectInfo() {
        var statusInfo = new StatusEffectInfo();
        statusInfo.duration = ELECTRIFIED_TIME;
        statusInfo.effect = StatusEffect.Electrified;
        return statusInfo;
    }
}
