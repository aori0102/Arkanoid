package game.Voltraxis.Object;

import game.Damagable.DamageAcceptor;
import game.Damagable.DamageInfo;
import game.Damagable.DamageType;
import game.Damagable.ICanDealDamage;
import game.Effect.StatusEffect;
import game.Player.PaddleDamageAcceptor;
import game.Voltraxis.Voltraxis;
import game.Voltraxis.VoltraxisData;
import org.Animation.AnimationClipData;
import org.Animation.SpriteAnimator;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Rendering.SpriteRenderer;
import utils.MathUtils;
import utils.Time;

public class UltimateLaser extends MonoBehaviour implements ICanDealDamage {

    private static final double LASER_APPEAR_RATE = 23.013;

    private SpriteRenderer renderer = null;
    private double ratio = 0.0;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public UltimateLaser(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        getComponent(SpriteAnimator.class).playAnimation(AnimationClipData.Voltraxis_UltimateLaser, null);
        renderer = getComponent(SpriteRenderer.class);
        renderer.setFillAmount(0.0);
        renderer.setFillType(SpriteRenderer.FillType.Vertical_TopToBottom);
    }

    @Override
    public void update() {
        ratio = MathUtils.lerp(ratio, 1.0, Time.getDeltaTime() * LASER_APPEAR_RATE);
        renderer.setFillAmount(ratio);
    }

    @Override
    public DamageInfo getDamageInfo() {
        var damageInfo = new DamageInfo();
        damageInfo.amount = (int) (VoltraxisData.ULTIMATE_LASER_DAMAGE_PROPORTION * Voltraxis.getInstance().getVoltraxisStatManager().getAttack());
        damageInfo.type = DamageType.HitPlayer;
        return damageInfo;
    }

    @Override
    public StatusEffect getEffect() {
        return null;
    }

    @Override
    public void onDamaged() {
    }

    @Override
    public boolean isDamageTarget(DamageAcceptor damageAcceptor) {
        return damageAcceptor instanceof PaddleDamageAcceptor;
    }

}