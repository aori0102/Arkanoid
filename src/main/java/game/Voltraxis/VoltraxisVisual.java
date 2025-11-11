package game.Voltraxis;

import game.Entity.EntityHealth;
import game.Voltraxis.Prefab.VoltraxisVisualPrefab;
import javafx.scene.paint.Color;
import org.Animation.AnimationClipData;
import org.Animation.SpriteAnimator;
import org.Annotation.LinkViaPrefab;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.ParticleSystem.Emitter.ParticleEmitter;
import org.Rendering.SpriteRenderer;
import utils.Time;

/**
 * Visual component of Voltraxis.
 */
public final class VoltraxisVisual extends MonoBehaviour {

    private static final double DAMAGE_FLASHING_TIME = 0.1;

    private final SpriteAnimator animator = addComponent(SpriteAnimator.class);
    private final SpriteRenderer spriteRenderer = addComponent(SpriteRenderer.class);

    @LinkViaPrefab
    private ParticleEmitter smokeEmitter = null;

    private Time.CoroutineID disableDamageTint_coroutineID = null;

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

        animateIdle();

        Voltraxis.getInstance().getVoltraxisNormalAttackBrain().onBasicAttackCommenced
                .addListener(this::voltraxis_onBasicAttackCommenced);
        Voltraxis.getInstance().getVoltraxisCharging().onChargingPhaseChanged
                .addListener(this::voltraxisCharging_onChargingPhaseChanged);
        Voltraxis.getInstance().getVoltraxisCharging().onChargingEntered
                .addListener(this::voltraxisCharging_onChargingStarted);
        Voltraxis.getInstance().getVoltraxisCharging().onFinishUnleashing
                .addListener(this::voltraxisCharging_onFinishUnleashing);
        Voltraxis.getInstance().getVoltraxisCharging().onUnleashingLaser
                .addListener(this::voltraxisCharging_onUnleashingLaser);
        Voltraxis.getInstance().getVoltraxisCharging().onChargingTerminated
                .addListener(this::voltraxisCharging_onChargingTerminated);
        Voltraxis.getInstance().getVoltraxisCharging().onBossWeakened
                .addListener(this::voltraxisCharging_onBossWeakened);
        Voltraxis.getInstance().getVoltraxisHealth().onHealthChanged
                .addListener(this::voltraxisHealth_onHealthChanged);

    }

    @Override
    protected void onDestroy() {
        Time.removeCoroutine(disableDamageTint_coroutineID);
    }

    /**
     * Called when {@link VoltraxisHealth#onHealthChanged} is invoked.<br><br>
     * This function sets a brief red tint when {@link Voltraxis} is damaged.
     *
     * @param sender Event caller {@link VoltraxisHealth}.
     * @param e      Event argument containing information on the health change.
     */
    private void voltraxisHealth_onHealthChanged(Object sender, EntityHealth.OnHealthChangedEventArgs e) {
        if (e.alterType.isDamage()) {
            spriteRenderer.setOverlayColor(Color.RED);
            disableDamageTint_coroutineID
                    = Time.addCoroutine(this::disableDamageTint, Time.getTime() + DAMAGE_FLASHING_TIME);
        }
    }

    private void disableDamageTint() {
        spriteRenderer.setOverlayColor(Color.WHITE);
    }

    /**
     * Called when {@link VoltraxisCharging#onBossWeakened} is invoked.<br><br>
     * This function animates Voltraxis on weakened state.
     *
     * @param sender Event caller {@link VoltraxisCharging}.
     * @param e      Empty event argument.
     */
    private void voltraxisCharging_onBossWeakened(Object sender, Void e) {
        animator.playAnimation(AnimationClipData.Voltraxis_Weakened, null);
        smokeEmitter.startEmit();
    }

    /**
     * Called when {@link VoltraxisCharging#onChargingPhaseChanged} is invoked.<br><br>
     * Update visual corresponding to the charging phase of Voltraxis.
     */
    private void voltraxisCharging_onChargingPhaseChanged(Object sender, VoltraxisCharging.ChargingPhase e) {
        animateCharging(e);
    }

    /**
     * Called when {@link VoltraxisNormalAttackBrain#onBasicAttackCommenced} is invoked.<br><br>
     * Plays normal attack animation.
     */
    private void voltraxis_onBasicAttackCommenced(Object sender, Void e) {
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
     * Called when {@link VoltraxisCharging#onChargingTerminated} is invoked.<br><br>
     * This function reverts animation to Idle state after Voltraxis' charging.
     *
     * @param sender Event caller {@link VoltraxisCharging}.
     * @param e      Empty event argument.
     */
    private void voltraxisCharging_onChargingTerminated(Object sender, Void e) {
        animateIdle();
        smokeEmitter.stopEmit();
    }

    /**
     * Called when {@link VoltraxisCharging#onUnleashingLaser} is invoked.<br><br>
     * This function animate Voltraxis upon starting unleashing ultimate laser.
     */
    private void voltraxisCharging_onUnleashingLaser(Object sender, Void e) {
        animator.playAnimation(AnimationClipData.Voltraxis_Charging_UnleashingLaser, null);
    }

    private void animateIdle() {
        animator.playAnimation(AnimationClipData.Voltraxis_Idle, null);
    }

    private void animateCharging(VoltraxisCharging.ChargingPhase chargingPhase) {
        if (chargingPhase != VoltraxisCharging.ChargingPhase.None) {
            animator.playAnimation(chargingPhase.animationIndex, null);
        }
    }

    /**
     * Link the smoke particle emitter.<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link VoltraxisVisualPrefab}
     * as part of component linking process.</i></b>
     *
     * @param smokeEmitter The smoke particle emitter.
     */
    public void linkSmokeEmitter(ParticleEmitter smokeEmitter) {
        this.smokeEmitter = smokeEmitter;
    }

}