package game.Player;

import game.PowerUp.Index.PowerUp;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import utils.Time;

/**
 * Central logic for player. Control {@link PowerUp}'s effects
 * and {@link PlayerPaddle} behaviour.
 */
public class Player extends MonoBehaviour {

    /// Singleton
    private static Player instance = null;

    /// Attributes
    private static final int ATTACK = 80;
    private static final int BASE_SPEED = 1000;
    private int currentSpeed;

    /// Player core components
    private final PlayerPowerUpHandler playerPowerUpHandler;
    private final PlayerHealth playerHealth;
    private final PlayerSkillsHandler playerSkillsHandler;
    private PlayerPaddle playerPaddle;
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
        currentSpeed = BASE_SPEED;

        playerPowerUpHandler = addComponent(PlayerPowerUpHandler.class);
        playerSkillsHandler = addComponent(PlayerSkillsHandler.class);
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

    @Override
    protected void onDestroy() {
        instance = null;
    }

    @Override
    public void awake() {
        Time.addCoroutine(() -> playerHealth.damage(15), Time.getTime() + 3);
    }

    public static Player getInstance() {
        return instance;
    }

    public int getAttack() {
        return ATTACK;
    }

    public int getBaseSpeed() {
        return BASE_SPEED;
    }

    public int getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(int currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    /**
     * <br><br>
     * <b><i><u>NOTE</u> : Only use within {@link }
     * as part of component linking process.</i></b>
     *
     * @param playerPaddle .
     */
    public void linkPlayerPaddle(PlayerPaddle playerPaddle) {
        this.playerPaddle = playerPaddle;
    }

}