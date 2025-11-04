package game.UI.Prefab;

import game.UI.Buttons.MenuButton;
import game.UI.PauseMenu.PauseMenuManager;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public class MenuButtonPrefab extends Prefab {
    @Override
    public GameObject instantiatePrefab() {
        MenuButton menuButton = GameObjectManager.instantiate("MenuButtonPrefab")
                .addComponent(MenuButton.class);
        PauseMenuManager.getInstance().linkMenuButton(menuButton);

        return menuButton.getGameObject();
    }
}
