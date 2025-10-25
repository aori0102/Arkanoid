package game.Player;

import game.PowerUp.Index.PowerUp;
import game.GameObject.Paddle;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import utils.Time;

/**
 * Central logic for player. Control {@link PowerUp}'s effects
 * and {@link Paddle} behaviour.
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