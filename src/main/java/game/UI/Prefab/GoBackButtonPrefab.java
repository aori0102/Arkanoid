package game.UI.Prefab;

import game.UI.Buttons.GoBackButton;
import game.UI.GoBackButtonManager;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public class GoBackButtonPrefab extends Prefab {

    @Override
    public GameObject instantiatePrefab() {
        GoBackButton goBackButton = GameObjectManager.instantiate("GoBackButton")
                .addComponent(GoBackButton.class);
        GoBackButtonManager.getInstance().linkGoBackButton(goBackButton);

        return goBackButton.getGameObject();
    }
}
