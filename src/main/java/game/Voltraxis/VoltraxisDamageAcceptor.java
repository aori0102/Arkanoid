package game.Voltraxis;

import game.Damagable.DamageAcceptor;
import game.Damagable.DamageInfo;
import game.Effect.StatusEffect;
import org.GameObject.GameObject;

public final class VoltraxisDamageAcceptor extends DamageAcceptor {

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public VoltraxisDamageAcceptor(GameObject owner) {
        super(owner);
    }

    @Override
    protected void takeDamage(DamageInfo damageInfo) {
        super.takeDamage(damageInfo);
        var voltraxisStatManager = Voltraxis.getInstance().getVoltraxisStatManager();
        int finalDamage = (int) (damageInfo.amount * ((double) voltraxisStatManager.getDefence() / (voltraxisStatManager.getDefence() + VoltraxisData.DEFENSE_STRENGTH_SCALE)));
        Voltraxis.getInstance().getVoltraxisHealth().damage(finalDamage);
    }

    @Override
    protected void applyEffect(StatusEffect effect) {

    }

}