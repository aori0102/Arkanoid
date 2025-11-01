package game.MapGenerator;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public final class BrickMapManagerPrefab extends Prefab {

    @Override
    public GameObject instantiatePrefab() {
        return GameObjectManager.instantiate("BrickMapManager")
                .addComponent(BrickMapManager.class)
                .getGameObject();
    }

}