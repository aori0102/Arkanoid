package org;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import game.Obstacle.Obstacle;
import game.object.Paddle;
import javafx.scene.Group;
import utils.Time;
import utils.Vector2;

public class ObstacleManager extends MonoBehaviour{

    // Number of obstacles can be spawned per minutes
    private final double baseDensityPerMinutes = 20;

    // Max number of existing obstacle
    private final int maxActive = 10;

    // Minimum time between each spawn times
    private final double minSpawnInerval = 0.5;

    // Minimum gap between obstacles
    private final double minSpawnDistance = 200;

    // Safe range to spawn in order to not collied with paddle
    private final double safeRadiusFromPaddle = 250;

    // Smoothing coefficient
    private final double alpha = 2;

    // Next spawn time
    private double nextSpawnTime;

    // Expected number of spawned obstacles
    private double lambda;

    // Time since last spawn
    private double timeSinceLastSpawn = 0;
    private Random random = new Random();

    private Paddle paddle;
    private boolean canStart = false;
    private double warningSignExistingTime = 3;
    private double warningSignExisitingCounter = 0;


    // Singleton
    public static ObstacleManager instance;

    /**
     * Event fired to paddle when it collides with obstacles.
     */
    public EventHandler<Void> onPaddleCollidedWithObstacle ;

    // List to store all the obstacles
    public List<Obstacle> obstacles = new ArrayList<>();

    public ObstacleManager(GameObject gameObjectManager) {
        super(gameObjectManager);
        instance = this;
    }

    public void awake() {
        onPaddleCollidedWithObstacle = new EventHandler<Void>(instance );
        for (var obstacle : obstacles) {
            obstacle.gameObject.setActive(false);
        }

    }

    public void update() {
        spawnObstacles();
    }

    /**
     * Adding the obstacles to the set in order to manage it.
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
     * @param obstacle : the obstacle which is wanted to remove
     */
    public void removeObstacle(Obstacle obstacle) {
        obstacles.remove(obstacle);
    }

    /**
     * Disable a
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
     * @return true if the obstacle can spawn
     */
    private boolean handleSpawningStatus() {
        timeSinceLastSpawn += Time.deltaTime;
        lambda = (baseDensityPerMinutes / 60) * (1 + 0.05);

        // Current number of active obstacle
        int active = countActiveObstacles();

        // The remaining unactive range
        double occupancy = Math.min(1.0, active / maxActive);
        // The spawn adjust rate
        double scale = Math.max(0, 1 - Math.pow(occupancy, alpha));
        // The most effective lambda
        double effectiveLambda = lambda * scale;

        // The poisson process result
        double poissonProcess = effectiveLambda * Time.deltaTime;
        if (random.nextDouble() < poissonProcess) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Spawning obstacles
     */
    private void spawnObstacles() {
        // Check if obstacle can be spawned
        if (!handleSpawningStatus()) return;
        // Check if the current active number is still in the limit range
        if (countActiveObstacles() >= maxActive) return;
        // check if the respawn time in each obstacle is valid
        if (timeSinceLastSpawn < minSpawnInerval) return;

        // Trying to spawn
        int tries = 10;
        for (int i = 0; i < tries; i++) {
            Vector2 pos = sampleSpawnPosition();
            spawnAt(pos);
            timeSinceLastSpawn = 0;
        }
    }

    /**
     *Random some position in the world space
     * @return the spawn position
     */
    private Vector2 sampleSpawnPosition() {
        int tries = 10;
        for (int i = 0; i < tries; i++) {
            double x = 50 + random.nextDouble() * (1200 - 100);
            double y = 1000;

            Vector2 pos = new Vector2(x, y);

            if (isTooCloseToPaddle(pos)) continue;
            if (isTooCloseToObstacles(pos)) continue;

            return new Vector2(x, y);
        } return new Vector2(600, 1000);
    }
    /**
     * Spawn obstacle at a chosen position
     * @param pos : the spawn position
     */
    private void spawnAt(Vector2 pos) {
        if (obstacles.isEmpty() || sampleSpawnPosition() == null) return;

        Obstacle chosen = obstacles.get(random.nextInt(obstacles.size()));
        chosen.gameObject.setActive(true);
        chosen.getTransform().setGlobalPosition(pos);
    }


    /**
     * Check if the spawn pos is too close to the paddle or not
     * @param pos : the chosen position
     * @return true if the pos is too close to the paddle
     */
    private boolean isTooCloseToPaddle(Vector2 pos) {
        Vector2 playerPos = paddle.getTransform().getGlobalPosition();
        return Vector2.distance(pos, playerPos) < safeRadiusFromPaddle;
    }

    /**
     * Check if the spawn pos is too close to other obstacles
     * @param pos : the chosen position
     * @return true if the pos is too close to the other obstacles
     */
    private boolean isTooCloseToObstacles(Vector2 pos) {
        for (Obstacle o : obstacles) {
            if (!o.gameObject.isActive()) continue;
            Vector2 op = o.getTransform().getGlobalPosition();
            if (Vector2.distance(pos, op) < minSpawnDistance) return true;
        }
        return false;
    }

    /**
     * Count the number of active obstacles
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
     * Get the current root of the game
     * @return the root group
     */
    public Group getRoot() {
        return Main.root;
    }

    /**
     * Set the paddle
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
