package game.Voltraxis.Prefab;

import game.Voltraxis.VoltraxisHealthBar;
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

/**
 * Prefab of Voltraxis' health bar UI.
 */
public final class HealthBarUIPrefab extends Prefab {

    private static final double HEALTH_FONT_SIZE = 24.0;
    private static final double NAME_FONT_SIZE = 40.0;
    private static final String BOSS_NAME_LABEL = "Voltraxis";
    private static final Vector2 NAME_TEXT_OFFSET = new Vector2(0.0, -40.0);
    private static final Vector2 HEALTH_BAR_POSITION = new Vector2(600.0, 70.0);
    private static final Vector2 HEALTH_BAR_SIZE = new Vector2(670.33, 29.79);

    @Override
    public GameObject instantiatePrefab() {

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
        nameText.setText(BOSS_NAME_LABEL);
        nameText.setFont(FontDataIndex.Jersey_25);
        nameText.setHorizontalAlignment(TextHorizontalAlignment.Center);
        nameText.setVerticalAlignment(TextVerticalAlignment.Middle);
        nameText.setFontSize(NAME_FONT_SIZE);
        nameText.setRenderLayer(RenderLayer.UI);
        nameText.getTransform().setLocalPosition(NAME_TEXT_OFFSET);
        nameTextObject.setParent(healthBarObject);

        // Link serialize field
        healthBar.setHealthText(healthText);
        healthBar.setHealthLostImage(lostSpriteRenderer);
        healthBar.setHealthRemainImage(remainSpriteRenderer);

        return healthBarObject;

    }

}