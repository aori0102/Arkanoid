package game.LaserBeam;

import org.Audio.AudioManager;
import org.Audio.SFXAsset;
import org.Event.EventActionID;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

public final class LaserBeamSFXHandler extends MonoBehaviour {

    private static LaserBeamSFXHandler instance = null;

    private EventActionID laserBeam_onAnyLaserBeamSpawned_ID = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public LaserBeamSFXHandler(GameObject owner) {
        super(owner);
        if (instance != null) {
            throw new ReinitializedSingletonException("LaserBeamSFXHandler is a singleton!");
        }
        instance = this;
    }

    @Override
    public void start() {
        laserBeam_onAnyLaserBeamSpawned_ID = LaserBeam.onAnyLaserBeamSpawned
                .addListener(this::laserBeam_onAnyLaserBeamSpawned);
    }

    @Override
    protected void onDestroy() {
        LaserBeam.onAnyLaserBeamSpawned.removeListener(laserBeam_onAnyLaserBeamSpawned_ID);
        instance = null;
    }

    /**
     * Called when {@link LaserBeam#onAnyLaserBeamSpawned} is invoked.<br><br>
     * This function plays a sound when a {@link LaserBeam} is spawned.
     *
     * @param sender Event caller {@link LaserBeam}.
     * @param e      Empty event argument.
     */
    private void laserBeam_onAnyLaserBeamSpawned(Object sender, Void e) {
        AudioManager.playSFX(SFXAsset.SFXIndex.OnLaserBeamShoot);
    }

}
