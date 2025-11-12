package game.Audio;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public class MusicHandlerPrefab extends Prefab {

    @Override
    public GameObject instantiatePrefab() {
        MusicHandler musicHandler = GameObjectManager.instantiate("MusicHandler")
                .addComponent(MusicHandler.class);

        return musicHandler.getGameObject();
    }
}
