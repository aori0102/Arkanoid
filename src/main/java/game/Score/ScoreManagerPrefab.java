package game.Score;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public final class ScoreManagerPrefab extends Prefab {

    @Override
    public GameObject instantiatePrefab() {
        return GameObjectManager.instantiate("ScoreManager")
                .addComponent(ScoreManager.class)
                .getGameObject();
    }

}