package game.Voltraxis;

import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import utils.Time;

import java.util.HashMap;

/**
 * Central class for controlling effects. It handles visualizing
 * current effects and stat modifier as each effect is applied.
 */
public final class VoltraxisEffectManager extends MonoBehaviour {

    /**
     * Utility class to hold effect information.
     */
    public static class EffectInputInfo {

        /**
         * The index of the effect.
         */
        public VoltraxisData.EffectIndex index;

        /**
         * The value this effect applies.
         */
        public double value;

        /**
         * The effect duration.
         */
        public double duration;

        /**
         * The function to run when this effect ends, this includes when the
         * effect is manually removed.
         */
        public Runnable effectEndedCallback;

    }

    /**
     * Linker class that links UI with their info.
     */
    private static class EffectTrackingInfo {

        /**
         * The overall info of the effect.
         */
        public EffectInputInfo info;

        /**
         * The time when the effect starts.
         */
        public double effectStartTick;

        public Time.CoroutineID terminateCoroutineID;

    }

    public static class EffectID {
    }

    private final HashMap<EffectID, EffectTrackingInfo> effectTrackingInfoMap = new HashMap<>();

    public EventHandler<EffectInputInfo> onEffectAdded = new EventHandler<>(VoltraxisEffectManager.class);
    public EventHandler<EffectInputInfo> onEffectRemoved = new EventHandler<>(VoltraxisEffectManager.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public VoltraxisEffectManager(GameObject owner) {
        super(owner);
    }

    /**
     * Add an effect.<br><br>
     * This function initialize a UI for the respective effect and modifies stats based on
     * the effect.
     * <p>
     * This function returns a unique {@link EffectID} for the added effect.
     * This can be used to manually terminate the effect when within certain constraint other
     * than duration.
     * </p>
     *
     * @param info The info of the effect to be added.
     */
    public EffectID addEffect(EffectInputInfo info) {

        onEffectAdded.invoke(this, info);

        var id = new EffectID();

        var effectTracker = new EffectTrackingInfo();
        effectTracker.effectStartTick = Time.getTime();
        effectTracker.info = info;
        effectTracker.terminateCoroutineID
                = Time.addCoroutine(() -> removeEffect(id), Time.getTime() + info.duration);
        effectTrackingInfoMap.put(id, effectTracker);

        return id;

    }

    public void removeEffect(EffectID effectID) {
        var tracker = effectTrackingInfoMap.remove(effectID);
        onEffectRemoved.invoke(this, tracker.info);
        Time.removeCoroutine(tracker.terminateCoroutineID);
    }

}