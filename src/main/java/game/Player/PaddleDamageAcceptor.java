package game.Player;

import game.Damagable.DamageAcceptor;
import game.Damagable.DamageInfo;
import org.GameObject.GameObject;

public final class PaddleDamageAcceptor extends DamageAcceptor {

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PaddleDamageAcceptor(GameObject owner) {
        super(owner);
    }

    @Override
    protected void takeDamage(DamageInfo damageInfo) {
        Player.getInstance().getPlayerHealth().damage(damageInfo.amount);
    }

}