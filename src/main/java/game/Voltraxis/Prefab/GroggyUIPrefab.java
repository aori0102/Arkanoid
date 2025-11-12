package game.Voltraxis.Prefab;

import game.Voltraxis.VoltraxisGroggyUI;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Layer.RenderLayer;
import org.Prefab.Prefab;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

/**
 * Prefab of Voltraxis' groggy gauge UI.
 */
public final class GroggyUIPrefab extends Prefab {

    private static final Vector2 GROGGY_GAUGE_UI_POSITION = new Vector2(838.0, 100.0);
    private static final Vector2 GROGGY_GAUGE_SIZE = new Vector2(156.25, 13.93);

    @Override
    public GameObject instantiatePrefab() {

        var centerPivot = new Vector2(0.5, 0.5);

        // Groggy
        var groggyUIObject = GameObjectManager.instantiate("GroggyGaugeUI");
        var groggyUI = groggyUIObject.addComponent(VoltraxisGroggyUI.class);
        groggyUIObject.getTransform().setGlobalPosition(GROGGY_GAUGE_UI_POSITION);

        // Background
        var backgroundObject = GameObjectManager.instantiate("Background");
        var backgroundRenderer = backgroundObject.addComponent(SpriteRenderer.class);
        backgroundRenderer.setImage(ImageAsset.ImageIndex.Voltraxis_UI_Groggy_Background.getImage());
        backgroundRenderer.setPivot(centerPivot);
        backgroundRenderer.setRenderLayer(RenderLayer.UI_3);
        backgroundRenderer.setSize(GROGGY_GAUGE_SIZE);
        backgroundObject.setParent(groggyUIObject);

        // Fill
        var fillObject = GameObjectManager.instantiate("Fill");
        var fillRenderer = fillObject.addComponent(SpriteRenderer.class);
        fillRenderer.setImage(ImageAsset.ImageIndex.Voltraxis_UI_Groggy_Fill.getImage());
        fillRenderer.setPivot(centerPivot);
        fillRenderer.setRenderLayer(RenderLayer.UI_3);
        fillRenderer.setFillType(SpriteRenderer.FillType.Horizontal_LeftToRight);
        fillRenderer.setSize(GROGGY_GAUGE_SIZE);
        fillObject.setParent(groggyUIObject);

        // Outline
        var outlineObject = GameObjectManager.instantiate("Outline");
        var outlineRenderer = outlineObject.addComponent(SpriteRenderer.class);
        outlineRenderer.setImage(ImageAsset.ImageIndex.Voltraxis_UI_Groggy_Outline.getImage());
        outlineRenderer.setPivot(centerPivot);
        outlineRenderer.setRenderLayer(RenderLayer.UI_3);
        outlineRenderer.setSize(GROGGY_GAUGE_SIZE);
        outlineObject.setParent(groggyUIObject);

        // Link component
        groggyUI.attachFillRenderer(fillRenderer);

        return groggyUIObject;

    }

}