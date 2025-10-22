package game.Player;

import game.PowerUp.Index.PowerUp;
import game.GameObject.Paddle;
import org.Event.EventHandler;
import org.Exception.ReinitializedSingletonException;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

/**
 * Central logic for player. Control {@link PowerUp}'s effects
 * and {@link Paddle} behaviour.
 */
public class Player extends MonoBehaviour {

    public static final int MAX_HEALTH = 100;
    public static final int MAX_LIVES = 3;
    private int attack = 10;
    private int health = MAX_HEALTH;
    private int nodeHealth = 3;

    public EventHandler<Void> OnHealthReachZero = new EventHandler<>(this);
    public EventHandler<Void> OnNodeHealthReachZero = new EventHandler<>(this);


    private static Player instance = null;

    private PlayerPowerUpHandler playerPowerUpHandler = null;
    private int health = MAX_HEALTH;
    private int lives = MAX_LIVES;

    public EventHandler<Void> onHealthChanged = new EventHandler<>(this);
    public EventHandler<Void> onLivesChanged = new EventHandler<>(this);

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
    protected void destroyComponent() {
        instance = null;
    }

    @Override
    public void update() {
        if(health <= 0) {
            OnHealthReachZero.invoke(this, null);
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

    public int getHealth() {
        return health;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    private void player_onHealthReachZero(Object sender, Void e) {
        health = MAX_HEALTH;
        nodeHealth--;
        if (nodeHealth <= 0) {
            OnNodeHealthReachZero.invoke(this, null);
        }
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

}