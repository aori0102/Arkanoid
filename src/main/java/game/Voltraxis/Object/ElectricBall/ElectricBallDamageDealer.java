package game.Voltraxis.Object.ElectricBall;

import game.Entity.EntityDamageDealer;
import game.Entity.EntityHealth;
import game.Entity.EntityStat;
import game.Player.Paddle.PaddleHealth;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;

public final class ElectricBallDamageDealer extends EntityDamageDealer {
    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public ElectricBallDamageDealer(GameObject owner) {
        super(owner);
    }

    @Override
    protected void onDamageDealt() {
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
}
