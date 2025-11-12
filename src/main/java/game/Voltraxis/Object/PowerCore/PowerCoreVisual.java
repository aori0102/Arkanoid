package game.Voltraxis.Object.PowerCore;

import game.Entity.EntityHealth;
import game.Voltraxis.Prefab.PowerCorePrefab;
import game.Voltraxis.Voltraxis;
import game.Voltraxis.VoltraxisCharging;
import javafx.scene.paint.Color;
import org.Animation.AnimationClipData;
import org.Animation.SpriteAnimator;
import org.Annotation.LinkViaPrefab;
import org.Event.EventActionID;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Rendering.SpriteRenderer;
import utils.Time;

public class PowerCoreVisual extends MonoBehaviour {

    private static final double DAMAGE_FLASHING_TIME = 0.1;

    private final SpriteAnimator animator = addComponent(SpriteAnimator.class);
    private final SpriteRenderer renderer = addComponent(SpriteRenderer.class);

    private EventActionID chargingPhaseChangedEventID = null;
    private EventActionID powerCoreHealth_onHealthChanged_ID = null;

    private Time.CoroutineID disableDamageTint_coroutineID = null;

    @LinkViaPrefab
    private PowerCore powerCore = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PowerCoreVisual(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        animator.playAnimation(AnimationClipData.Voltraxis_PowerCore_Idle, null);
        chargingPhaseChangedEventID = Voltraxis.getInstance().getVoltraxisCharging().onChargingPhaseChanged
                .addListener(this::voltraxisCharging_onChargingPhaseChanged);
    }

    @Override
    public void start() {
        powerCoreHealth_onHealthChanged_ID = powerCore.getPowerCoreHealth().onHealthChanged
                .addListener(this::powerCoreHealth_onHealthChanged);
    }

    @Override
    public void onDestroy() {
        try {
            Voltraxis.getInstance().getVoltraxisCharging().onChargingPhaseChanged
                    .removeListener(chargingPhaseChangedEventID);
        } catch (NullPointerException _) {
        }

        try {
            powerCore.getPowerCoreHealth().onHealthChanged
                    .removeListener(powerCoreHealth_onHealthChanged_ID);
        } catch (NullPointerException _) {
        }

        Time.removeCoroutine(disableDamageTint_coroutineID);
    }

    /**
     * Called when {@link VoltraxisCharging#onChargingPhaseChanged} is invoked.<br><br>
     * This function animate the power core based on the charging phase of Voltraxis.
     *
     * @param sender Event caller {@link VoltraxisCharging}.
     * @param e      Empty event argument
     */
    private void voltraxisCharging_onChargingPhaseChanged(Object sender, VoltraxisCharging.ChargingPhase e) {
        switch (e) {
            case Phase_1 -> animateLowCharging();
            case Phase_3 -> animateMediumCharging();
            case Phase_5 -> animateHighCharging();
        }
    }

    /**
     * Animate power core when Voltraxis' charging is in low state.
     */
    private void animateLowCharging() {
        animator.playAnimation(AnimationClipData.Voltraxis_PowerCore_Idle_ChargingLow, null);
    }

    /**
     * Animate power core when Voltraxis' charging is in medium state.
     */
    private void animateMediumCharging() {
        animator.playAnimation(AnimationClipData.Voltraxis_PowerCore_Idle_ChargingMedium, null);
    }

    /**
     * Animate power core when Voltraxis' charging is in high state.
     */
    private void animateHighCharging() {
        animator.playAnimation(AnimationClipData.Voltraxis_PowerCore_Idle_ChargingHigh, null);
    }

    /**
     * Called when {@link PowerCoreHealth#onHealthChanged} is invoked.<br><br>
     * This function displays a brief red tint when damaged.
     *
     * @param sender Event caller {@link PowerCoreHealth}.
     * @param e      Event argument containing health change information.
     */
    private void powerCoreHealth_onHealthChanged(Object sender, EntityHealth.OnHealthChangedEventArgs e) {
        if (e.alterType.isDamage()) {
            renderer.setOverlayColor(Color.RED);
            disableDamageTint_coroutineID
                    = Time.addCoroutine(this::disableDamageTint, DAMAGE_FLASHING_TIME);
        }
    }

    /**
     * Remove red tint after damaged.
     */
    private void disableDamageTint() {
        renderer.setOverlayColor(Color.WHITE);
    }

    /**
     * Link the power core class.<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link PowerCorePrefab}
     * as part of component linking process.</i></b>
     *
     * @param powerCore The power core central class.
     */
    public void linkPowerCore(PowerCore powerCore) {
        this.powerCore = powerCore;
    }

}
