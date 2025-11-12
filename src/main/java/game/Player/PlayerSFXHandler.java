package game.Player;

import game.Player.Paddle.PlayerPaddle;
import game.PowerUp.Index.PowerUp;
import org.Audio.AudioManager;
import org.Audio.SFXAsset;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

public final class PlayerSFXHandler extends MonoBehaviour {

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PlayerSFXHandler(GameObject owner) {
        super(owner);
    }

    @Override
    public void start() {
        Player.getInstance().getPlayerPaddle().onPowerUpConsumed
                .addListener(this::playerPaddle_onPowerUpConsumed);
    }

    /**
     * Called when {@link PlayerPaddle#onPowerUpConsumed} is invoked.<br><br>
     * This function plays a sound upon consuming a {@link PowerUp}
     *
     * @param sender Event caller {@link PlayerPaddle}.
     * @param e      Event argument representing the consumed {@link PowerUp}.
     */
    private void playerPaddle_onPowerUpConsumed(Object sender, PowerUp e) {
        AudioManager.playSFX(SFXAsset.SFXIndex.OnPowerReceived);
    }

}
