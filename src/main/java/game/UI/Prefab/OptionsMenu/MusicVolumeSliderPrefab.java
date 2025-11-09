package game.UI.Prefab.OptionsMenu;

import game.UI.Options.Slider.MusicVolumeSlider;
import game.UI.Options.OptionsManager;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public class MusicVolumeSliderPrefab extends Prefab {
    @Override
    public GameObject instantiatePrefab() {
        MusicVolumeSlider musicVolumeSlider = GameObjectManager.instantiate("MusicVolumeSlider")
                .addComponent(MusicVolumeSlider.class);
        OptionsManager.getInstance().linkMusicVolumeSlider(musicVolumeSlider);

        return musicVolumeSlider.getGameObject();
    }
}
