package game.UI.Prefab;

import game.UI.Buttons.StartButton;
import game.UI.MainMenu.MainMenuManager;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public class StartButtonPrefab extends Prefab {
    @Override
    public GameObject instantiatePrefab() {
        StartButton startButton = GameObjectManager.instantiate("StartButton")
                .addComponent(StartButton.class);
        MainMenuManager.getInstance().linkStartButton(startButton);

        return startButton.getGameObject();
    }
}
