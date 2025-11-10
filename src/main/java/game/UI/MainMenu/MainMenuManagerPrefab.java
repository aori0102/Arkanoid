package game.UI.MainMenu;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public final class MainMenuManagerPrefab extends Prefab {
    @Override
    public GameObject instantiatePrefab() {
        return GameObjectManager.instantiate("MainMenuManager")
                .addComponent(MainMenuManager.class)
                .getGameObject();
    }
}
