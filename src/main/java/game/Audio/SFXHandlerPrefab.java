package game.Audio;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public class SFXHandlerPrefab extends Prefab {
    @Override
    public GameObject instantiatePrefab() {
        SFXHandler sfxHandler = GameObjectManager.instantiate("SFXHandler")
                .addComponent(SFXHandler.class);

        return sfxHandler.getGameObject();
    }
}
