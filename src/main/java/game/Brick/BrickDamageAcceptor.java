package game.Brick;

import game.Damagable.DamageAcceptor;
import game.Damagable.DamageInfo;
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
    protected void takeDamage(DamageInfo damageInfo) {
        brick.damage(damageInfo.amount);
    }

}