package game.Rank;

import javafx.scene.paint.Color;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Layer.RenderLayer;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import org.Text.FontDataIndex;
import org.Text.TextHorizontalAlignment;
import org.Text.TextUI;
import org.Text.TextVerticalAlignment;
import org.Prefab.Prefab;
import utils.Vector2;

public final class RankUIPrefab extends Prefab {

    private static final Vector2 EXP_BAR_POSITION = new Vector2(1075.0, 506.0);

    private static final Vector2 RANK_TEXT_POSITION = new Vector2(975.0, 521.0);
    private static final double RANK_TEXT_SIZE = 36.0;
    private static final String RANK_DEFAULT_TEXT = "Rank 0";
    private static final Color RANK_TEXT_COLOR = Color.web("#EEDC13");

    private static final Vector2 RANK_UP_ICON_POSITION = new Vector2(1147.0, 527.0);

    @Override
    public GameObject instantiatePrefab() {

        var rankUIObject = GameObjectManager.instantiate("RankUI");
        var rankUI = rankUIObject.addComponent(RankUI.class);
        var centerPivot = new Vector2(0.5, 0.5);

        // EXP Bar Background
        var expBarBackgroundRenderer = GameObjectManager.instantiate("ExpBarBackground")
                .addComponent(SpriteRenderer.class);
        expBarBackgroundRenderer.setImage(ImageAsset.ImageIndex.Player_UI_Rank_Background.getImage());
        expBarBackgroundRenderer.setPivot(centerPivot);
        expBarBackgroundRenderer.setRenderLayer(RenderLayer.UI);
        expBarBackgroundRenderer.getTransform().setGlobalPosition(EXP_BAR_POSITION);
        expBarBackgroundRenderer.getGameObject().setParent(rankUIObject);

        // EXP Bar Fill
        var expBarFillRenderer = GameObjectManager.instantiate("ExpBarFill")
                .addComponent(SpriteRenderer.class);
        expBarFillRenderer.setImage(ImageAsset.ImageIndex.Player_UI_Rank_Fill.getImage());
        expBarFillRenderer.setPivot(centerPivot);
        expBarFillRenderer.setRenderLayer(RenderLayer.UI);
        expBarFillRenderer.setFillType(SpriteRenderer.FillType.Horizontal_LeftToRight);
        expBarFillRenderer.setFillAmount(0.0);
        expBarFillRenderer.getTransform().setGlobalPosition(EXP_BAR_POSITION);
        expBarFillRenderer.getGameObject().setParent(rankUIObject);

        // EXP Bar Outline
        var expBarOutlineRenderer = GameObjectManager.instantiate("ExpBarOutline")
                .addComponent(SpriteRenderer.class);
        expBarOutlineRenderer.setImage(ImageAsset.ImageIndex.Player_UI_Rank_Outline.getImage());
        expBarOutlineRenderer.setPivot(centerPivot);
        expBarOutlineRenderer.setRenderLayer(RenderLayer.UI);
        expBarOutlineRenderer.getTransform().setGlobalPosition(EXP_BAR_POSITION);
        expBarOutlineRenderer.getGameObject().setParent(rankUIObject);

        // Rank text
        var rankText = GameObjectManager.instantiate("RankText")
                .addComponent(TextUI.class);
        rankText.setFont(FontDataIndex.Jersey_25);
        rankText.setFontSize(RANK_TEXT_SIZE);
        rankText.setText(RANK_DEFAULT_TEXT);
        rankText.setHorizontalAlignment(TextHorizontalAlignment.Left);
        rankText.setVerticalAlignment(TextVerticalAlignment.Top);
        rankText.setTextColor(RANK_TEXT_COLOR);
        rankText.setRenderLayer(RenderLayer.UI);
        rankText.getTransform().setGlobalPosition(RANK_TEXT_POSITION);
        rankText.getGameObject().setParent(rankUIObject);

        // Rank up icon
        var rankUpIconRenderer = GameObjectManager.instantiate("rankUpIcon")
                .addComponent(SpriteRenderer.class);
        rankUpIconRenderer.setImage(ImageAsset.ImageIndex.Player_UI_Rank_LevelUpIcon.getImage());
        rankUpIconRenderer.setPivot(centerPivot);
        rankUpIconRenderer.setRenderLayer(RenderLayer.UI);
        rankUpIconRenderer.getTransform().setGlobalPosition(RANK_UP_ICON_POSITION);
        rankUpIconRenderer.getGameObject().setParent(rankUIObject);

        // Link component
        rankUI.linkRankText(rankText);
        rankUI.linkRankUpRenderer(rankUpIconRenderer);
        rankUI.linkFillRenderer(expBarFillRenderer);

        return rankUIObject;

    }
}
