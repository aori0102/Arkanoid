package game.Voltraxis.Prefab;

import game.Voltraxis.VoltraxisEffectIconUI;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Layer.RenderLayer;
import org.Prefab.Prefab;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

/**
 * Prefab class of Voltraxis' effect icon UI.
 */
public final class EffectIconUIPrefab extends Prefab {

    @Override
    public GameObject instantiatePrefab() {

        var centerPivot = new Vector2(0.5, 0.5);
        var effectIconObject = GameObjectManager.instantiate("Effect icon")
                .addComponent(VoltraxisEffectIconUI.class)
                .addComponent(SpriteRenderer.class)
                .getGameObject();
        var effectRenderer = effectIconObject.getComponent(SpriteRenderer.class);
        effectRenderer.setPivot(centerPivot);
        effectRenderer.setRenderLayer(RenderLayer.UI_Middle);

        return effectIconObject;

    }

}