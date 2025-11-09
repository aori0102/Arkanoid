package game.PlayerData;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public final class DataManagerPrefab extends Prefab {
    @Override
    public GameObject instantiatePrefab() {
        return  GameObjectManager.instantiate("DataManager")
                .addComponent(DataManager.class)
                .getGameObject();
    }
}
