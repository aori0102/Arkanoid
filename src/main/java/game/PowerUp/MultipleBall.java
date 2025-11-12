package game.PowerUp;

import game.Player.PlayerPowerUpHandler;
import game.PowerUp.Index.PowerUp;
import game.Player.Paddle.PlayerPaddle;
import org.GameObject.GameObject;

/**
 * Base class for all power-ups that create multiple balls.
 *
 * This class provides common functionality for multiple ball power-ups such as
 * DuplicateBall or TriplicateBall. It handles linking to the player paddle
 * and the player's power-up handler, while the specific ball duplication logic
 * is implemented in child classes.
 */
public abstract class MultipleBall extends PowerUp {

    /** Reference to the player's power-up handler for triggering events. */
    protected PlayerPowerUpHandler playerPowerUpHandler;

    /** Reference to the player's paddle. */
    protected PlayerPaddle playerPaddle;

    /** Alias reference to the player's paddle (redundant, can be used in child classes). */
    protected PlayerPaddle paddle;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The GameObject that owns this component.
     */
    public MultipleBall(GameObject owner) {
        super(owner);
    }

    /**
     * Handle the event when multiple balls need to be spawned.
     *
     * This method will be called when the paddle collides with the power-up.
     * Child classes must override this method to define the specific
     * behavior of ball duplication or triplication.
     */
    protected abstract void handleOnMultipleRequest();

    /**
     * Link the player's power-up handler to this MultipleBall power-up.
     *
     * @param playerPowerUpHandler The PlayerPowerUpHandler to link.
     */
    public void linkPlayerPowerUp(PlayerPowerUpHandler playerPowerUpHandler) {
        this.playerPowerUpHandler = playerPowerUpHandler;
    }

    /**
     * Link the player's paddle to this MultipleBall power-up.
     *
     * @param playerPaddle The PlayerPaddle to link.
     */
    public void linkPaddle(PlayerPaddle playerPaddle) {
        this.playerPaddle = playerPaddle;
    }
}
