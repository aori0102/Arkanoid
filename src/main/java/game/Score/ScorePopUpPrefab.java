package game.Score;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public final class ScorePopUpPrefab extends Prefab {

    @Override
    public GameObject instantiatePrefab() {
        return GameObjectManager.instantiate("ScorePopUpPrefab")
                .addComponent(ScorePopUp.class)
                .getGameObject();
    }

}