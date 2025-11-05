package game.Obstacle.Index;

import game.Obstacle.Index.Generator.LaserPrefab;
import game.Obstacle.Index.Generator.ObstaclePrefab;
import game.Obstacle.Laser.Laser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class ObstaclePrefabGenerator {

    public static final HashMap<Class<? extends Obstacle>, ObstaclePrefab> obstaclePrefabMap = new HashMap<>();
    public static final List<Class<? extends Obstacle>> registeredObstacleList;

    static {

        obstaclePrefabMap.put(Laser.class, new LaserPrefab());

        registeredObstacleList = new ArrayList<>(obstaclePrefabMap.keySet());

    }

}