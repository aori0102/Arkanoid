package game.UI.Prefab.MainMenu;

import game.UI.Buttons.RecordButton;
import game.UI.MainMenu.MainMenuManager;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public class RecordButtonPrefab extends Prefab {

    @Override
    public GameObject instantiatePrefab() {
        RecordButton recordButton = GameObjectManager.instantiate("RecordButton")
                .addComponent(RecordButton.class);
        MainMenuManager.getInstance().linkRecordButton(recordButton);

        return recordButton.getGameObject();
    }
}
