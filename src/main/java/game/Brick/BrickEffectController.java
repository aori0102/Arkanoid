package game.Brick;

import game.Effect.StatusEffect;
import game.Entity.EntityEffectController;
import game.Entity.EntityHealth;
import game.Entity.EntityStat;
import org.GameObject.GameObject;

public final class BrickEffectController extends EntityEffectController {

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public BrickEffectController(GameObject owner) {
        super(owner);
    }

    @Override
    protected boolean canBeInflictedWith(StatusEffect effect) {
        return true;
    }

    @Override
    protected boolean canHoldMultipleEffect() {
        return true;
    }

    @Override
    protected Class<? extends EntityHealth> getHealthComponentClass() {
        return BrickHealth.class;
    }

    @Override
    protected Class<? extends EntityStat> getStatComponentClass() {
        return BrickStat.class;
    }

}