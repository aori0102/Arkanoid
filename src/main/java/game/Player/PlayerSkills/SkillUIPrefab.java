package game.Player.PlayerSkills;

import javafx.scene.paint.Color;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Layer.RenderLayer;
import org.Prefab.Prefab;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import org.Text.FontDataIndex;
import org.Text.TextHorizontalAlignment;
import org.Text.TextUI;
import org.Text.TextVerticalAlignment;
import utils.Vector2;

public final class SkillUIPrefab extends Prefab {

    private static final Vector2 TOOLTIP_OFFSET = new Vector2(24.0, 24.0);

    private static final double TOOLTIP_TEXT_SIZE = 16.0;
    private static final Color TOOLTIP_TEXT_COLOR = Color.web("#FFFFFF");

    private static final double CHARGE_TEXT_SIZE = 16.0;
    private static final Color CHARGE_TEXT_COLOR = Color.web("#FFFFFF");
    private static final Vector2 CHARGE_TEXT_OFFSET = new Vector2(0.0, 30.0);

    @Override
    public GameObject instantiatePrefab() {

        var centerPivot = new Vector2(0.5, 0.5);
        var skillUIObject = GameObjectManager.instantiate("SkillUI");
        var skillUI = skillUIObject.addComponent(SkillUI.class);

        // Skill background
        var skillBackgroundRenderer = GameObjectManager.instantiate("SkillBackground")
                .addComponent(SpriteRenderer.class);
        skillBackgroundRenderer.setImage(ImageAsset.ImageIndex.Player_UI_Skill_SkillBackground.getImage());
        skillBackgroundRenderer.setPivot(centerPivot);
        skillBackgroundRenderer.setRenderLayer(RenderLayer.UI_Top);
        skillBackgroundRenderer.getGameObject().setParent(skillUIObject);

        // Skill icon
        var skillIconRenderer = GameObjectManager.instantiate("SkillIcon")
                .addComponent(SpriteRenderer.class);
        skillIconRenderer.setPivot(centerPivot);
        skillIconRenderer.setRenderLayer(RenderLayer.UI_Top);
        skillIconRenderer.getGameObject().setParent(skillUIObject);

        // Not ready overlay
        var notReadyOverlayRenderer = GameObjectManager.instantiate("NotReadyOverlay")
                .addComponent(SpriteRenderer.class);
        notReadyOverlayRenderer.setImage(ImageAsset.ImageIndex.Player_UI_Skill_NotReadyOverlay.getImage());
        notReadyOverlayRenderer.setPivot(centerPivot);
        notReadyOverlayRenderer.setRenderLayer(RenderLayer.UI_Top);
        notReadyOverlayRenderer.getGameObject().setParent(skillUIObject);

        // Ready overlay
        var readyOverlayRenderer = GameObjectManager.instantiate("ReadyOverlay")
                .addComponent(SpriteRenderer.class);
        readyOverlayRenderer.setImage(ImageAsset.ImageIndex.Player_UI_Skill_ReadyOverlay.getImage());
        readyOverlayRenderer.setPivot(centerPivot);
        readyOverlayRenderer.setRenderLayer(RenderLayer.UI_Top);
        readyOverlayRenderer.getGameObject().setParent(skillUIObject);

        // Charging ring
        var chargingRingRenderer = GameObjectManager.instantiate("ChargingRing")
                .addComponent(SpriteRenderer.class);
        chargingRingRenderer.setImage(ImageAsset.ImageIndex.Player_UI_Skill_ChargingRing.getImage());
        chargingRingRenderer.setPivot(centerPivot);
        chargingRingRenderer.setFillType(SpriteRenderer.FillType.Radial_CounterClockwise);
        chargingRingRenderer.setRadialStartPoint(SpriteRenderer.RadialStartPoint.Top);
        chargingRingRenderer.setRenderLayer(RenderLayer.UI_Top);
        chargingRingRenderer.getGameObject().setParent(skillUIObject);

        // Tooltip
        var tooltipObject = GameObjectManager.instantiate("Tooltip");
        tooltipObject.setParent(skillUIObject);
        tooltipObject.getTransform().setLocalPosition(TOOLTIP_OFFSET);

        // Tooltip background
        var tooltipBackgroundRenderer = GameObjectManager.instantiate("Background")
                .addComponent(SpriteRenderer.class);
        tooltipBackgroundRenderer.setImage(ImageAsset.ImageIndex.Player_UI_Skill_TooltipBackground.getImage());
        tooltipBackgroundRenderer.setPivot(centerPivot);
        tooltipBackgroundRenderer.setRenderLayer(RenderLayer.UI_Top);
        tooltipBackgroundRenderer.getGameObject().setParent(tooltipObject);

        // Tooltip key
        var toolTipKeyText = GameObjectManager.instantiate("TooltipKey")
                .addComponent(TextUI.class);
        toolTipKeyText.setFontSize(TOOLTIP_TEXT_SIZE);
        toolTipKeyText.setSolidFill(TOOLTIP_TEXT_COLOR);
        toolTipKeyText.setVerticalAlignment(TextVerticalAlignment.Middle);
        toolTipKeyText.setHorizontalAlignment(TextHorizontalAlignment.Center);
        toolTipKeyText.setRenderLayer(RenderLayer.UI_Top);
        toolTipKeyText.setFont(FontDataIndex.Jersey_25);
        toolTipKeyText.getGameObject().setParent(tooltipObject);

        // Charge
        var chargeText = GameObjectManager.instantiate("Charge")
                .addComponent(TextUI.class);
        chargeText.setFontSize(CHARGE_TEXT_SIZE);
        chargeText.setSolidFill(CHARGE_TEXT_COLOR);
        chargeText.setVerticalAlignment(TextVerticalAlignment.Middle);
        chargeText.setHorizontalAlignment(TextHorizontalAlignment.Center);
        chargeText.setRenderLayer(RenderLayer.UI_Top);
        chargeText.setFont(FontDataIndex.Jersey_25);
        chargeText.getGameObject().setParent(skillUIObject);
        chargeText.getTransform().setLocalPosition(CHARGE_TEXT_OFFSET);

        // Charging nob
        var chargingNobRenderer = GameObjectManager.instantiate("ChargingNob")
                .addComponent(SpriteRenderer.class);
        chargingNobRenderer.setImage(ImageAsset.ImageIndex.Player_UI_Skill_ChargingIndicatorNob.getImage());
        chargingNobRenderer.setPivot(centerPivot);
        chargingNobRenderer.setRenderLayer(RenderLayer.UI_Top);
        chargingNobRenderer.getGameObject().setParent(skillUIObject);

        // Link component
        skillUI.linkChargeLabel(chargeText);
        skillUI.linkSkillIcon(skillIconRenderer);
        skillUI.linkChargingRing(chargingRingRenderer);
        skillUI.linkChargingNob(chargingNobRenderer);
        skillUI.linkReadyOverlay(readyOverlayRenderer);
        skillUI.linkNotReadyOverlay(notReadyOverlayRenderer);
        skillUI.linkKeybindLabel(toolTipKeyText);

        return skillUIObject;

    }
}
