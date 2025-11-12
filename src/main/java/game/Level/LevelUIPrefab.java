package game.Level;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public final class LevelUIPrefab extends Prefab {

    @Override
    public GameObject instantiatePrefab() {
        return GameObjectManager.instantiate("LevelNotificationUI")
                .addComponent(LevelNotificationUI.class)
                .getGameObject();
    }

}