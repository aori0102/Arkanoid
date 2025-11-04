package game.UI.Prefab;

import game.UI.Buttons.ContinueButton;
import game.UI.MainMenu.MainMenuManager;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public class ContinueButtonPrefab extends Prefab {

    @Override
    public GameObject instantiatePrefab() {
        ContinueButton continueButton = GameObjectManager.instantiate("ContinueButton")
                .addComponent(ContinueButton.class);
        MainMenuManager.getInstance().linkContinueButton(continueButton);

        return continueButton.getGameObject();
    }
}
