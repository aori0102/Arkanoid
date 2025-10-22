package game.Player;

import game.PowerUp.Index.PowerUp;
import game.GameObject.Paddle;
import org.Event.EventHandler;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

/**
 * Central logic for player. Control {@link PowerUp}'s effects
 * and {@link Paddle} behaviour.
 */
public class Player extends MonoBehaviour {

    public static final int MAX_HEALTH = 100;
    public static final int MAX_LIVES = 3;

    private static Player instance = null;

    private PlayerPowerUpHandler playerPowerUpHandler = null;
    private int lives = MAX_LIVES;
    private int attack = 10;
    private int health = MAX_HEALTH;

    public EventHandler<Void> onHealthChanged = new EventHandler<>(this);
    public EventHandler<Void> onLivesChanged = new EventHandler<>(this);
    public EventHandler<Void> onHealthReachZero = new EventHandler<>(this);
    public EventHandler<Void> onLivesReachZero = new EventHandler<>(this);

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

}