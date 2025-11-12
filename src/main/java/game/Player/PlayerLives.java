package game.Player;

import game.Ball.BallsManager;
import game.GameManager.LevelState;
import game.Level.LevelManager;
import game.Player.Paddle.PaddleHealth;
import org.Event.EventActionID;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

/**
 * Class that handles player's health and lives as the game progresses.
 */
public class PlayerLives extends MonoBehaviour {

    private int lives = PlayerAttributes.MAX_LIVES;
    private boolean dead = false;

    private EventActionID ballsManager_onAllBallDestroyed_ID = null;

    public EventHandler<Void> onLivesDecreased = new EventHandler<>(PlayerLives.class);
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
        ballsManager_onAllBallDestroyed_ID = BallsManager.getInstance().onAllBallDestroyed
                .addListener(this::ballsManager_onAllBallDestroyed);
    }

    @Override
    public void onDestroy() {
        if (BallsManager.getInstance() != null) {
            BallsManager.getInstance().onAllBallDestroyed.removeListener(ballsManager_onAllBallDestroyed_ID);
        }
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

    /**
     * Called when {@link BallsManager#onAllBallDestroyed} is invoked.<br><br>
     * This function kills the paddle immediately when there are no more balls.
     *
     * @param sender Event caller {@link BallsManager}.
     * @param e      Empty event argument.
     */
    private void ballsManager_onAllBallDestroyed(Object sender, Void e) {
        if (LevelManager.getInstance().getLevelState() == LevelState.Playing) {
            onPlayerDead();
        }
    }

    private void onPlayerDead() {
        if (dead) {
            return;
        }
        dead = true;
        lives--;
        if (lives == 0) {
            onLivesReachZero.invoke(this, null);
        } else {
            onLivesDecreased.invoke(this, null);
            Player.getInstance().getPlayerPaddle().getPaddleHealth().resetHealth();
            dead = false;
        }
    }

    public int getLives() {
        return lives;
    }

}