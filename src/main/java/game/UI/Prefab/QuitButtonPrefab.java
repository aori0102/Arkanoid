package game.UI.Prefab;

import game.UI.Buttons.QuitButton;
import game.UI.MainMenu.MainMenuManager;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public class QuitButtonPrefab extends Prefab {
    @Override
    public GameObject instantiatePrefab() {
        QuitButton quitButton = GameObjectManager.instantiate("QuitButton").
                addComponent(QuitButton.class);
        MainMenuManager.getInstance().linkQuitButton(quitButton);

        return quitButton.getGameObject();

    }
}
