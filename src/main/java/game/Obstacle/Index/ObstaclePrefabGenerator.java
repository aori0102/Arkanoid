package game.Obstacle.Index;

import game.Obstacle.Index.Generator.LaserPrefab;
import game.Obstacle.Index.Generator.ObstaclePrefab;
import game.Obstacle.Laser.Laser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Singleton-like class that manages obstacle prefabs.
 * <p>
 * Responsibilities:
 * - Stores a mapping between obstacle types and their corresponding prefabs.
 * - Maintains a list of all registered obstacle classes.
 * - Provides an easy way to generate obstacles via their prefabs.
 * </p>
 */
public final class ObstaclePrefabGenerator {

    /**
     * Maps each obstacle class to its corresponding prefab generator.
     * <p>
     * Example: Laser.class -> LaserPrefab
     * </p>
     */
    public static final HashMap<Class<? extends Obstacle>, ObstaclePrefab> obstaclePrefabMap = new HashMap<>();

    /**
     * List of all registered obstacle classes.
     * <p>
     * This preserves the insertion order for generating obstacles.
     * </p>
     */
    public static final List<Class<? extends Obstacle>> registeredObstacleList;

    static {
        // Register Laser obstacle
        obstaclePrefabMap.put(Laser.class, new LaserPrefab());

        // Initialize the list of registered obstacles
        registeredObstacleList = new ArrayList<>(obstaclePrefabMap.keySet());
    }

}
