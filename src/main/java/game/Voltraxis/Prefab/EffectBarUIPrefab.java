package game.Voltraxis.Prefab;

import game.Voltraxis.VoltraxisEffectBarUI;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import utils.Vector2;

/**
 * Prefab of Voltraxis' effect bar UI.
 */
public final class EffectBarUIPrefab implements IVoltraxisPrefab {

    private static final Vector2 EFFECT_BAR_ANCHOR = new Vector2(280.0, 100.0);

    @Override
    public GameObject instantiatePrefab() {

        var effectBarUI = GameObjectManager.instantiate("EffectBarUI")
                .addComponent(VoltraxisEffectBarUI.class);
        effectBarUI.getTransform().setGlobalPosition(EFFECT_BAR_ANCHOR);

        return effectBarUI.getGameObject();

    }

}