package game.UI.Prefab.OptionsMenu;

import game.UI.Options.OptionsManager;
import game.UI.Options.Text.MasterVolumeText;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public class MasterVolumeTextPrefab extends Prefab {
    @Override
    public GameObject instantiatePrefab() {
        MasterVolumeText masterVolumeText = GameObjectManager.instantiate("MasterVolumeText")
                .addComponent(MasterVolumeText.class);
        OptionsManager.getInstance().linkMasterVolumeText(masterVolumeText);

        return masterVolumeText.getGameObject();
    }
}
