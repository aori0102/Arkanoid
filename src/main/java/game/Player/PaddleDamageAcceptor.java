package game.Player;

import game.Damagable.DamageAcceptor;
import game.Damagable.DamageInfo;
import game.Effect.StatusEffect;
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
        super.takeDamage(damageInfo);
        Player.getInstance().getPlayerHealth().damage(damageInfo.amount);
    }

    @Override
    protected void applyEffect(StatusEffect effect) {

    }

}