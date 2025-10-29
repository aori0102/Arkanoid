package game.Voltraxis.Prefab;

import game.Voltraxis.VoltraxisData;
import game.Voltraxis.VoltraxisEffectIconUI;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Layer.RenderLayer;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

/**
 * Prefab class of Voltraxis' effect icon UI. This class instantiates icon object based
 * on {@link game.Voltraxis.VoltraxisData.EffectIndex} specified when creating this prefab
 * object with the constructor {@link #EffectIconUIPrefab(VoltraxisData.EffectIndex)}.
 */
public final class EffectIconUIPrefab implements IVoltraxisPrefab {

    private static final Vector2 ICON_SIZE = new Vector2(30.58, 23.52);
    private static final Vector2 EFFECT_BAR_ANCHOR = new Vector2(280.0, 100.0);

    private final VoltraxisData.EffectIndex effectIndex;

    @Override
    public GameObject instantiatePrefab() {

        var centerPivot = new Vector2(0.5, 0.5);
        var effect = GameObjectManager.instantiate("Effect icon [" + effectIndex + "]")
                .addComponent(VoltraxisEffectIconUI.class);
        var effectIconObject = GameObjectManager.instantiate("Visual");
        effectIconObject.setParent(effect.getGameObject());
        var effectIcon = effectIconObject.addComponent(VoltraxisEffectIconUI.class);
        var effectRenderer = effectIconObject.addComponent(SpriteRenderer.class);
        effectRenderer.setPivot(centerPivot);
        effectRenderer.setImage(effectIndex.getImageIndex().getImage());
        effectRenderer.setSize(ICON_SIZE);
        effectRenderer.setRenderLayer(RenderLayer.UI);
        effectIcon.setVisualRenderer(effectRenderer);

        return effect.getGameObject();

    }

    public EffectIconUIPrefab(VoltraxisData.EffectIndex effectIndex) {
        this.effectIndex = effectIndex;
    }

}