package game.Obstacle.Index;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public final class ObstacleManagerPrefab extends Prefab {
    @Override
    public GameObject instantiatePrefab() {
        return GameObjectManager.instantiate("ObstacleManager")
                .addComponent(ObstacleManager.class)
                .getGameObject();
    }
}
