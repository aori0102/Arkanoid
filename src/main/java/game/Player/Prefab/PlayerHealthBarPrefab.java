package game.Player.Prefab;

import game.Player.PlayerHealthUI;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Layer.RenderLayer;
import org.Prefab.Prefab;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

/**
 * Prefab of the player's health bar UI. Attached with {@link PlayerHealthUI}
 * with multiple UI elements as children.
 */
public class PlayerHealthBarPrefab extends Prefab {

    private static final Vector2 HEALTH_BAR_POSITION = new Vector2(600.0, 750.0);
    private static final Vector2 HEALTH_BAR_SIZE = new Vector2(246.0, 16.0);
    public static final Vector2 LIFE_BAR_SIZE = new Vector2(76.0, 3.0);
    /**
     * Offset between lives bar and health bar.
     */
    private static final Vector2 LIVES_RENDER_OFFSET = new Vector2(0.0, 12.0);
    /**
     * Offset between each life bar.
     */
    private static final Vector2 LIFE_RENDERER_OFFSET = new Vector2(79.0, 0.0);

    @Override
    public GameObject instantiatePrefab() {

        var centerPivot = new Vector2(0.5, 0.5);
        var healthBar = GameObjectManager.instantiate("HealthBarUI")
                .addComponent(PlayerHealthUI.class);
        healthBar.getTransform().setGlobalPosition(HEALTH_BAR_POSITION);

        // Background
        var backgroundObject = GameObjectManager.instantiate("Background");
        var backgroundRenderer = backgroundObject.addComponent(SpriteRenderer.class);
        backgroundRenderer.setImage(ImageAsset.ImageIndex.Player_UI_HealthBar_Background.getImage());
        backgroundRenderer.setPivot(centerPivot);
        backgroundRenderer.setSize(HEALTH_BAR_SIZE);
        backgroundRenderer.setRenderLayer(RenderLayer.UI_3);
        backgroundObject.setParent(healthBar.getGameObject());

        // Fill
        var fillObject = GameObjectManager.instantiate("Fill");
        var fillRenderer = fillObject.addComponent(SpriteRenderer.class);
        fillRenderer.setImage(ImageAsset.ImageIndex.Player_UI_HealthBar_Fill.getImage());
        fillRenderer.setPivot(centerPivot);
        fillRenderer.setSize(HEALTH_BAR_SIZE);
        fillRenderer.setRenderLayer(RenderLayer.UI_3);
        fillRenderer.setFillType(SpriteRenderer.FillType.Horizontal_LeftToRight);
        fillObject.setParent(healthBar.getGameObject());

        // Outline
        var outlineObject = GameObjectManager.instantiate("Outline");
        var outlineRenderer = outlineObject.addComponent(SpriteRenderer.class);
        outlineRenderer.setImage(ImageAsset.ImageIndex.Player_UI_HealthBar_Outline.getImage());
        outlineRenderer.setPivot(centerPivot);
        outlineRenderer.setSize(HEALTH_BAR_SIZE);
        outlineRenderer.setRenderLayer(RenderLayer.UI_3);
        outlineObject.setParent(healthBar.getGameObject());

        // Life
        var lifeContainerObject = GameObjectManager.instantiate("LifeContainer");
        lifeContainerObject.getTransform().setLocalPosition(LIVES_RENDER_OFFSET);
        lifeContainerObject.setParent(healthBar.getGameObject());

        // Left life
        var leftLifeObject = GameObjectManager.instantiate("LeftLife");
        var leftLifeRenderer = leftLifeObject.addComponent(SpriteRenderer.class);
        leftLifeRenderer.setImage(ImageAsset.ImageIndex.Player_UI_HealthBar_LifeRemain.getImage());
        leftLifeRenderer.setPivot(centerPivot);
        leftLifeRenderer.setSize(LIFE_BAR_SIZE);
        leftLifeRenderer.setRenderLayer(RenderLayer.UI_3);
        leftLifeObject.setParent(lifeContainerObject);
        leftLifeObject.getTransform().setLocalPosition(LIFE_RENDERER_OFFSET.inverse());

        // Center life
        var centerLifeObject = GameObjectManager.instantiate("CenterLife");
        var centerLifeRenderer = centerLifeObject.addComponent(SpriteRenderer.class);
        centerLifeRenderer.setImage(ImageAsset.ImageIndex.Player_UI_HealthBar_LifeRemain.getImage());
        centerLifeRenderer.setPivot(centerPivot);
        centerLifeRenderer.setSize(LIFE_BAR_SIZE);
        centerLifeRenderer.setRenderLayer(RenderLayer.UI_3);
        centerLifeObject.setParent(lifeContainerObject);

        // Right life
        var rightLifeObject = GameObjectManager.instantiate("RightLife");
        var rightLifeRenderer = rightLifeObject.addComponent(SpriteRenderer.class);
        rightLifeRenderer.setImage(ImageAsset.ImageIndex.Player_UI_HealthBar_LifeRemain.getImage());
        rightLifeRenderer.setPivot(centerPivot);
        rightLifeRenderer.setSize(LIFE_BAR_SIZE);
        rightLifeRenderer.setRenderLayer(RenderLayer.UI_3);
        rightLifeObject.setParent(lifeContainerObject);
        rightLifeObject.getTransform().setLocalPosition(LIFE_RENDERER_OFFSET);

        // Link component
        healthBar.linkFillRenderer(fillRenderer);
        healthBar.linkLivesRenderers(leftLifeRenderer, centerLifeRenderer, rightLifeRenderer);

        return healthBar.getGameObject();

    }

}