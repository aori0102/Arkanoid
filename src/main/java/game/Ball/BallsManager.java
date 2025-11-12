package game.Ball;

import game.Effect.StatusEffectInfo;
import game.Player.Player;
import game.Effect.StatusEffect;
import org.Event.EventActionID;
import org.Event.EventHandler;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;

import java.util.HashSet;

/**
 * Singleton class that manages all balls in the game.
 * <p>
 * Responsibilities:
 * <ul>
 *     <li>Track all balls currently in play.</li>
 *     <li>Handle ball spawn and destruction events.</li>
 *     <li>Apply status effects to all balls.</li>
 *     <li>Provide utility functions such as removing random balls.</li>
 * </ul>
 * </p>
 */
public class BallsManager extends MonoBehaviour {

    // Max number of balls allowed for multiplication
    private static final int MAX_BALL_TO_MULTIPLY = 12;

    private static BallsManager instance = null; // Singleton instance

    private final HashSet<Ball> ballSet = new HashSet<>(); // All active balls
    public int index = 1; // Optional index for tracking balls

    // Event triggered when all balls are destroyed
    public EventHandler<Void> onAllBallDestroyed = new EventHandler<>(BallsManager.class);

    // Event listener IDs
    private EventActionID ball_onAnyBallDestroyed_ID = null;
    private EventActionID ball_onAnyBallSpawned_ID = null;

    /**
     * Constructor.
     *
     * @param owner The owner GameObject of this MonoBehaviour.
     */
    public BallsManager(GameObject owner) {
        super(owner);
        if (instance != null) {
            throw new ReinitializedSingletonException("BallsManager is a singleton!");
        }
        instance = this;
    }

    /**
     * Get the singleton instance.
     *
     * @return BallsManager instance.
     */
    public static BallsManager getInstance() {
        return instance;
    }

    @Override
    public void awake() {
        // Register listeners for ball spawn/destruction
        ball_onAnyBallDestroyed_ID = Ball.onAnyBallDestroyed
                .addListener(this::ball_onAnyBallDestroyed);
        ball_onAnyBallSpawned_ID = Ball.onAnyBallSpawned
                .addListener(this::ball_onAnyBallSpawned);
    }

    @Override
    public void onDestroy() {
        // Remove listeners and clear singleton
        Ball.onAnyBallDestroyed.removeListener(ball_onAnyBallDestroyed_ID);
        Ball.onAnyBallSpawned.removeListener(ball_onAnyBallSpawned_ID);
        instance = null;
    }

    /**
     * Add a ball to the manager.
     *
     * @param ball The Ball object to add.
     */
    private void addBall(Ball ball) {
        ballSet.add(ball);
    }

    /**
     * Remove a ball from the manager.
     *
     * @param ball The Ball object to remove.
     */
    private void removeBall(Ball ball) {
        ballSet.remove(ball);
        if (ballSet.isEmpty()) {
            onAllBallDestroyed.invoke(this, null); // Trigger event if no balls remain
        }
    }

    /**
     * Remove a random ball from the set and destroy it.
     *
     * @return true if a ball was removed, false if none existed.
     */
    public boolean removeRandomBall() {
        if (ballSet.isEmpty()) return false;

        var ballToRemove = ballSet.iterator().next();
        ballSet.remove(ballToRemove);
        GameObjectManager.destroy(ballToRemove.getGameObject());
        return true;
    }

    public void destroyAllBalls() {
        var ballSet = new HashSet<>(this.ballSet);
        for (var ball : ballSet) {
            GameObjectManager.destroy(ball.getGameObject());
        }
    }

    /**
     * Event handler for when a ball is destroyed.
     *
     * @param sender The Ball that was destroyed.
     * @param e      Empty event argument.
     */
    private void ball_onAnyBallDestroyed(Object sender, Void e) {
        if (sender instanceof Ball ball) {
            removeBall(ball);
        }
    }

    /**
     * Event handler for when a ball is spawned.
     *
     * @param sender The Ball that was spawned.
     * @param e      Empty event argument.
     */
    private void ball_onAnyBallSpawned(Object sender, Void e) {
        if (sender instanceof Ball ball) {
            addBall(ball);
        }
    }

    /**
     * Get all active balls.
     *
     * @return Set of active Ball objects.
     */
    public HashSet<Ball> getBallSet() {
        return ballSet;
    }

    /**
     * Apply a status effect to all balls.
     *
     * @param statusEffect The status effect to apply.
     */
    public void applyStatusPowerUpEffect(StatusEffect statusEffect) {
        if (statusEffect != null) {
            var statusInfo = new StatusEffectInfo();
            statusInfo.effect = statusEffect;
            statusInfo.duration = Double.MAX_VALUE; // Permanent effect
            for (var ball : ballSet) {
                ball.getBallEffectController().inflictEffect(statusInfo);
            }
        }
    }

    /**
     * Spawn the initial ball at the start of the game.
     */
    public void spawnInitialBall() {
        var ball = PrefabManager.instantiatePrefab(PrefabIndex.Ball)
                .getComponent(Ball.class);
        Player.getInstance().getPlayerPaddle().isFired = false;
        ballSet.add(ball);
    }

    /**
     * Get the current number of active balls.
     *
     * @return Number of balls.
     */
    public int getBallCount() {
        return ballSet.size();
    }

    /**
     * Check if ball multiplication is allowed.
     *
     * @return true if current ball count is below maximum allowed.
     */
    public boolean canSpawnBallMultiplication() {
        return getBallCount() < MAX_BALL_TO_MULTIPLY;
    }

}
