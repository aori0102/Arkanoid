package game.Player;

import game.PowerUp.Index.PowerUp;
import org.Event.EventHandler;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.InputAction.ActionMap;

import java.util.HashSet;
import java.util.List;

/**
 * Central logic for player. Control {@link PowerUp}'s effects
 * and {@link PlayerPaddle} behaviour.
 */
public class Player extends MonoBehaviour {

    public static final int MAX_HEALTH = 100;
    public static final int MAX_LIVES = 3;

    private static Player instance = null;

    private PlayerPowerUpHandler playerPowerUpHandler = null;
    private PlayerController playerController = null;
    private PlayerSkillsHandler playerSkillsHandler = null;

    private int lives = MAX_LIVES;
    private int attack = 10;
    private int health = MAX_HEALTH;

    private PlayerPaddle playerPaddle;

    public EventHandler<Void> onHealthChanged = new EventHandler<>(Player.class);
    public EventHandler<Void> onLivesChanged = new EventHandler<>(Player.class);
    public EventHandler<Void> onHealthReachZero = new EventHandler<>(Player.class);
    public EventHandler<Void> onLivesReachZero = new EventHandler<>(Player.class);
    public EventHandler<HashSet<ActionMap.Action>> onPlayerInputEnter = new EventHandler<>(Player.class);

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
        playerController = addComponent(PlayerController.class);
        playerSkillsHandler = addComponent(PlayerSkillsHandler.class);

    }

    /**
     * Link this player with a paddle. Player will be
     * listening to paddle's {@link PlayerPaddle#onPowerUpConsumed}.
     *
     * @param paddle The paddle to be linked.
     */
    public void linkPlayerPaddle(PlayerPaddle paddle) {
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
    protected void destroyComponent() {
        instance = null;
    }

    @Override
    public void update() {
        if(health <= 0) {
            onHealthReachZero.invoke(this, null);
        }
    }

    private void onPlayerDead() {
        lives--;
        onLivesChanged.invoke(this, null);
        if (lives > 0) {
            health = MAX_HEALTH;
        } else {
            System.out.println("Game over!");
        }
    }

    public static Player getInstance() {
        return instance;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void damage(int amount) {
        health -= amount;
        if (health <= 0) {
            health = 0;
            onPlayerDead();
        }
        onHealthChanged.invoke(this, null);
    }

    public int getLives() {
        return lives;
    }

    public int getHealth() {
        return health;
    }

    public PlayerController getPlayerController() {
        return playerController;
    }

    public PlayerSkillsHandler getPlayerSkillsHandler() {
        return playerSkillsHandler;
    }

}