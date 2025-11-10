package game.Score;

import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Layer.RenderLayer;
import org.Prefab.Prefab;
import org.Text.FontDataIndex;
import org.Text.TextHorizontalAlignment;
import org.Text.TextUI;
import org.Text.TextVerticalAlignment;
import utils.Vector2;

public final class ScoreUIPrefab extends Prefab {

    public static final Color TEXT_COLOR_GRADIENT_START = Color.web("#EEDC13");
    public static final Color TEXT_COLOR_GRADIENT_END = Color.web("#EE9A13");

    private static final Vector2 SCORE_TEXT_POSITION = new Vector2(125.0, 214.0);
    private static final String SCORE_TEXT_DEFAULT = "0";
    public static final double SCORE_TEXT_SIZE = 64.0;

    public static final double LEVEL_TEXT_SIZE = 36.0;
    private static final String LEVEL_TEXT_DEFAULT = "Level 1";
    private static final Vector2 LEVEL_TEXT_POSITION = new Vector2(125.0, 32.0);

    private static final String COMBO_TEXT_DEFAULT = "x0";
    private static final Vector2 COMBO_TEXT_POSITION = new Vector2(125.0, 348.0);

    @Override
    public GameObject instantiatePrefab() {

        var scoreUI = GameObjectManager.instantiate("ScoreUI")
                .addComponent(ScoreUI.class);

        // Score text
        var scoreText = GameObjectManager.instantiate("ScoreText")
                .addComponent(TextUI.class);
        scoreText.setFont(FontDataIndex.Jersey_25);
        scoreText.setFontSize(SCORE_TEXT_SIZE);
        scoreText.setGradientFill(
                new Vector2(0.5, 0.0),
                new Vector2(0.5, 1.0),
                new Stop(0.0, TEXT_COLOR_GRADIENT_START),
                new Stop(1.0, TEXT_COLOR_GRADIENT_END)
        );
        scoreText.setText(SCORE_TEXT_DEFAULT);
        scoreText.setHorizontalAlignment(TextHorizontalAlignment.Center);
        scoreText.setVerticalAlignment(TextVerticalAlignment.Middle);
        scoreText.setRenderLayer(RenderLayer.UI_Middle);
        scoreText.getGameObject().setParent(scoreUI.getGameObject());
        scoreText.getTransform().setGlobalPosition(SCORE_TEXT_POSITION);

        // Level text
        var levelText = GameObjectManager.instantiate("LevelText")
                .addComponent(TextUI.class);
        levelText.setFont(FontDataIndex.Jersey_25);
        levelText.setFontSize(LEVEL_TEXT_SIZE);
        levelText.setGradientFill(
                new Vector2(0.5, 0.0),
                new Vector2(0.5, 1.0),
                new Stop(0.0, TEXT_COLOR_GRADIENT_START),
                new Stop(1.0, TEXT_COLOR_GRADIENT_END)
        );
        levelText.setText(LEVEL_TEXT_DEFAULT);
        levelText.setHorizontalAlignment(TextHorizontalAlignment.Center);
        levelText.setVerticalAlignment(TextVerticalAlignment.Middle);
        levelText.setRenderLayer(RenderLayer.UI_Middle);
        levelText.setPivot(new Vector2(0.5, 0.0));
        levelText.getGameObject().setParent(scoreUI.getGameObject());
        levelText.getTransform().setGlobalPosition(LEVEL_TEXT_POSITION);

        // Combo text
        var comboText = GameObjectManager.instantiate("ComboText")
                .addComponent(TextUI.class);
        comboText.setFont(FontDataIndex.Jersey_25);
        comboText.setFontSize(SCORE_TEXT_SIZE);
        comboText.setGradientFill(
                new Vector2(0.5, 0.0),
                new Vector2(0.5, 1.0),
                new Stop(0.0, TEXT_COLOR_GRADIENT_START),
                new Stop(1.0, TEXT_COLOR_GRADIENT_END)
        );
        comboText.setText(COMBO_TEXT_DEFAULT);
        comboText.setHorizontalAlignment(TextHorizontalAlignment.Center);
        comboText.setVerticalAlignment(TextVerticalAlignment.Middle);
        comboText.setRenderLayer(RenderLayer.UI_Middle);
        comboText.getGameObject().setParent(scoreUI.getGameObject());
        comboText.getTransform().setGlobalPosition(COMBO_TEXT_POSITION);

        // Link component to score manager
        scoreUI.linkScoreText(scoreText);
        scoreUI.linkComboText(comboText);

        return scoreUI.getGameObject();

    }

}