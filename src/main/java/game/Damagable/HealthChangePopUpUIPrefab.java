package game.Damagable;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;
import org.Text.TextUI;

public final class HealthChangePopUpUIPrefab extends Prefab {

    /**
     * Instantiate new DamagePopUpUI
     * @return new DamagePopUpUI
     */
    @Override
    public GameObject instantiatePrefab() {

        return GameObjectManager.instantiate("DamagePopUpUI")
                .addComponent(TextUI.class)
                .addComponent(HealthChangePopUpUI.class)
                .getGameObject();

    }

}