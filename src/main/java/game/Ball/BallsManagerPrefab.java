package game.Ball;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public final class BallsManagerPrefab extends Prefab {
    @Override
    public GameObject instantiatePrefab() {
        return GameObjectManager.instantiate("BallManager")
                .addComponent(BallsManager.class)
                .getGameObject();
    }
}
