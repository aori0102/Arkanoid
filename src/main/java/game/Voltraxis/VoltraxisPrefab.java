package game.Voltraxis;

import game.Voltraxis.Object.ElectricBall;
import game.Voltraxis.Object.PowerCore;
import game.Voltraxis.Object.PowerCoreHealthBar;
import javafx.scene.paint.Color;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Layer.RenderLayer;
import org.Physics.BoxCollider;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import org.Text.FontDataIndex;
import org.Text.TextHorizontalAlignment;
import org.Text.TextUI;
import org.Text.TextVerticalAlignment;
import utils.Vector2;

public final class VoltraxisPrefab {

    /// Boss name
    private static final Vector2 NAME_TEXT_OFFSET = new Vector2(0.0, -40.0);

    /// Health bar
    private static final Vector2 HEALTH_BAR_POSITION = new Vector2(600.0, 70.0);
    private static final double HEALTH_FONT_SIZE = 24.0;
    private static final Vector2 HEALTH_BAR_SIZE = new Vector2(670.33, 29.79);

    /// Groggy gauge
    private static final Vector2 GROGGY_GAUGE_POSITION = new Vector2(838.0, 100.0);
    private static final Vector2 GROGGY_GAUGE_SIZE = new Vector2(156.25, 13.93);

    /// Icon
    private static final Vector2 ICON_SIZE = new Vector2(30.58, 23.52);
    private static final Vector2 EFFECT_BAR_ANCHOR = new Vector2(280.0, 100.0);

    /// Boss position
    private static final Vector2 BOSS_POSITION = new Vector2(600.0, 300.0);
    private static final Vector2 BOSS_RENDER_SIZE = new Vector2(300.0, 300.0);
    private static final Vector2 BOSS_COLLIDER_SIZE = new Vector2(230.0, 230.0);

    /// Power core
    private static final Vector2 POWER_CORE_COLLIDER_SIZE = new Vector2(80.0, 80.0);
    private static final Vector2 POWER_CORE_RENDER_SIZE = new Vector2(128.0, 128.0);
    private static final Vector2 POWER_CORE_UI_OFFSET = new Vector2(0.0, -60.0);
    private static final Vector2 POWER_CORE_HEALTH_BAR_RENDER_SIZE = new Vector2(146.0, 14.29);

    public static GameObject instantiate() {

        var voltraxisObject = GameObjectManager.instantiate("Boss");

        // Create components
        var voltraxis = instantiateVoltraxis();
        var groggyGauge = instantiateGroggyGauge();
        var effectManager = instantiateEffectManager();
        var healthBar = instantiateHealthBar();
        var visual = instantiateVisual();

        // Link components
        voltraxis.attachVoltraxisEffectManager(effectManager);
        voltraxis.attachVoltraxisGroggyGauge(groggyGauge);
        groggyGauge.setVoltraxis(voltraxis);
        healthBar.setVoltraxis(voltraxis);
        visual.setVoltraxis(voltraxis);

        // Parent components
        voltraxis.getGameObject().setParent(voltraxisObject);
        groggyGauge.getGameObject().setParent(voltraxisObject);
        healthBar.getGameObject().setParent(voltraxisObject);
        visual.getGameObject().setParent(voltraxis.getGameObject());
        effectManager.getGameObject().setParent(voltraxisObject);

        // Starting position
        voltraxis.getTransform().setGlobalPosition(BOSS_POSITION);

        return voltraxisObject;

    }

    static ElectricBall instantiateElectricBall() {

        // Main object
        var electricBall = GameObjectManager.instantiate("Electric Ball");
        return electricBall.addComponent(ElectricBall.class);

    }

    static VoltraxisEffectIcon instantiateVoltraxisEffectIcon(VoltraxisData.EffectIndex index) {

        var centerPivot = new Vector2(0.5, 0.5);
        var effectObject = GameObjectManager.instantiate("Effect icon [" + index + "]");
        var effectIconObject = GameObjectManager.instantiate("Visual");
        effectIconObject.setParent(effectObject);
        var effectIcon = effectIconObject.addComponent(VoltraxisEffectIcon.class);
        var effectRenderer = effectIconObject.addComponent(SpriteRenderer.class);
        effectRenderer.setPivot(centerPivot);
        effectRenderer.setImage(index.getImageIndex().getImage());
        effectRenderer.setSize(ICON_SIZE);
        effectRenderer.setRenderLayer(RenderLayer.UI);
        effectIcon.setVisualRenderer(effectRenderer);

        return effectObject.addComponent(VoltraxisEffectIcon.class);

    }

