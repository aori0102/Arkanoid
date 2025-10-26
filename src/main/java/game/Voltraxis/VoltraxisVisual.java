package game.Voltraxis;

import org.Animation.AnimationClipData;
import org.Animation.SpriteAnimator;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

/**
 * Visual component of Voltraxis.
 */
public final class VoltraxisVisual extends MonoBehaviour {

    private SpriteAnimator animator = null;
    private Voltraxis voltraxis = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public VoltraxisVisual(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        animator = getComponent(SpriteAnimator.class);
        animateIdle();

        voltraxis.onBasicAttackCommenced.addListener(this::voltraxis_onBasicAttackCommenced);
        voltraxis.getVoltraxisCharging().onChargingPhaseChanged
                .addListener(this::voltraxisCharging_onChargingPhaseChanged);
        voltraxis.getVoltraxisCharging().onChargingEntered
                .addListener(this::voltraxisCharging_onChargingStarted);
        voltraxis.getVoltraxisCharging().onFinishUnleashing
                .addListener(this::voltraxisCharging_onFinishUnleashing);
        voltraxis.getVoltraxisCharging().onUnleashingLaser
                .addListener(this::voltraxisCharging_onUnleashingLaser);
    }

    /**
     * Link the central brain of Voltraxis to this visual.<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link VoltraxisPrefab}
     * as part of component linking process.</i></b>
     *
     * @param voltraxis The central brain of Voltraxis.
     */
    public void linkVoltraxis(Voltraxis voltraxis) {
        this.voltraxis = voltraxis;
    }

    /**
     * Called when {@link VoltraxisCharging#onChargingPhaseChanged} is invoked.<br><br>
     * Update visual corresponding to the charging phase of Voltraxis.
     */
    private void voltraxisCharging_onChargingPhaseChanged(Object sender, VoltraxisCharging.ChargingPhase e) {
        animateCharging(e);
    }

    /**
     * Called when {@link Voltraxis#onBasicAttackCommenced} is invoked.<br><br>
     * Plays normal attack animation.
     */
    private void voltraxis_onBasicAttackCommenced(Object sender, Void e) {
        System.out.println("Play normal attack");
        animator.playAnimation(AnimationClipData.Voltraxis_NormalAttack, this::animateIdle);
    }

    /**
     * Called when {@link VoltraxisCharging#onChargingEntered} is invoked.<br><br>
     * This function animate Voltraxis upon ready for charging.
     */
    private void voltraxisCharging_onChargingStarted(Object sender, Void e) {
        animator.playAnimation(AnimationClipData.Voltraxis_Charging_EnterCharging, null);
    }

    /**
     * Called when {@link VoltraxisCharging#onFinishUnleashing} is invoked.<br><br>
     * This function animate Voltraxis upon starting unleashing ultimate laser.
     */
    private void voltraxisCharging_onFinishUnleashing(Object sender, Void e) {
        animator.playAnimation(AnimationClipData.Voltraxis_Charging_ExitUltimate, this::animateIdle);
    }

    /**
     * Called when {@link VoltraxisCharging#onUnleashingLaser} is invoked.<br><br>
     * This function animate Voltraxis upon starting unleashing ultimate laser.
     */
    private void voltraxisCharging_onUnleashingLaser(Object sender, Void e) {
        animator.playAnimation(AnimationClipData.Voltraxis_Charging_UnleashingLaser, null);
    }

    private void animateIdle() {
        System.out.println("Play idle");
        animator.playAnimation(AnimationClipData.Voltraxis_Idle, null);
    }

    private void animateCharging(VoltraxisCharging.ChargingPhase chargingPhase) {
        if (chargingPhase != VoltraxisCharging.ChargingPhase.None) {
            animator.playAnimation(chargingPhase.animationIndex, null);
        }
    }

}