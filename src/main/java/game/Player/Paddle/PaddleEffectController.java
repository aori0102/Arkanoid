package game.Player.Paddle;

import game.Effect.StatusEffect;
import game.Entity.EntityEffectController;
import game.Entity.EntityHealth;
import game.Entity.EntityStat;
import org.GameObject.GameObject;

public class PaddleEffectController extends EntityEffectController {
    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PaddleEffectController(GameObject owner) {
        super(owner);
    }

    @Override
    public boolean canBeInflictedWith(StatusEffect effect) {
        return true;
    }

    @Override
    protected boolean canHoldMultipleEffect() {
        return false;
    }

    @Override
    protected Class<? extends EntityHealth> getHealthComponentClass() {
        return PaddleHealth.class;
    }

    @Override
    protected Class<? extends EntityStat> getStatComponentClass() {
        return PaddleStat.class;
    }
}