    static PowerCore instantiatePowerCore() {

        var centerPivot = new Vector2(0.5, 0.5);

        // Core object
        var powerCoreObject = GameObjectManager.instantiate("PowerCore");
        var powerCore = powerCoreObject.addComponent(PowerCore.class);
        var powerCoreCollider = powerCoreObject.addComponent(BoxCollider.class);
        powerCoreCollider.setLocalSize(POWER_CORE_COLLIDER_SIZE);

        // Visual object
        var powerCoreVisualObject = GameObjectManager.instantiate("Visual");
        powerCoreVisualObject.setParent(powerCoreObject);
        var powerCoreVisualRenderer = powerCoreVisualObject.addComponent(SpriteRenderer.class);
        powerCoreVisualRenderer.setPivot(centerPivot);
        powerCoreVisualRenderer.setImage(ImageAsset.ImageIndex.Voltraxis_PowerCore.getImage());
        powerCoreVisualRenderer.setSize(POWER_CORE_RENDER_SIZE);

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
        healthBar.setPowerCore(powerCore);

        return powerCore;

    }

    private static Voltraxis instantiateVoltraxis() {

        // Main object
        var voltraxisObject = GameObjectManager.instantiate("Voltraxis");
        var voltraxis = voltraxisObject.addComponent(Voltraxis.class);
        voltraxisObject.addComponent(BoxCollider.class).setLocalSize(BOSS_COLLIDER_SIZE);
        return voltraxis;

    }

    private static VoltraxisVisual instantiateVisual() {

        var visualObject = GameObjectManager.instantiate("Visual");
        var visual = visualObject.addComponent(VoltraxisVisual.class);
        visual.addComponent(SpriteRenderer.class).setSize(BOSS_RENDER_SIZE);
        return visual;

    }

    private static VoltraxisEffectManager instantiateEffectManager() {

        var effectManagerObject = GameObjectManager.instantiate("Effect Manager");
        effectManagerObject.getTransform().setGlobalPosition(EFFECT_BAR_ANCHOR);

        return effectManagerObject.addComponent(VoltraxisEffectManager.class);

    }

    private static VoltraxisGroggyGauge instantiateGroggyGauge() {

        var centerPivot = new Vector2(0.5, 0.5);

        // Groggy
        var groggyObject = GameObjectManager.instantiate("Groggy Gauge");
        var groggyGauge = groggyObject.addComponent(VoltraxisGroggyGauge.class);
        groggyObject.getTransform().setGlobalPosition(GROGGY_GAUGE_POSITION);

        // Background
        var backgroundObject = GameObjectManager.instantiate("Background");
        var backgroundRenderer = backgroundObject.addComponent(SpriteRenderer.class);
        backgroundRenderer.setImage(ImageAsset.ImageIndex.Voltraxis_UI_Groggy_Background.getImage());
        backgroundRenderer.setPivot(centerPivot);
        backgroundRenderer.setRenderLayer(RenderLayer.UI);
        backgroundRenderer.setSize(GROGGY_GAUGE_SIZE);
        backgroundObject.setParent(groggyObject);

        // Fill
        var fillObject = GameObjectManager.instantiate("Fill");
        var fillRenderer = fillObject.addComponent(SpriteRenderer.class);
        fillRenderer.setImage(ImageAsset.ImageIndex.Voltraxis_UI_Groggy_Fill.getImage());
        fillRenderer.setPivot(centerPivot);
        fillRenderer.setRenderLayer(RenderLayer.UI);
        fillRenderer.setFillType(SpriteRenderer.FillType.Horizontal_LeftToRight);
        fillRenderer.setSize(GROGGY_GAUGE_SIZE);
        fillObject.setParent(groggyObject);

        // Outline
        var outlineObject = GameObjectManager.instantiate("Outline");
        var outlineRenderer = outlineObject.addComponent(SpriteRenderer.class);
        outlineRenderer.setImage(ImageAsset.ImageIndex.Voltraxis_UI_Groggy_Outline.getImage());
        outlineRenderer.setPivot(centerPivot);
        outlineRenderer.setRenderLayer(RenderLayer.UI);
        outlineRenderer.setSize(GROGGY_GAUGE_SIZE);
        outlineObject.setParent(groggyObject);

        // Link component
        groggyGauge.setFill(fillRenderer);

        return groggyGauge;

    }

