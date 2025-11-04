package game.PowerUp;

import game.Player.PlayerPowerUpHandler;
import game.GameObject.BallsManager;
import game.PowerUp.Index.PowerUp;
import game.Player.Paddle.PlayerPaddle;
import org.GameObject.GameObject;

/**
 * Base class for all the multiple ball power up
 */
public abstract class MultipleBall extends PowerUp {

    protected PlayerPowerUpHandler playerPowerUpHandler;
    protected PlayerPaddle playerPaddle;
    protected PlayerPaddle paddle;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public MultipleBall(GameObject owner) {
        super(owner);
    }

    /**
     * Handle the onMultipleRequest event. This will be called when the paddle
     * collided with a power up. The child class will override this method.
     */
    protected abstract void handleOnMultipleRequest();

    /**
     * Link the player power up
     *
     * @param playerPowerUpHandler : the linked playerPowerUpHandler
     */
    public void linkPlayerPowerUp(PlayerPowerUpHandler playerPowerUpHandler) {
        this.playerPowerUpHandler = playerPowerUpHandler;
    }

    /**
     * Link the paddle
     *
     * @param playerPaddle: the linked paddle
     */
    public void linkPaddle(PlayerPaddle playerPaddle) {
        this.playerPaddle = playerPaddle;
    }

}