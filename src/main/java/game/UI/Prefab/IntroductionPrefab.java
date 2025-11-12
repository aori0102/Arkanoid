package game.UI.Prefab;

import game.UI.Introduction;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public class IntroductionPrefab extends Prefab {
    @Override
    public GameObject instantiatePrefab() {
        Introduction introduction = GameObjectManager.instantiate("Introduction")
                .addComponent(Introduction.class);

        return introduction.getGameObject();
    }
}
