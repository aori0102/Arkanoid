package game.Brick.Sound;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public final class BrickSFXHandlerPrefab extends Prefab {
    @Override
    public GameObject instantiatePrefab() {
        return GameObjectManager.instantiate("BrickSFXHandler")
                .addComponent(BrickSFXHandler.class)
                .getGameObject();
    }
}
