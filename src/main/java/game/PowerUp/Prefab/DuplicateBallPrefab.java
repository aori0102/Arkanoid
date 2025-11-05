package game.PowerUp.Prefab;

import game.PowerUp.DuplicateBall;
import org.GameObject.GameObject;
import org.Prefab.Prefab;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;
import utils.Vector2;

public class DuplicateBallPrefab extends Prefab {

    @Override
    public GameObject instantiatePrefab() {

        var powerUpObject = PrefabManager.instantiatePrefab(PrefabIndex.PowerUp_Placeholder);
        var duplicateBall = powerUpObject.addComponent(DuplicateBall.class);
        duplicateBall.getTransform().setGlobalScale(new Vector2(0.5, 0.5));

        return powerUpObject;

    }

}
