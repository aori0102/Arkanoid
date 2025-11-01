package game.GameManager.Score;

import javafx.scene.paint.Color;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Layer.RenderLayer;
import org.Main;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import org.Text.FontDataIndex;
import org.Text.TextHorizontalAlignment;
import org.Text.TextUI;
import org.Text.TextVerticalAlignment;
import utils.Vector2;

public final class ScoreManagerPrefab {

    public static final Color TEXT_COLOR = Color.web("#EEDC13");
    public static final double LEVEL_TEXT_SIZE = 36;
    public static final double SCORE_TEXT_SIZE = 64;

    public static void instantiate() {

        var scoreManager = GameObjectManager.instantiate("ScoreManager")
                .addComponent(ScoreManager.class);
        instantiateScoreUI().setParent(scoreManager.getGameObject());

    }

    private static GameObject instantiateScoreUI() {

        var scoreUI = GameObjectManager.instantiate("Scoreboard")
                .addComponent(ScoreUI.class);

        // Background
        var backgroundRenderer = GameObjectManager.instantiate("Background")
                .addComponent(SpriteRenderer.class);
        backgroundRenderer.setPivot(new Vector2(1.0, 0.0));
        backgroundRenderer.setImage(ImageAsset.ImageIndex.Player_UI_Scoreboard_Background.getImage());
        backgroundRenderer.setRenderLayer(RenderLayer.UI);
        backgroundRenderer.getTransform().setGlobalPosition(new Vector2(Main.STAGE_WIDTH, 0.0));
        backgroundRenderer.getGameObject().setParent(scoreUI.getGameObject());

        // Score text
        var scoreText = GameObjectManager.instantiate("ScoreText")
                .addComponent(TextUI.class);
        scoreText.setFont(FontDataIndex.Jersey_25);
        scoreText.setFontSize(SCORE_TEXT_SIZE);
        scoreText.setTextColor(TEXT_COLOR);
        scoreText.setText("0");
        scoreText.setHorizontalAlignment(TextHorizontalAlignment.Center);
        scoreText.setVerticalAlignment(TextVerticalAlignment.Middle);
        scoreText.setRenderLayer(RenderLayer.UI);
        scoreText.getGameObject().setParent(scoreUI.getGameObject());
        scoreText.getTransform().setGlobalPosition(new Vector2(1075.0, 214.0));

        // Level text
        var levelText = GameObjectManager.instantiate("LevelText")
                .addComponent(TextUI.class);
        levelText.setFont(FontDataIndex.Jersey_25);
        levelText.setFontSize(LEVEL_TEXT_SIZE);
        levelText.setTextColor(TEXT_COLOR);
        levelText.setText("Level 1");
        levelText.setHorizontalAlignment(TextHorizontalAlignment.Center);
        levelText.setVerticalAlignment(TextVerticalAlignment.Middle);
        levelText.setRenderLayer(RenderLayer.UI);
        levelText.setPivot(new Vector2(0.5, 0.0));
        levelText.getGameObject().setParent(scoreUI.getGameObject());
        levelText.getTransform().setGlobalPosition(new Vector2(1075.0, 32.0));

        // Combo text
        var comboText = GameObjectManager.instantiate("ComboText")
                .addComponent(TextUI.class);
        comboText.setFont(FontDataIndex.Jersey_25);
        comboText.setFontSize(SCORE_TEXT_SIZE);
        comboText.setTextColor(TEXT_COLOR);
        comboText.setText("x0");
        comboText.setHorizontalAlignment(TextHorizontalAlignment.Center);
        comboText.setVerticalAlignment(TextVerticalAlignment.Middle);
        comboText.setRenderLayer(RenderLayer.UI);
        comboText.getGameObject().setParent(scoreUI.getGameObject());
        comboText.getTransform().setGlobalPosition(new Vector2(1075.0, 348.0));

        // Link component to score manager
        scoreUI.linkScoreText(scoreText);
        scoreUI.linkComboText(comboText);

        return scoreUI.getGameObject();

    }

}