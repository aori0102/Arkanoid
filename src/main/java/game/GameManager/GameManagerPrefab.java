package game.GameManager;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public final class GameManagerPrefab extends Prefab {

    @Override
    public GameObject instantiatePrefab() {
        return GameObjectManager.instantiate("GameManagerPrefab")
                .addComponent(GameManager.class)
                .getGameObject();
    }

}