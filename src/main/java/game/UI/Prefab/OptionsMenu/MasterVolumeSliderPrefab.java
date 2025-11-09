package game.UI.Prefab.OptionsMenu;

import game.UI.Options.Slider.MasterVolumeSlider;
import game.UI.Options.OptionsManager;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public class MasterVolumeSliderPrefab  extends Prefab {
    @Override
    public GameObject instantiatePrefab() {
        MasterVolumeSlider masterVolumeSlider = GameObjectManager.instantiate("MasterVolumeSliderPrefab")
                .addComponent(MasterVolumeSlider.class);
        OptionsManager.getInstance().linkMasterVolumeSlider(masterVolumeSlider);

        return masterVolumeSlider.getGameObject();
    }
}
