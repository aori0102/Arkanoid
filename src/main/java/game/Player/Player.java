package game.Player;

import game.Player.Paddle.PlayerPaddle;
import game.PowerUp.Index.PowerUp;
import org.Annotation.LinkViaPrefab;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

/**
 * Central logic for player. Control {@link PowerUp}'s effects
 * and {@link PlayerPaddle} behaviour.
 */
public class Player extends MonoBehaviour {

    /// Singleton
    private static Player instance = null;

    @LinkViaPrefab
    private PlayerPaddle playerPaddle = null;

    private final PlayerPowerUpHandler playerPowerUpHandler = addComponent(PlayerPowerUpHandler.class);
    private final PlayerLives playerLives = addComponent(PlayerLives.class);
    private final PlayerSkillsHandler playerSkillsHandler = addComponent(PlayerSkillsHandler.class);
    private final PlayerController playerController = addComponent(PlayerController.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public Player(GameObject owner) {

        super(owner);

        if (instance != null) {
            throw new ReinitializedSingletonException("Player is a singleton");
        }
        instance = this;
    }

    public static Player getInstance() {
        return instance;
    }

    @Override
    protected void onDestroy() {
        instance = null;
    }

    public PlayerPaddle getPlayerPaddle() {
        return playerPaddle;
    }

    public PlayerPowerUpHandler getPlayerPowerUpHandler() {
        return playerPowerUpHandler;
    }

    public PlayerLives getPlayerLives() {
        return playerLives;
    }

    public PlayerSkillsHandler getPlayerSkillsHandler() {
        return playerSkillsHandler;
    }

    public PlayerController getPlayerController() {
        return playerController;
    }

    /**
     * Link the paddle the player will control.<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link }
     * as part of component linking process.</i></b>
     *
     * @param playerPaddle This player's paddle.
     */
    public void linkPlayerPaddle(PlayerPaddle playerPaddle) {
        this.playerPaddle = playerPaddle;
    }

}