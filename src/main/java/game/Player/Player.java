package game.Player;

import game.PowerUp.Index.PowerUp;
import org.Event.EventHandler;
import game.GameObject.Paddle;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.InputAction.ActionMap;

import java.util.HashSet;
import java.util.List;
import utils.Time;

/**
 * Central logic for player. Control {@link PowerUp}'s effects
 * and {@link PlayerPaddle} behaviour.
 */
public class Player extends MonoBehaviour {

    /// Singleton
    private static Player instance = null;

    /// Attributes
    private int attack = 10;

    /// Player core components
    private final PlayerPowerUpHandler playerPowerUpHandler;
    private final PlayerHealth playerHealth;
    private final PlayerSkillsHandler playerSkillsHandler;
    private final PlayerPaddle playerPaddle;
    private PlayerController playerController = null;

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

        playerPowerUpHandler = addComponent(PlayerPowerUpHandler.class);
        playerSkillsHandler = addComponent(PlayerSkillsHandler.class);
        playerPaddle = addComponent(PlayerPaddle.class);
        playerHealth = addComponent(PlayerHealth.class);
        playerController = addComponent(PlayerController.class);
    }

    public PlayerPowerUpHandler getPlayerPowerUpHandler() {
        return playerPowerUpHandler;
    }

    public PlayerHealth getPlayerHealth() {
        return playerHealth;
    }
    public PlayerSkillsHandler getPlayerSkillsHandler() {
        return playerSkillsHandler;
    }

    public PlayerPaddle getPlayerPaddle() {
        return playerPaddle;
    }

    public PlayerController getPlayerController() {
        return playerController;
    }

    /**
     * Link this player with a paddle. Player will be
     * listening to paddle's {@link PlayerPaddle#onPowerUpConsumed}.
     *
     * @param paddle The paddle to be linked.
     */
    public void linkPlayerPaddle(PlayerPaddle paddle) {
        // TODO : Delete this

        this.playerPaddle = paddle;
        playerPaddle.onPowerUpConsumed.addListener(this::paddle_onPowerUpConsumed);
        playerSkillsHandler.linkPlayerPaddle(paddle.getComponent(PlayerPaddle.class));
    }

    /**
     * Called when a power up triggers against this player's
     * registered paddle.
     *
     * @param sender The caller, {@link PlayerPaddle}.
     * @param e      The power up that was triggered.
     */
    private void paddle_onPowerUpConsumed(Object sender, PowerUp e) {
        playerPowerUpHandler.apply(e);
        e.onApplied();
    }

    @Override
    protected void onDestroy() {
        instance = null;
    }

    @Override
    public void awake() {
        Time.addCoroutine(() -> playerHealth.damage(15), Time.time + 3);
    }

    private void onPlayerDead() {
        // Tell health to regenerate
    }

    public static Player getInstance() {
        return instance;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

}