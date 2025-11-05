package game.PowerUp.Prefab;

import game.PowerUp.ShieldPowerUp;
import org.GameObject.GameObject;
import org.Prefab.Prefab;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;
import utils.Vector2;

public class ShieldPrefab extends Prefab {

    @Override
    public GameObject instantiatePrefab() {

        var powerUpObject = PrefabManager.instantiatePrefab(PrefabIndex.PowerUp_Placeholder);
        var shield = powerUpObject.addComponent(ShieldPowerUp.class);
        shield.getTransform().setGlobalScale(new Vector2(0.5, 0.5));

        return powerUpObject;

    }

}
