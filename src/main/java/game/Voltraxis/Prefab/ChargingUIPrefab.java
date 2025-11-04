package game.Voltraxis.Prefab;

import game.Voltraxis.VoltraxisChargingUI;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Layer.RenderLayer;
import org.Prefab.Prefab;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

/**
 * Prefab of Voltraxis' EX charging UI.
 */
public final class ChargingUIPrefab extends Prefab {

    private static final Vector2 CHARGING_UI_RENDER_SIZE = new Vector2(200.0, 10.0);
    private static final Vector2 CHARGING_UI_RENDER_POSITION = new Vector2(600.0, 100.0);

    @Override
    public GameObject instantiatePrefab() {

        var chargingUI = GameObjectManager.instantiate("VoltraxisChargingUI")
                .addComponent(VoltraxisChargingUI.class);
        var uiHolderObject = GameObjectManager.instantiate("UIHolder");
        uiHolderObject.setParent(chargingUI.getGameObject());
        uiHolderObject.getTransform().setGlobalPosition(CHARGING_UI_RENDER_POSITION);

        var centerPivot = new Vector2(0.5, 0.5);

        // Background
        var backgroundObject = GameObjectManager.instantiate("Background");
        var backgroundRenderer = backgroundObject.addComponent(SpriteRenderer.class);
        backgroundRenderer.setImage(ImageAsset.ImageIndex.Voltraxis_UI_Charging_Background.getImage());
        backgroundRenderer.setPivot(centerPivot);
        backgroundRenderer.setRenderLayer(RenderLayer.UI_Middle);
        backgroundRenderer.setSize(CHARGING_UI_RENDER_SIZE);
        backgroundObject.setParent(uiHolderObject);

        // Fill
        var fillObject = GameObjectManager.instantiate("Fill");
        var fillRenderer = fillObject.addComponent(SpriteRenderer.class);
        fillRenderer.setImage(ImageAsset.ImageIndex.Voltraxis_UI_Charging_Fill.getImage());
        fillRenderer.setPivot(centerPivot);
        fillRenderer.setRenderLayer(RenderLayer.UI_Middle);
        fillRenderer.setFillType(SpriteRenderer.FillType.Horizontal_LeftToRight);
        fillRenderer.setSize(CHARGING_UI_RENDER_SIZE);
        fillObject.setParent(uiHolderObject);

        // Outline
        var outlineObject = GameObjectManager.instantiate("Outline");
        var outlineRenderer = outlineObject.addComponent(SpriteRenderer.class);
        outlineRenderer.setImage(ImageAsset.ImageIndex.Voltraxis_UI_Charging_Outline.getImage());
        outlineRenderer.setPivot(centerPivot);
        outlineRenderer.setRenderLayer(RenderLayer.UI_Middle);
        outlineRenderer.setSize(CHARGING_UI_RENDER_SIZE);
        outlineObject.setParent(uiHolderObject);

        // Link component
        chargingUI.linkFillRenderer(fillRenderer);
        chargingUI.linkUIObject(uiHolderObject);

        return chargingUI.getGameObject();

    }

}