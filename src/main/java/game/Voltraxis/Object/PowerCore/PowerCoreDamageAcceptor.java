package game.Voltraxis.Object.PowerCore;

import game.Damagable.DamageAcceptor;
import game.Damagable.DamageInfo;
import org.GameObject.GameObject;

public final class PowerCoreDamageAcceptor extends DamageAcceptor {

    private PowerCore powerCore = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PowerCoreDamageAcceptor(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        super.awake();
        powerCore = getComponent(PowerCore.class);
    }

    @Override
    protected void takeDamage(DamageInfo damageInfo) {
        powerCore.getPowerCoreHealth().damage(damageInfo.amount);
    }

}