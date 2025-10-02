package org;

import game.object.Obstacle;

import java.util.HashSet;

public class ObstacleManager {

    /**
     * Event fired to paddle when it collides with obstacles.
     */
    public static EventHandler onPaddleCollidedWithObstacles = new EventHandler();

    /**
     * The set stored all the obstacles.
     */
    public static HashSet<Obstacle> obstacles = new HashSet<>();

    /**
     * Adding the obstacles to the set in order to manage it.
     * @param obstacle is the obstacle we want to add
     */
    public static void addObstacle(Obstacle obstacle) {
        obstacles.add(obstacle);
        obstacle.onObstacleCollided.addListener((obstacleCollided) -> {
            onPaddleCollidedWithObstacles.invoke(obstacleCollided);
        });
    }

}
