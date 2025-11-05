package game.PowerUp.Prefab;

import game.PowerUp.TriplicateBall;
import org.GameObject.GameObject;
import org.Prefab.Prefab;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;
import utils.Vector2;

public class TriplicateBallPrefab extends Prefab {

    @Override
    public GameObject instantiatePrefab() {

        var powerUpObject = PrefabManager.instantiatePrefab(PrefabIndex.PowerUp_Placeholder);
        var triplicateBall = powerUpObject.addComponent(TriplicateBall.class);
        triplicateBall.getTransform().setGlobalScale(new Vector2(0.5, 0.5));

        return powerUpObject;

    }

}
