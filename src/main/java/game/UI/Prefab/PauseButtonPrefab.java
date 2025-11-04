package game.UI.Prefab;

import game.UI.Buttons.PauseButton;
import game.UI.PauseMenu.PauseMenuManager;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public class PauseButtonPrefab extends Prefab {
    @Override
    public GameObject instantiatePrefab() {
        PauseButton pauseButton = GameObjectManager.instantiate("PauseButton")
                .addComponent(PauseButton.class);
        PauseMenuManager.getInstance().linkPauseButton(pauseButton);

        return pauseButton.getGameObject();
    }
}