    private static VoltraxisHealthBar instantiateHealthBar() {

        var centerPivot = new Vector2(0.5, 0.5);

        // Holder
        var healthBarObject = GameObjectManager.instantiate("Health Bar");
        healthBarObject.getTransform().setGlobalPosition(HEALTH_BAR_POSITION);
        var healthBar = healthBarObject.addComponent(VoltraxisHealthBar.class);

        // Background
        var backgroundObject = GameObjectManager.instantiate("Background");
        var backgroundSpriteRenderer = backgroundObject.addComponent(SpriteRenderer.class);
        backgroundSpriteRenderer.setImage(ImageAsset.ImageIndex.Voltraxis_UI_HealthBar_Background.getImage());
        backgroundSpriteRenderer.setPivot(centerPivot);
        backgroundSpriteRenderer.setRenderLayer(RenderLayer.UI);
        backgroundSpriteRenderer.setSize(HEALTH_BAR_SIZE);
        backgroundObject.setParent(healthBarObject);

        // Fill lost
        var lostObject = GameObjectManager.instantiate("Lost Health");
        var lostSpriteRenderer = lostObject.addComponent(SpriteRenderer.class);
        lostSpriteRenderer.setImage(ImageAsset.ImageIndex.Voltraxis_UI_HealthBar_Lost.getImage());
        lostSpriteRenderer.setPivot(centerPivot);
        lostSpriteRenderer.setRenderLayer(RenderLayer.UI);
        lostSpriteRenderer.setSize(HEALTH_BAR_SIZE);
        lostObject.setParent(healthBarObject);

        // Fill remain
        var remainObject = GameObjectManager.instantiate("Remain Health");
        var remainSpriteRenderer = remainObject.addComponent(SpriteRenderer.class);
        remainSpriteRenderer.setImage(ImageAsset.ImageIndex.Voltraxis_UI_HealthBar_Remain.getImage());
        remainSpriteRenderer.setPivot(centerPivot);
        remainSpriteRenderer.setRenderLayer(RenderLayer.UI);
        remainSpriteRenderer.setSize(HEALTH_BAR_SIZE);
        remainObject.setParent(healthBarObject);

        // Outline
        var outlineObject = GameObjectManager.instantiate("Outline");
        var outlineSpriteRenderer = outlineObject.addComponent(SpriteRenderer.class);
        outlineSpriteRenderer.setImage(ImageAsset.ImageIndex.Voltraxis_UI_HealthBar_Outline.getImage());
        outlineSpriteRenderer.setPivot(centerPivot);
        outlineSpriteRenderer.setRenderLayer(RenderLayer.UI);
        outlineSpriteRenderer.setSize(HEALTH_BAR_SIZE);
        outlineObject.setParent(healthBarObject);

        // Health text
        var healthTextObject = GameObjectManager.instantiate("Health");
        var healthText = healthTextObject.addComponent(TextUI.class);
        healthText.setTextColor(Color.WHITE);
        healthText.setFont(FontDataIndex.Jersey_25);
        healthText.setHorizontalAlignment(TextHorizontalAlignment.Center);
        healthText.setVerticalAlignment(TextVerticalAlignment.Middle);
        healthText.setFontSize(HEALTH_FONT_SIZE);
        healthText.setRenderLayer(RenderLayer.UI);
        healthTextObject.setParent(healthBarObject);

        // Boss name text
        var nameTextObject = GameObjectManager.instantiate("Name");
        var nameText = nameTextObject.addComponent(TextUI.class);
        nameText.setText("Voltraxis");
        nameText.setFont(FontDataIndex.Jersey_25);
        nameText.setHorizontalAlignment(TextHorizontalAlignment.Center);
        nameText.setVerticalAlignment(TextVerticalAlignment.Middle);
        nameText.setFontSize(40);
        nameText.setRenderLayer(RenderLayer.UI);
        nameText.getTransform().setLocalPosition(NAME_TEXT_OFFSET);
        nameTextObject.setParent(healthBarObject);

        // Link serialize field
        healthBar.setHealthText(healthText);
        healthBar.setHealthLostImage(lostSpriteRenderer);
        healthBar.setHealthRemainImage(remainSpriteRenderer);

        return healthBar;

    }

}