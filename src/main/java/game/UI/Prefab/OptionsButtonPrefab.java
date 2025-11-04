package game.UI.Prefab;

import game.GameManager.GameManager;
import game.UI.Buttons.OptionsButton;
import game.UI.MainMenu.MainMenuManager;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public class OptionsButtonPrefab extends Prefab {
    @Override
    public GameObject instantiatePrefab() {
        OptionsButton optionsButton = GameObjectManager.instantiate("OptionsButton")
                .addComponent(OptionsButton.class);
        MainMenuManager.getInstance().linkOptionsButton(optionsButton);

        return optionsButton.getGameObject();
    }
}
