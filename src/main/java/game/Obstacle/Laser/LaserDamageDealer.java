package game.Obstacle.Laser;

import game.Effect.StatusEffect;
import game.Effect.StatusEffectInfo;
import game.Entity.EntityDamageDealer;
import game.Entity.EntityEffectController;
import game.Entity.EntityHealth;
import game.Entity.EntityStat;
import game.Player.Paddle.PaddleHealth;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;

public class LaserDamageDealer extends EntityDamageDealer {
    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public LaserDamageDealer(GameObject owner) {
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
        return LaserStat.class;
    }

    @Override
    protected void onEffectInflicted(EntityEffectController effectController) {
        System.out.println("LaserDamageDealer EffectInflicted");
    }

    @Override
    protected StatusEffectInfo getStatusEffectInfo() {
        System.out.println("LaserDamageDealer StatusEffectInfo");
        var stunnedEffectInfo = new StatusEffectInfo();
        stunnedEffectInfo.duration = 3;
        stunnedEffectInfo.effect = StatusEffect.Stunned;

        return stunnedEffectInfo;
    }
}
