package game.LaserBeam;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public final class LaserBeamSFXHandlerPrefab extends Prefab {
    @Override
    public GameObject instantiatePrefab() {
        return GameObjectManager.instantiate("LaserBeamSFXHandlerPrefab")
                .addComponent(LaserBeamSFXHandler.class)
                .getGameObject();
    }
}
