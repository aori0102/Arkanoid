package game.UI.Prefab.RecordMenu;

import game.UI.RecordMenu.RecordMenuManager;
import game.UI.RecordMenu.RecordTitle;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public class RecordTitlePrefab extends Prefab {
    @Override
    public GameObject instantiatePrefab() {
        RecordTitle recordTitle = GameObjectManager.instantiate("RecordTitle")
                .addComponent(RecordTitle.class);
        RecordMenuManager.getInstance().linkRecordTitle(recordTitle);

        return recordTitle.getGameObject();
    }
}
