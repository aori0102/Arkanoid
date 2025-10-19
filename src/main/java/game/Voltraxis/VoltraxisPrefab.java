package game.Voltraxis;

import game.Voltraxis.Object.ElectricBall;
import game.Voltraxis.Object.PowerCore;
import game.Voltraxis.Object.PowerCoreHealthBar;
import game.Voltraxis.Object.PowerCoreVisual;
import javafx.scene.paint.Color;
import org.Animation.AnimationClipData;
import org.Animation.SpriteAnimator;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Layer.Layer;
import org.Layer.RenderLayer;
import org.Physics.BoxCollider;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import org.Text.FontDataIndex;
import org.Text.TextHorizontalAlignment;
import org.Text.TextUI;
import org.Text.TextVerticalAlignment;
import utils.Vector2;

/**
 * Central class for generate Voltraxis' prefabs.
 */
public final class VoltraxisPrefab {

    /// Boss name
    private static final Vector2 NAME_TEXT_OFFSET = new Vector2(0.0, -40.0);

    /// Health bar
    private static final Vector2 HEALTH_BAR_POSITION = new Vector2(600.0, 70.0);
    private static final double HEALTH_FONT_SIZE = 24.0;
    private static final Vector2 HEALTH_BAR_SIZE = new Vector2(670.33, 29.79);

    /// Groggy gauge
    private static final Vector2 GROGGY_GAUGE_UI_POSITION = new Vector2(838.0, 100.0);
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
    private static final Vector2 POWER_CORE_UI_OFFSET = new Vector2(0.0, -60.0);
    private static final Vector2 POWER_CORE_HEALTH_BAR_RENDER_SIZE = new Vector2(146.0, 14.29);

    /// Electric ball
    private static final Vector2 BALL_SIZE = new Vector2(64.0, 64.0);

    /**
     * Instantiate the entirety of Voltraxis.
     *
     * @return The game object for the entirety of Voltraxis.
     */
    public static GameObject instantiate() {

        var voltraxisObject = GameObjectManager.instantiate("Boss");

        // Create components
        var voltraxis = instantiateVoltraxis();
        var groggy = voltraxis.addComponent(VoltraxisGroggy.class);
        var groggyUI = instantiateGroggyUI();
        var effectManager = instantiateEffectManager();
        var healthBar = instantiateHealthBar();
        var visual = instantiateVisual();

        // Link components
        voltraxis.attachVoltraxisEffectManager(effectManager);
        voltraxis.attachVoltraxisGroggyGauge(groggy);
        groggy.attachVoltraxisGroggyUI(groggyUI);
        groggy.setVoltraxis(voltraxis);
        healthBar.setVoltraxis(voltraxis);
        visual.setVoltraxis(voltraxis);

        // Parent components
        voltraxis.getGameObject().setParent(voltraxisObject);
        groggyUI.getGameObject().setParent(voltraxisObject);
        healthBar.getGameObject().setParent(voltraxisObject);
        visual.getGameObject().setParent(voltraxis.getGameObject());
        effectManager.getGameObject().setParent(voltraxisObject);

        // Starting position
        voltraxis.getTransform().setGlobalPosition(BOSS_POSITION);

        return voltraxisObject;

    }

    /**
     * Instantiate Voltraxis' {@link ElectricBall}. The resulting
     * object includes its {@link SpriteRenderer} and
     * {@link BoxCollider} with preset settings.
     *
     * @return Voltraxis' electric ball.
     */
    static ElectricBall instantiateElectricBall() {

        // Main object
        var electricBallObject = GameObjectManager.instantiate("Electric Ball");

        var boxCollider = electricBallObject.addComponent(BoxCollider.class);
        boxCollider.setLocalSize(BALL_SIZE);
        boxCollider.setIncludeLayer(Layer.Player.getUnderlyingValue());

        var spriteRenderer = electricBallObject.addComponent(SpriteRenderer.class);
        spriteRenderer.setImage(ImageAsset.ImageIndex.Voltraxis_ElectricBall.getImage());
        spriteRenderer.setSize(BALL_SIZE);
        spriteRenderer.setPivot(new Vector2(0.5, 0.5));

        return electricBallObject.addComponent(ElectricBall.class);

    }

    /**
     * Instantiate an effect icon UI corresponding to the
     * {@code index} that is provided. The resulting object
     * has a {@link SpriteRenderer} and {@link VoltraxisEffectIcon}
     * with preset settings.
     *
     * @param index The effect index.
     * @return The component {@link VoltraxisEffectIcon} attached
     * to the icon UI's game object.
     */
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

