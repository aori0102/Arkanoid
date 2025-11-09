package game.UI.Prefab.OptionsMenu;

import game.UI.Options.OptionsManager;
import game.UI.Options.Slider.SFXVolumeSlider;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public class SFXVolumeSliderPrefab extends Prefab {
    @Override
    public GameObject instantiatePrefab() {
        SFXVolumeSlider sfxVolumeSlider = GameObjectManager.instantiate("SFXVolumeSlider")
                .addComponent(SFXVolumeSlider.class);
        OptionsManager.getInstance().linkSFXVolumeSlider(sfxVolumeSlider);

        return sfxVolumeSlider.getGameObject();
    }
}
