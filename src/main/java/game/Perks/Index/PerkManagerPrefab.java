package game.Perks.Index;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public final class PerkManagerPrefab extends Prefab {

    @Override
    public GameObject instantiatePrefab() {
        return GameObjectManager.instantiate("PerManager")
                .addComponent(PerkManager.class)
                .getGameObject();
    }

}