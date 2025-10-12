package org;


import java.util.ArrayList;
import java.util.List;

import game.Obstacle.Obstacle;
import game.object.Paddle;
import utils.Random;
import utils.Time;
import utils.Vector2;

public class ObstacleManager extends MonoBehaviour {

    // Number of obstacles can be spawned per minutes
    private static final double BASE_DENSITY_PER_MINUTE = 20;

    // Max number of existing obstacle
    private static final int MAX_ACTIVE = 10;

    // Minimum time between each spawn times
    private static final double MIN_SPAWN_INTERVAL = 0.5;

    // Minimum gap between obstacles
    private static final double MIN_SPAWN_DISTANCE = 200;

    // Safe range to spawn in order to not collied with paddle
    private static final double SAFE_RADIUS_FROM_PADDLE = 250;

    // Smoothing coefficient
    private static final double ALPHA = 2;

    // Time since last spawn
    private double timeSinceLastSpawn = 0;

    private Paddle paddle;

    // Singleton
    public static ObstacleManager instance;

    /**
     * Event fired to paddle when it collides with obstacles.
     */
    public EventHandler<Void> onPaddleCollidedWithObstacle = new EventHandler<>(this);

    // List to store all the obstacles
    public List<Obstacle> obstacles = new ArrayList<>();

    public ObstacleManager(GameObject gameObjectManager) {
        super(gameObjectManager);
        instance = this;
    }

    public void awake() {
        for (var obstacle : obstacles) {
            obstacle.gameObject.setActive(false);
        }

    }

    public void update() {
        spawnObstacles();
    }

    /**
     * Adding the obstacles to the set in order to manage it.
     *
     * @param obstacle is the obstacle we want to add
     */
    public void addObstacle(Obstacle obstacle) {
        obstacles.add(obstacle);
        obstacle.onObstacleCollided.addListener((e, obstacleCollided) -> {
            onPaddleCollidedWithObstacle.invoke(instance, null);
        });
    }

    /**
     * Remove the obstacle from the list
     *
     * @param obstacle : the obstacle which is wanted to remove
     */
    public void removeObstacle(Obstacle obstacle) {
        obstacles.remove(obstacle);
    }

    /**
     * Disable a
     *
     * @param obstacle
     */
    public void disableObstacle(Obstacle obstacle) {
        obstacles.get(obstacles.indexOf(obstacle)).gameObject.setActive(false);
    }

    /**
     * Handle spawning status by using Poisson process. The formular of this process is
     * p = lambda * timeDelta
     * Lambda is the expected spawn obstacles number and is calculated by the density per minutes multiplies with
     * a specific coefficient
     * Occupancy is the rest of the spawn range. It is calculated by currentActive divides for maxActivate.
     * Scale is the adjust spawn coefficient, which is used to adjust spawn rate due to current density and calculated by
     * one minus power of occupancy and alpha
     * The final lambda we use is effectiveLambda - which equals to lambda multiply with scale
     *
     * @return true if the obstacle can spawn
     */
    private boolean handleSpawningStatus() {
        timeSinceLastSpawn += Time.deltaTime;
        // Expected number of spawned obstacles
        double lambda = (BASE_DENSITY_PER_MINUTE / 60) * (1 + 0.05);

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
        return Random.Range(0.0, 1.0) < poissonProcess;

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
        int tries = 10;
        for (int i = 0; i < tries; i++) {
            Vector2 pos = sampleSpawnPosition();
            spawnAt(pos);
            timeSinceLastSpawn = 0;
        }
    }

    /**
     * Random some position in the world space
     *
     * @return the spawn position
     */
    private Vector2 sampleSpawnPosition() {
        int tries = 10;
        for (int i = 0; i < tries; i++) {
            double x = 50 + Random.Range(0.0, 1.0) * (1200 - 100);
            double y = 1000;

            Vector2 pos = new Vector2(x, y);

            if (isTooCloseToPaddle(pos)) continue;
            if (isTooCloseToObstacles(pos)) continue;

            return new Vector2(x, y);
        }
        return new Vector2(600, 1000);
    }

    /**
     * Spawn obstacle at a chosen position
     *
     * @param pos : the spawn position
     */
    private void spawnAt(Vector2 pos) {
        if (obstacles.isEmpty()) return;

        Obstacle chosen = obstacles.get(Random.Range(0,obstacles.size()));
        chosen.gameObject.setActive(true);
        chosen.getTransform().setGlobalPosition(pos);
    }


    /**
     * Check if the spawn pos is too close to the paddle or not
     *
     * @param pos : the chosen position
     * @return true if the pos is too close to the paddle
     */
    private boolean isTooCloseToPaddle(Vector2 pos) {
        Vector2 playerPos = paddle.getTransform().getGlobalPosition();
        return Vector2.distance(pos, playerPos) < SAFE_RADIUS_FROM_PADDLE;
    }

    /**
     * Check if the spawn pos is too close to other obstacles
     *
     * @param pos : the chosen position
     * @return true if the pos is too close to the other obstacles
     */
    private boolean isTooCloseToObstacles(Vector2 pos) {
        for (Obstacle o : obstacles) {
            if (!o.gameObject.isActive()) continue;
            Vector2 op = o.getTransform().getGlobalPosition();
            if (Vector2.distance(pos, op) < MIN_SPAWN_DISTANCE) return true;
        }
        return false;
    }

    /**
     * Count the number of active obstacles
     *
     * @return number of active obstacles
     */
    private int countActiveObstacles() {
        int count = 0;
        var iterator = obstacles.iterator();
        while (iterator.hasNext()) {
            Obstacle obstacle = iterator.next();

            if (obstacle == null || obstacle.gameObject == null || obstacle.gameObject.isDestroyed()) {
                iterator.remove();
                continue;
            }

            if (obstacle.gameObject.isActive()) {
                count++;
            }
        }

        return count;
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
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
    }

    @Override
    protected void destroyComponent() {

    }
}
