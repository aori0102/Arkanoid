package game.Voltraxis.Object.PowerCore;

import game.Voltraxis.Voltraxis;
import game.Voltraxis.VoltraxisCharging;
import org.Animation.AnimationClipData;
import org.Animation.SpriteAnimator;
import org.Event.EventActionID;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

public class PowerCoreVisual extends MonoBehaviour {

    private SpriteAnimator animator = null;

    private EventActionID chargingPhaseChangedEventID = null;

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
        animator = getComponent(SpriteAnimator.class);
        animator.playAnimation(AnimationClipData.Voltraxis_PowerCore_Idle, null);
        chargingPhaseChangedEventID = Voltraxis.getInstance().getVoltraxisCharging().onChargingPhaseChanged
                .addListener(this::voltraxisCharging_onChargingPhaseChanged);
    }

    @Override
    public void onDestroy() {
        if (Voltraxis.getInstance() != null) {
            Voltraxis.getInstance().getVoltraxisCharging().onChargingPhaseChanged
                    .removeListener(chargingPhaseChangedEventID);
        }
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

    private void animateLowCharging() {
        animator.playAnimation(AnimationClipData.Voltraxis_PowerCore_Idle_ChargingLow, null);
    }

    private void animateMediumCharging() {
        animator.playAnimation(AnimationClipData.Voltraxis_PowerCore_Idle_ChargingMedium, null);
    }

    private void animateHighCharging() {
        animator.playAnimation(AnimationClipData.Voltraxis_PowerCore_Idle_ChargingHigh, null);
    }

}
