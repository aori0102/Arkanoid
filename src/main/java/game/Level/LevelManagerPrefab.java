package game.Level;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public final class LevelManagerPrefab extends Prefab {
    @Override
    public GameObject instantiatePrefab() {
        return GameObjectManager.instantiate("LevelManager")
                .addComponent(LevelManager.class)
                .getGameObject();
    }
}
