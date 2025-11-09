package game.UI.Prefab.OptionsMenu;

import game.UI.Options.OptionsManager;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public class OptionsManagerPrefab extends Prefab {

    @Override
    public GameObject instantiatePrefab() {
        OptionsManager optionsManager = GameObjectManager.instantiate("OptionsManager")
                .addComponent(OptionsManager.class);

        return optionsManager.getGameObject();
    }
}
