package game.Voltraxis.Prefab;

import game.Voltraxis.Object.PowerCore.*;
import org.Animation.AnimationClipData;
import org.Animation.SpriteAnimator;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Layer.RenderLayer;
import org.Physics.BoxCollider;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

/**
 * Prefab of Voltraxis' power core, containing both logical and visual objects.
 */
public final class PowerCorePrefab implements IVoltraxisPrefab {

    private static final Vector2 POWER_CORE_COLLIDER_SIZE = new Vector2(80.0, 80.0);
    private static final Vector2 POWER_CORE_UI_OFFSET = new Vector2(0.0, -75.0);
    private static final Vector2 POWER_CORE_HEALTH_BAR_RENDER_SIZE = new Vector2(146.0, 14.29);

    @Override
    public GameObject instantiatePrefab() {

        var centerPivot = new Vector2(0.5, 0.5);

        // Core object
        var powerCoreObject = GameObjectManager.instantiate("PowerCore")
                .addComponent(PowerCore.class)
                .addComponent(PowerCoreHealth.class)
                .getGameObject();

        // Collider
        var powerCoreCollider = powerCoreObject.addComponent(BoxCollider.class);
        powerCoreCollider.setLocalSize(POWER_CORE_COLLIDER_SIZE);

        // Visual object
        var powerCoreVisual = GameObjectManager.instantiate("PowerCoreVisual")
                .addComponent(PowerCoreVisual.class);
        powerCoreVisual.getGameObject().setParent(powerCoreObject);
        var powerCoreVisualAnimator = powerCoreVisual.getGameObject().addComponent(SpriteAnimator.class);

        // Link animation clips
        powerCoreVisualAnimator.addAnimationClip(AnimationClipData.Voltraxis_PowerCore_Idle_ChargingHigh);
        powerCoreVisualAnimator.addAnimationClip(AnimationClipData.Voltraxis_PowerCore_Idle_ChargingLow);
        powerCoreVisualAnimator.addAnimationClip(AnimationClipData.Voltraxis_PowerCore_Idle_ChargingMedium);
        powerCoreVisualAnimator.addAnimationClip(AnimationClipData.Voltraxis_PowerCore_Idle);

        // UI object
        var powerCoreUIObject = GameObjectManager.instantiate("PowerCoreUI");
        powerCoreUIObject.setParent(powerCoreObject);
        powerCoreUIObject.getTransform().setLocalPosition(POWER_CORE_UI_OFFSET);

        // Health bar object
        var healthBarObject = GameObjectManager.instantiate("HealthBar");
        var healthBar = healthBarObject.addComponent(PowerCoreHealthBar.class);
        healthBarObject.setParent(powerCoreUIObject);

        // Background
        var backgroundObject = GameObjectManager.instantiate("Background");
        var backgroundRenderer = backgroundObject.addComponent(SpriteRenderer.class);
        backgroundRenderer.setPivot(centerPivot);
        backgroundRenderer.setImage(ImageAsset.ImageIndex.Voltraxis_PowerCore_UI_HealthBar_Background.getImage());
        backgroundRenderer.setSize(POWER_CORE_HEALTH_BAR_RENDER_SIZE);
        backgroundRenderer.setRenderLayer(RenderLayer.UI);
        backgroundObject.setParent(healthBarObject);

        // Fill lost
        var fillLostObject = GameObjectManager.instantiate("FillLost");
        var fillLostRenderer = fillLostObject.addComponent(SpriteRenderer.class);
        fillLostRenderer.setPivot(centerPivot);
        fillLostRenderer.setImage(ImageAsset.ImageIndex.Voltraxis_PowerCore_UI_HealthBar_Lost.getImage());
        fillLostRenderer.setSize(POWER_CORE_HEALTH_BAR_RENDER_SIZE);
        fillLostRenderer.setRenderLayer(RenderLayer.UI);
        fillLostRenderer.setFillType(SpriteRenderer.FillType.Horizontal_LeftToRight);
        fillLostObject.setParent(healthBarObject);

        // Fill remain
        var fillRemainObject = GameObjectManager.instantiate("FillRemain");
        var fillRemainRenderer = fillRemainObject.addComponent(SpriteRenderer.class);
        fillRemainRenderer.setPivot(centerPivot);
        fillRemainRenderer.setImage(ImageAsset.ImageIndex.Voltraxis_PowerCore_UI_HealthBar_Remain.getImage());
        fillRemainRenderer.setSize(POWER_CORE_HEALTH_BAR_RENDER_SIZE);
        fillRemainRenderer.setRenderLayer(RenderLayer.UI);
        fillRemainRenderer.setFillType(SpriteRenderer.FillType.Horizontal_LeftToRight);
        fillRemainObject.setParent(healthBarObject);

        // Outline
        var outlineObject = GameObjectManager.instantiate("Outline");
        var outlineRenderer = outlineObject.addComponent(SpriteRenderer.class);
        outlineRenderer.setPivot(centerPivot);
        outlineRenderer.setImage(ImageAsset.ImageIndex.Voltraxis_PowerCore_UI_HealthBar_Outline.getImage());
        outlineRenderer.setSize(POWER_CORE_HEALTH_BAR_RENDER_SIZE);
        outlineRenderer.setRenderLayer(RenderLayer.UI);
        outlineObject.setParent(healthBarObject);

        // Link components
        healthBar.setFillLost(fillLostRenderer);
        healthBar.setFillRemain(fillRemainRenderer);
        healthBar.setPowerCore(powerCoreObject.getComponent(PowerCore.class));

        return powerCoreObject;

    }

}