package game.Score;

import javafx.scene.paint.Color;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Layer.RenderLayer;
import org.Main;
import org.Prefab.Prefab;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import org.Text.FontDataIndex;
import org.Text.TextHorizontalAlignment;
import org.Text.TextUI;
import org.Text.TextVerticalAlignment;
import utils.Vector2;

public final class ScoreUIPrefab extends Prefab {

    public static final Color TEXT_COLOR = Color.web("#EEDC13");

    private static final Vector2 SCORE_TEXT_POSITION = new Vector2(1075.0, 214.0);
    private static final String SCORE_TEXT_DEFAULT = "0";
    public static final double SCORE_TEXT_SIZE = 64.0;

    public static final double LEVEL_TEXT_SIZE = 36.0;
    private static final String LEVEL_TEXT_DEFAULT = "Level 1";
    private static final Vector2 LEVEL_TEXT_POSITION = new Vector2(1075.0, 32.0);

    private static final String COMBO_TEXT_DEFAULT = "x0";
    private static final Vector2 COMBO_TEXT_POSITION = new Vector2(1075.0, 348.0);

    @Override
    public GameObject instantiatePrefab() {

        var scoreUI = GameObjectManager.instantiate("ScoreUI")
                .addComponent(ScoreUI.class);

        // Score text
        var scoreText = GameObjectManager.instantiate("ScoreText")
                .addComponent(TextUI.class);
        scoreText.setFont(FontDataIndex.Jersey_25);
        scoreText.setFontSize(SCORE_TEXT_SIZE);
        scoreText.setTextColor(TEXT_COLOR);
        scoreText.setText(SCORE_TEXT_DEFAULT);
        scoreText.setHorizontalAlignment(TextHorizontalAlignment.Center);
        scoreText.setVerticalAlignment(TextVerticalAlignment.Middle);
        scoreText.setRenderLayer(RenderLayer.UI);
        scoreText.getGameObject().setParent(scoreUI.getGameObject());
        scoreText.getTransform().setGlobalPosition(SCORE_TEXT_POSITION);

        // Level text
        var levelText = GameObjectManager.instantiate("LevelText")
                .addComponent(TextUI.class);
        levelText.setFont(FontDataIndex.Jersey_25);
        levelText.setFontSize(LEVEL_TEXT_SIZE);
        levelText.setTextColor(TEXT_COLOR);
        levelText.setText(LEVEL_TEXT_DEFAULT);
        levelText.setHorizontalAlignment(TextHorizontalAlignment.Center);
        levelText.setVerticalAlignment(TextVerticalAlignment.Middle);
        levelText.setRenderLayer(RenderLayer.UI);
        levelText.setPivot(new Vector2(0.5, 0.0));
        levelText.getGameObject().setParent(scoreUI.getGameObject());
        levelText.getTransform().setGlobalPosition(LEVEL_TEXT_POSITION);

        // Combo text
        var comboText = GameObjectManager.instantiate("ComboText")
                .addComponent(TextUI.class);
        comboText.setFont(FontDataIndex.Jersey_25);
        comboText.setFontSize(SCORE_TEXT_SIZE);
        comboText.setTextColor(TEXT_COLOR);
        comboText.setText(COMBO_TEXT_DEFAULT);
        comboText.setHorizontalAlignment(TextHorizontalAlignment.Center);
        comboText.setVerticalAlignment(TextVerticalAlignment.Middle);
        comboText.setRenderLayer(RenderLayer.UI);
        comboText.getGameObject().setParent(scoreUI.getGameObject());
        comboText.getTransform().setGlobalPosition(COMBO_TEXT_POSITION);

        // Link component to score manager
        scoreUI.linkScoreText(scoreText);
        scoreUI.linkComboText(comboText);

        return scoreUI.getGameObject();

    }

}