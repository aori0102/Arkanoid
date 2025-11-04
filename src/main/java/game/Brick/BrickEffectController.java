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
    public void awake() {
        super.awake();
        onEffectInflicted.addListener(this::brickEffectController_onEffectInflicted);
    }

    /**
     * Called when {@link BrickEffectController#onEffectInflicted} is invoked.<br><br>
     * a
     *
     * @param sender j
     * @param e      j
     */
    private void brickEffectController_onEffectInflicted(Object sender, StatusEffect e) {
        System.out.println(gameObject.getName() + " inflicted with " + e);
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