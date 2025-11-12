package game.Audio;

import game.Brick.Brick;
import game.LaserBeam.LaserBeam;
import game.Player.Paddle.PlayerPaddle;
import game.PowerUp.Index.PowerUp;
import org.Audio.AudioManager;
import org.Audio.SFXAsset;
import org.Event.EventActionID;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

public class SFXHandler extends MonoBehaviour {
    private EventActionID sfxHandler_onAnyBrickHit = null;
    private EventActionID sfxHandler_onLaserBeamShoot = null;
    private EventActionID sfxHandler_onPowerUpConsumed = null;
    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public SFXHandler(GameObject owner) {
        super(owner);

    }

    @Override
    public void start() {
        sfxHandler_onAnyBrickHit= Brick.onAnyBrickHit.addListener(SFXHandler::sfxHandler_onAnyBrickHit);
        sfxHandler_onLaserBeamShoot = LaserBeam.onLaserBeamShoot.addListener(SFXHandler::sfxHandler_onLaserBeamShoot);
        sfxHandler_onPowerUpConsumed = PlayerPaddle.onAnyPowerUpConsumed.addListener(SFXHandler::sfxHandler_onPowerUpConsumed);
    }

    @Override
    protected void onDestroy() {
        Brick.onAnyBrickHit.removeListener(sfxHandler_onAnyBrickHit);
        LaserBeam.onLaserBeamShoot.removeListener(sfxHandler_onLaserBeamShoot);
        PlayerPaddle.onAnyPowerUpConsumed.removeListener(sfxHandler_onPowerUpConsumed);
    }

    private static void sfxHandler_onAnyBrickHit(Object sender, Void e) {
        AudioManager.playSFX(SFXAsset.SFXIndex.OnBrickHit);
    }

    private static void sfxHandler_onLaserBeamShoot(Object sender, Void e) {
        AudioManager.playSFX(SFXAsset.SFXIndex.OnLaserBeamShoot);
    }

    private static void sfxHandler_onPowerUpConsumed(Object sender, PowerUp e) {
        AudioManager.playSFX(SFXAsset.SFXIndex.OnPowerReceived);
    }


}
