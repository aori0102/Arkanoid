package game.Ball;

import game.Effect.StatusEffect;
import game.Entity.EntityEffectController;
import game.Entity.EntityHealth;
import game.Entity.EntityStat;
import org.GameObject.GameObject;

public final class BallEffectController extends EntityEffectController {

    public static final double EFFECT_INFLICTED_TIME = 100.0;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public BallEffectController(GameObject owner) {
        super(owner);
    }

    @Override
    protected boolean canBeInflictedWith(StatusEffect effect) {
        return true;
    }

    @Override
    protected boolean canHoldMultipleEffect() {
        return false;
    }

    @Override
    protected Class<? extends EntityHealth> getHealthComponentClass() {
        return null;
    }

    @Override
    protected Class<? extends EntityStat> getStatComponentClass() {
        return BallStat.class;
    }

}