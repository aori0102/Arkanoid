package game.UI.Prefab.OptionsMenu;

import game.UI.Options.OptionsManager;
import game.UI.Options.Text.MusicVolumeText;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public class MusicVolumeTextPrefab extends Prefab {
    @Override
    public GameObject instantiatePrefab() {
        MusicVolumeText musicVolumeText = GameObjectManager.instantiate("MusicVolumeText")
                .addComponent(MusicVolumeText.class);
        OptionsManager.getInstance().linkMusicVolumeText(musicVolumeText);

        return musicVolumeText.getGameObject();
    }
}
