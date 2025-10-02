package game.Player;

import game.PowerUp.PowerUp;
import game.object.Paddle;
import org.*;

public class Player extends MonoBehaviour {

    private static Player instance = null;

    private PlayerPowerUpHandler playerPowerUpHandler;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public Player(GameObject owner) {

        super(owner);

        if (instance != null) {
            throw new RuntimeException("Player is a singleton!");
        }

        instance = this;
        playerPowerUpHandler = addComponent(PlayerPowerUpHandler.class);

    }

    /**
     * Link this player with a paddle. Player will be
     * listening to paddle's {@link Paddle#onPowerUpConsumed}.
     *
     * @param paddle The paddle to be linked.
     */
    public void linkPaddle(Paddle paddle) {
        paddle.onPowerUpConsumed.addListener(this::paddle_onPowerUpConsumed);
    }

    /**
     * Called when a power up triggers against this player's
     * registered paddle.
     *
     * @param sender The caller, {@link Paddle}.
     * @param e      The power up that was triggered.
     */
    private void paddle_onPowerUpConsumed(Object sender, PowerUp e) {
        playerPowerUpHandler.apply(e);
        e.onApplied();
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        throw new RuntimeException("Cannot clone Player, which is a singleton!");
    }

    @Override
    protected void destroyComponent() {
        playerPowerUpHandler = null;
        instance = null;
    }

}
