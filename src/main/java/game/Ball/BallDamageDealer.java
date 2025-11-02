package game.Ball;

import game.Brick.BrickHealth;
import game.Entity.EntityDamageDealer;
import game.Entity.EntityHealth;
import game.Entity.EntityStat;
import org.GameObject.GameObject;

public final class BallDamageDealer extends EntityDamageDealer {

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public BallDamageDealer(GameObject owner) {
        super(owner);
    }

    @Override
    protected void onDamageDealt() {
    }

    @Override
    protected boolean canDealDamage() {
        return true;
    }

    @Override
    protected boolean isDamageTarget(EntityHealth entityHealth) {
        return entityHealth instanceof BrickHealth;
    }

    @Override
    protected Class<? extends EntityStat> getStatComponentClass() {
        return BallStat.class;
    }

}
