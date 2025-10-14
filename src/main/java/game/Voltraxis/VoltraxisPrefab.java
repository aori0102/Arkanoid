package game.Voltraxis;

import javafx.scene.paint.Color;
import org.*;
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

    public static GameObject instantiate() {
        var voltraxis = instantiateVoltraxis();
        var voltraxisObject = voltraxis.getGameObject();
        var voltraxisBossObject = GameObjectManager.instantiate("Voltraxis");
        instantiateUI(voltraxis).setParent(voltraxisBossObject);
        instantiateVisual(voltraxis).getGameObject().setParent(voltraxisObject);
        voltraxisObject.getTransform().setGlobalPosition(BOSS_POSITION);
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

    private static Voltraxis instantiateVoltraxis() {

        // Main object
        var voltraxisObject = GameObjectManager.instantiate("Voltraxis");
        return voltraxisObject.addComponent(Voltraxis.class);

    }

    private static VoltraxisVisual instantiateVisual(Voltraxis voltraxis) {

        var visualObject = GameObjectManager.instantiate("Visual");
        var visual = visualObject.addComponent(VoltraxisVisual.class);
        visual.setVoltraxis(voltraxis);
        return visual;

    }

    private static GameObject instantiateUI(Voltraxis voltraxis) {
        var uiObject = GameObjectManager.instantiate("Voltraxis UI");
        instantiateHealthBar(voltraxis).setParent(uiObject);
        instantiateGroggyGauge(voltraxis).setParent(uiObject);
        instantiateEffectManager(voltraxis).setParent(uiObject);
        return uiObject;
    }

    private static GameObject instantiateEffectManager(Voltraxis voltraxis) {

        var effectManagerObject = GameObjectManager.instantiate("Effect Manager");
        effectManagerObject.getTransform().setGlobalPosition(EFFECT_BAR_ANCHOR);
        var effectManager = effectManagerObject.addComponent(VoltraxisEffectManager.class);
        effectManager.setVoltraxis(voltraxis);

        return effectManagerObject;

    }

    private static GameObject instantiateGroggyGauge(Voltraxis voltraxis) {

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
        groggyGauge.setVoltraxis(voltraxis);

        return groggyObject;

    }

    private static GameObject instantiateHealthBar(Voltraxis voltraxis) {

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
        remainSpriteRenderer.setImage(ImageAsset.ImageIndex.Remain.getImage());
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
        healthBar.setVoltraxis(voltraxis);

        return healthBarObject;

    }

}