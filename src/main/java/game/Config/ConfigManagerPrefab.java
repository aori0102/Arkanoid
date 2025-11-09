package game.Config;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public final class ConfigManagerPrefab extends Prefab {
    @Override
    public GameObject instantiatePrefab() {
        return GameObjectManager.instantiate("ConfigManager")
                .addComponent(ConfigManager.class)
                .getGameObject();
    }
}
