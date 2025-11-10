package game.UI.Prefab.RecordMenu;

import game.UI.RecordMenu.RecordMenuManager;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public class RecordMenuManagerPrefab extends Prefab {
    @Override
    public GameObject instantiatePrefab() {
        return GameObjectManager.instantiate("RecordMenuManager")
                .addComponent(RecordMenuManager.class).getGameObject();
    }
}
