package game.Voltraxis.Prefab;

import game.Voltraxis.VoltraxisSFX;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public class VoltraxisSFXPrefab extends Prefab {
    @Override
    public GameObject instantiatePrefab() {
        VoltraxisSFX voltraxisSFX = GameObjectManager.instantiate("VoltraxisSFX")
                .addComponent(VoltraxisSFX.class);

        return voltraxisSFX.getGameObject();
    }
}
