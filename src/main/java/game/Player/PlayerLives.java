package game.Player;

import game.Player.Paddle.PaddleHealth;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

/**
 * Class that handles player's health and lives as the game progresses.
 */
public class PlayerLives extends MonoBehaviour {

    private int lives = PlayerData.MAX_LIVES;

    public EventHandler<Void> onLivesChanged = new EventHandler<>(PlayerLives.class);
    public EventHandler<Void> onLivesReachZero = new EventHandler<>(PlayerLives.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PlayerLives(GameObject owner) {
        super(owner);
    }

    @Override
    public void start() {
        Player.getInstance().getPlayerPaddle().getPaddleHealth().onHealthReachesZero
                .addListener(this::paddleHealth_onPaddleHealthReachesZero);
    }

    /**
     * Called when {@link PaddleHealth#onPaddleHealthReachesZero} is invoked.<br><br>
     * This function decreases lives when the paddle's health reaches zero.
     *
     * @param sender Event caller {@link PaddleHealth}.
     * @param e      Empty event argument.
     */
    private void paddleHealth_onPaddleHealthReachesZero(Object sender, Void e) {
        onPlayerDead();
    }

    private void onPlayerDead() {
        lives--;
        onLivesChanged.invoke(this, null);
        if (lives == 0) {
            onLivesReachZero.invoke(this, null);
        }
    }

    public int getLives() {
        return lives;
    }

}