package game.Obstacle.Index;


import game.GameObject.Paddle;
import org.Event.EventHandler;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import utils.Random;
import utils.Time;
import utils.Vector2;

import java.util.HashSet;

public class ObstacleManager extends MonoBehaviour {

    /**
     * Number of obstacles can be spawned per minutes.
     */
    private static final double BASE_DENSITY_PER_MINUTE = 20;

    /**
     * Max number of existing obstacle.
     */
    private static final int MAX_ACTIVE = 10;

    /**
     * Minimum time between each spawn times.
     */
    private static final double MIN_SPAWN_INTERVAL = 0.5;

    /**
     * Minimum gap between obstacles.
     */
    private static final double MIN_SPAWN_DISTANCE = 200;

    /**
     * Safe range to spawn in order to not collide with paddle.
     */
    private static final double SAFE_RADIUS_FROM_PADDLE = 250;

    /**
     * Maximum attempt to spawn an obstacle.
     */
    private static final int SPAWNING_ATTEMPT_THRESHOLD=10;

    /**
     * Smoothing coefficient
     */
    private static final double ALPHA = 2;

    // Singleton
    private static ObstacleManager instance;

    private double timeSinceLastSpawn = 0;
    private Paddle paddle;

    private final HashSet<Obstacle> obstacleSet = new HashSet<>();

    /**
     * Event fired to paddle when it collides with obstacles.
     */
    public EventHandler<Void> onPaddleCollidedWithObstacle = new EventHandler<>(this);

    public ObstacleManager(GameObject gameObjectManager) {
        super(gameObjectManager);
        if (instance != null) {
            throw new ReinitializedSingletonException("ObstacleManager is a singleton!");
        }
        instance = this;
    }

    public static ObstacleManager getInstance() {
        return instance;
    }

    @Override
    public void update() {
        spawnObstacles();
    }

    /**
     * Handle spawning status by using <b>Poisson process</b>. The formula of this process is
     * {@code p = lambda * timeDelta}, where {@code lambda} is the expected spawn obstacles
     * number and is calculated by the density per minutes multiplies with
     * a specific coefficient<br><br>
     * {@code occupancy} is the rest of the spawn range. It is calculated by {@code currentActive}
     * divided by {@code maxActivate}. {@code scale} is the adjust spawn coefficient, which is
     * used to adjust spawn rate due to current density and calculated by
     * {@code 1 - pow(occupancy, alpha)}. The final lambda we use is
     * {@code effectiveLambda} - which equals to {@code lambda} multiply with {@code scale}.
     *
     * @return {@code true} if the obstacle can spawn
     */
    private boolean handleSpawningStatus() {
        timeSinceLastSpawn += Time.deltaTime;
        // Expected number of spawned obstacles
        var level = 1;        // TODO: add a level system then change this part - Aori
        double lambda = (BASE_DENSITY_PER_MINUTE / 60) * (1 + level * 0.05);

        // Current number of active obstacle
        int active = countActiveObstacles();

        // The remaining unactive range
        double occupancy = Math.min(1.0, (double) active / MAX_ACTIVE);
        // The spawn adjust rate
        double scale = Math.max(0, 1 - Math.pow(occupancy, ALPHA));
        // The most effective lambda
        double effectiveLambda = lambda * scale;

        // The poisson process result
        double poissonProcess = effectiveLambda * Time.deltaTime;
        return Random.range(0.0, 1.0) < poissonProcess;

    }

    /**
     * Spawning obstacles
     */
    private void spawnObstacles() {
        // Check if obstacle can be spawned
        if (!handleSpawningStatus()) return;
        // Check if the current active number is still in the limit range
        if (countActiveObstacles() >= MAX_ACTIVE) return;
        // check if the respawn time in each obstacle is valid
        if (timeSinceLastSpawn < MIN_SPAWN_INTERVAL) return;

        // Trying to spawn
        Vector2 pos = sampleSpawnPosition();
        spawnAt(pos);
        timeSinceLastSpawn = 0;
    }

    /**
     * Random some position in the world space
     *
     * @return the spawn position
     */
    private Vector2 sampleSpawnPosition() {
        int tries = 10;
        for (int i = 0; i < tries; i++) {
            double x = 50 + Random.range(0.0, 1.0) * (1200 - 100);
            double y = 1000;

            Vector2 pos = new Vector2(x, y);

            if (isTooCloseToPaddle(pos)) continue;
            if (isTooCloseToObstacles(pos)) continue;

            return new Vector2(x, y);
        }
        return new Vector2(9999999, 9999999);
    }

    /**
     * Spawn obstacle at a chosen position
     *
     * @param position : the spawn position
     */
    private void spawnAt(Vector2 position) {
        var chosenKey = ObstaclePrefabGenerator.registeredObstacleList.get(
                Random.range(0, ObstaclePrefabGenerator.registeredObstacleList.size())
        );
        Obstacle chosen = ObstaclePrefabGenerator.obstaclePrefabMap.get(chosenKey).generateObstacle();
        chosen.getGameObject().setActive(true);
        chosen.getTransform().setGlobalPosition(position);
        obstacleSet.add(chosen);
        chosen.onObstacleDestroyed.addListener(this::obstacle_onObstacleDestroyed);
    }

    /**
     * Called when {@link Obstacle#onObstacleDestroyed} is invoked.<br><br>
     * This function clears the obstacle's data inside the registered set.
     */
    private void obstacle_onObstacleDestroyed(Object sender, Void e) {
        if (sender instanceof Obstacle obstacle) {
            obstacle.onObstacleDestroyed.removeAllListeners();
            obstacleSet.remove(obstacle);
        }
    }

    /**
     * Check if the spawn pos is too close to the paddle or not
     *
     * @param position The chosen position
     * @return {@code true} if the pos is too close to the paddle
     */
    private boolean isTooCloseToPaddle(Vector2 position) {
        Vector2 paddlePosition = paddle.getTransform().getGlobalPosition();
        return Vector2.distance(position, paddlePosition) < SAFE_RADIUS_FROM_PADDLE;
    }

    /**
     * Check if the spawn position is too close to other obstacles
     *
     * @param position : the chosen position
     * @return true if the pos is too close to the other obstacles
     */
    private boolean isTooCloseToObstacles(Vector2 position) {
        for (var obstacle : obstacleSet) {
            if (!obstacle.getGameObject().isActive()) continue;
            Vector2 obstaclePosition = obstacle.getTransform().getGlobalPosition();
            if (Vector2.distance(position, obstaclePosition) < MIN_SPAWN_DISTANCE) return true;
        }
        return false;
    }

    /**
     * Count the number of active obstacles
     *
     * @return number of active obstacles
     */
    private int countActiveObstacles() {
        return obstacleSet.size();
    }

    /**
     * Set the paddle
     *
     * @param chosenPaddle the set paddle
     */
    public void setPaddle(Paddle chosenPaddle) {
        paddle = chosenPaddle;
    }

    @Override
    protected void destroyComponent() {

    }
}