    /**
     * Instantiate Voltraxis' power core. The resulting object
     * includes both logical and visual object of the power core,
     * with each already has the preset components and settings.
     *
     * @return The game object including both power core's logical
     * brain and visual.
     */
    static PowerCore instantiatePowerCore() {

        var centerPivot = new Vector2(0.5, 0.5);

        // Core object
        var powerCoreObject = GameObjectManager.instantiate("PowerCore");
        var powerCore = powerCoreObject.addComponent(PowerCore.class);
        var powerCoreCollider = powerCoreObject.addComponent(BoxCollider.class);
        powerCoreCollider.setLocalSize(POWER_CORE_COLLIDER_SIZE);

        // Visual object
        var powerCoreVisualObject = GameObjectManager.instantiate("PowerCoreVisual");
        powerCoreVisualObject.setParent(powerCoreObject);
        var powerCoreVisual = powerCoreVisualObject.addComponent(PowerCoreVisual.class);
        var powerCoreVisualRenderer = powerCoreVisualObject.addComponent(SpriteRenderer.class);
        powerCoreVisualRenderer.setPivot(centerPivot);
        var powerCoreVisualAnimator = powerCoreVisualObject.addComponent(SpriteAnimator.class);
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
        powerCore.setPowerCoreVisual(powerCoreVisual);
        healthBar.setFillLost(fillLostRenderer);
        healthBar.setFillRemain(fillRemainRenderer);
        healthBar.setPowerCore(powerCore);

        return powerCore;

    }

    /**
     * Instantiate the base boss object with central class
     * {@link Voltraxis}. This object includes the component
     * {@link BoxCollider} with preset settings.
     *
     * @return The central class {@link Voltraxis} attached
     * to the base object.
     */
    private static Voltraxis instantiateVoltraxis() {

        // Main object
        var voltraxisObject = GameObjectManager.instantiate("Voltraxis");
        var voltraxis = voltraxisObject.addComponent(Voltraxis.class);
        var voltraxisPowerCoreManager = voltraxisObject.addComponent(VoltraxisPowerCoreManager.class);
        voltraxis.attachVoltraxisPowerCoreManager(voltraxisPowerCoreManager);
        voltraxisObject.addComponent(BoxCollider.class).setLocalSize(BOSS_COLLIDER_SIZE);
        return voltraxis;

    }

    /**
     * Instantiate the visual object of voltraxis.
     *
     * @return The visual class {@link VoltraxisVisual}
     * of Voltraxis.
     */
    private static VoltraxisVisual instantiateVisual() {

        var visualObject = GameObjectManager.instantiate("VoltraxisVisual");
        var visual = visualObject.addComponent(VoltraxisVisual.class);
        var animator = visualObject.addComponent(SpriteAnimator.class);
        animator.addAnimationClip(AnimationClipData.Voltraxis_Idle);
        var renderer = visualObject.addComponent(SpriteRenderer.class);
        renderer.setPivot(new Vector2(0.5, 0.5));
        return visual;

    }

    /**
     * Instantiate the base object of {@link VoltraxisEffectManager}.
     *
     * @return The component {@link VoltraxisEffectManager} attached
     * to its base object.
     */
    private static VoltraxisEffectManager instantiateEffectManager() {

        var effectManagerObject = GameObjectManager.instantiate("Effect Manager");
        effectManagerObject.getTransform().setGlobalPosition(EFFECT_BAR_ANCHOR);

        return effectManagerObject.addComponent(VoltraxisEffectManager.class);

    }

    /**
     * Instantiate the base object of {@link VoltraxisGroggy}.
     *
     * @return The component {@link VoltraxisGroggy} attached to
     * its base object.
     */
    private static VoltraxisGroggyUI instantiateGroggyUI() {

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
        backgroundRenderer.setRenderLayer(RenderLayer.UI);
        backgroundRenderer.setSize(GROGGY_GAUGE_SIZE);
        backgroundObject.setParent(groggyUIObject);

        // Fill
        var fillObject = GameObjectManager.instantiate("Fill");
        var fillRenderer = fillObject.addComponent(SpriteRenderer.class);
        fillRenderer.setImage(ImageAsset.ImageIndex.Voltraxis_UI_Groggy_Fill.getImage());
        fillRenderer.setPivot(centerPivot);
        fillRenderer.setRenderLayer(RenderLayer.UI);
        fillRenderer.setFillType(SpriteRenderer.FillType.Horizontal_LeftToRight);
        fillRenderer.setSize(GROGGY_GAUGE_SIZE);
        fillObject.setParent(groggyUIObject);

        // Outline
        var outlineObject = GameObjectManager.instantiate("Outline");
        var outlineRenderer = outlineObject.addComponent(SpriteRenderer.class);
        outlineRenderer.setImage(ImageAsset.ImageIndex.Voltraxis_UI_Groggy_Outline.getImage());
        outlineRenderer.setPivot(centerPivot);
        outlineRenderer.setRenderLayer(RenderLayer.UI);
        outlineRenderer.setSize(GROGGY_GAUGE_SIZE);
        outlineObject.setParent(groggyUIObject);

        // Link component
        groggyUI.attachFillRenderer(fillRenderer);

        return groggyUI;

    }

    /**
     * Instantiate the base health bar UI object.
     *
     * @return The component {@link VoltraxisHealthBar} attached to
     * the health bar UI object.
     */
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