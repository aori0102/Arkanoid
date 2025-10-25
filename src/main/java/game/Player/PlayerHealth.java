package game.Player;

import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import utils.Time;

/**
 * Class that handles player's health and lives as the game progresses.
 */
public class PlayerHealth extends MonoBehaviour {

    public static final int MAX_HEALTH = 100;
    public static final int MAX_LIVES = 3;

    private int lives = MAX_LIVES;
    private int health = MAX_HEALTH;

    public EventHandler<OnHealthChangedEventArgs> onHealthChanged = new EventHandler<>(PlayerHealth.class);

    public static class OnHealthChangedEventArgs {
        /**
         * The amount of health changed, positive means healed, while negative
         * means lost (damaged)
         */
        public int amount;
    }

    public EventHandler<Void> onLivesChanged = new EventHandler<>(PlayerHealth.class);
    public EventHandler<Void> onHealthReachZero = new EventHandler<>(PlayerHealth.class);
    public EventHandler<Void> onLivesReachZero = new EventHandler<>(PlayerHealth.class);
    public EventHandler<Void> onLivesAndHealthReset = new EventHandler<>(PlayerHealth.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PlayerHealth(GameObject owner) {
        super(owner);
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

    public void damage(int amount) {
        health -= amount;
        if (health <= 0) {
            health = 0;
            onPlayerDead();
        }
        var onPlayerHealthChangedEventArgs = new OnHealthChangedEventArgs();
        onPlayerHealthChangedEventArgs.amount = -amount;
        onHealthChanged.invoke(this, onPlayerHealthChangedEventArgs);
        Time.addCoroutine(() -> damage(15), Time.time + 3);
    }

    public void resetLives() {
        lives = MAX_LIVES;
        health = MAX_HEALTH;
        onHealthChanged.invoke(this, null);
        onLivesChanged.invoke(this, null);
    }

    public int getLives() {
        return lives;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

}