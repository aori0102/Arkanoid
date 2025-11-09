package game.UI.Prefab.OptionsMenu;

import game.UI.Options.OptionsManager;
import game.UI.Options.Text.SFXVolumeText;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public class SFXVolumeTextPrefab extends Prefab {
    @Override
    public GameObject instantiatePrefab() {
        SFXVolumeText sFXVolumeText = GameObjectManager.instantiate("SFXVolumeText")
                .addComponent(SFXVolumeText.class);

        OptionsManager.getInstance().linkSFXVolumeText(sFXVolumeText);

        return sFXVolumeText.getGameObject();
    }
}
