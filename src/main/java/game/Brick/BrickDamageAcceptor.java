package game.Brick;

import game.Damagable.DamageAcceptor;
import game.Damagable.DamageInfo;
import game.Damagable.ICanDealDamage;
import game.Effect.StatusEffect;
import org.GameObject.GameObject;

public final class BrickDamageAcceptor extends DamageAcceptor {

    private Brick brick = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public BrickDamageAcceptor(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        super.awake();
        brick = getComponent(Brick.class);
    }

    @Override
    protected void takeDamage(DamageInfo damageInfo, ICanDealDamage damageObject) {
        super.takeDamage(damageInfo, damageObject);
        brick.damage(damageInfo.amount);

    }

    @Override
    protected void applyEffect(StatusEffect effect) {
        brick.setStatusBrickEffect(effect);
    }

}