package game.UI.Prefab.GameOverMenu;

import game.UI.Buttons.RestartButton;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public class RestartButtonPrefab extends Prefab {
    @Override
    public GameObject instantiatePrefab() {
        RestartButton restartButton = GameObjectManager.instantiate("RestartButton")
                .addComponent(RestartButton.class);

        return restartButton.getGameObject();
    }
}
