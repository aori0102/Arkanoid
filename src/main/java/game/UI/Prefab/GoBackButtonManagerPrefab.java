package game.UI.Prefab;

import game.UI.GoBackButtonManager;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public class GoBackButtonManagerPrefab extends Prefab {
    @Override
    public GameObject instantiatePrefab() {
        GoBackButtonManager gobackButtonManager = GameObjectManager.instantiate("GoBackManager")
                .addComponent(GoBackButtonManager.class);

        return gobackButtonManager.getGameObject();
    }
}
