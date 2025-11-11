package game.GameOver;

import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Layer.RenderLayer;
import org.Main;
import org.Prefab.Prefab;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import org.Text.FontDataIndex;
import org.Text.TextHorizontalAlignment;
import org.Text.TextUI;
import org.Text.TextVerticalAlignment;
import utils.Vector2;

public final class GameOverManagerPrefab extends Prefab {

    private static final Color TEXT_COLOR_GRADIENT_START = Color.web("#A41010");
    private static final Color TEXT_COLOR_GRADIENT_END = Color.web("#4D0808");
    private static final Vector2 TEXT_GRADIENT_START_ANCHOR = new Vector2(0.5, 0.0);
    private static final Vector2 TEXT_GRADIENT_END_ANCHOR = new Vector2(0.5, 1.0);

    private static final String GAME_OVER_LABEL = "Game Over";
    private static final double GAME_OVER_TEXT_SIZE = 128.0;
    private static final Vector2 GAME_OVER_TEXT_POSITION = new Vector2(Main.STAGE_WIDTH / 2.0, 315.0);

    private static final Vector2 INFO_STARTING_TEXT_POSITION = new Vector2(Main.STAGE_WIDTH / 2.0, 485.0);
    private static final Vector2 INFO_TEXT_OFFSET = new Vector2(0.0, 62.0);

    private static final String SCORE_LABEL = "Score";
    private static final String RANK_LABEL = "Rank";
    private static final String LEVEL_CLEARED_LABEL = "Level cleared";
    private static final String BRICK_DESTROYED_LABEL = "Brick destroyed";

    @Override
    public GameObject instantiatePrefab() {

        var gameOverManagerObject = GameObjectManager.instantiate("GameOverManager")
                .addComponent(GameOverManager.class)
                .getGameObject();

        instantiateGameOverUI(gameOverManagerObject.getComponent(GameOverManager.class));

        return gameOverManagerObject;

    }

    private void instantiateGameOverUI(GameOverManager gameOverManager) {

        var gameOverManagerObject = gameOverManager.getGameObject();

        // Background
        var backgroundRenderer = GameObjectManager.instantiate("Background")
                .addComponent(SpriteRenderer.class);
        backgroundRenderer.setImage(ImageAsset.ImageIndex.GameOverOverlay.getImage());
        backgroundRenderer.setRenderLayer(RenderLayer.UI_Top);
        backgroundRenderer.getGameObject().setParent(gameOverManagerObject);

        // Game over text
        var gameOverText = GameObjectManager.instantiate("GameOverText")
                .addComponent(TextUI.class);
        gameOverText.setText(GAME_OVER_LABEL);
        gameOverText.setFontSize(GAME_OVER_TEXT_SIZE);
        gameOverText.setFont(FontDataIndex.Jersey_25);
        gameOverText.setHorizontalAlignment(TextHorizontalAlignment.Center);
        gameOverText.setVerticalAlignment(TextVerticalAlignment.Middle);
        gameOverText.setGradientFill(
                TEXT_GRADIENT_START_ANCHOR,
                TEXT_GRADIENT_END_ANCHOR,
                new Stop(0.0, TEXT_COLOR_GRADIENT_START),
                new Stop(1.0, TEXT_COLOR_GRADIENT_END)
        );
        gameOverText.setRenderLayer(RenderLayer.UI_Top);
        gameOverText.getTransform().setGlobalPosition(GAME_OVER_TEXT_POSITION);
        gameOverText.getGameObject().setParent(gameOverManagerObject);

        // Score info display
        var infoPosition = INFO_STARTING_TEXT_POSITION;
        var scoreInfoDisplay = PrefabManager.instantiatePrefab(PrefabIndex.GameOverInfoDisplayUI)
                .getComponent(GameOverInfoDisplayUI.class);
        scoreInfoDisplay.setLabelText(SCORE_LABEL);
        scoreInfoDisplay.getTransform().setGlobalPosition(infoPosition);

        // Rank info display
        infoPosition = infoPosition.add(INFO_TEXT_OFFSET);
        var rankInfoDisplay = PrefabManager.instantiatePrefab(PrefabIndex.GameOverInfoDisplayUI)
                .getComponent(GameOverInfoDisplayUI.class);
        rankInfoDisplay.setLabelText(RANK_LABEL);
        rankInfoDisplay.getTransform().setGlobalPosition(infoPosition);

        // Level cleared info display
        infoPosition = infoPosition.add(INFO_TEXT_OFFSET);
        var levelClearedInfoDisplay = PrefabManager.instantiatePrefab(PrefabIndex.GameOverInfoDisplayUI)
                .getComponent(GameOverInfoDisplayUI.class);
        levelClearedInfoDisplay.setLabelText(LEVEL_CLEARED_LABEL);
        levelClearedInfoDisplay.getTransform().setGlobalPosition(infoPosition);

        // Brick destroyed info display
        infoPosition = infoPosition.add(INFO_TEXT_OFFSET);
        var brickDestroyedInfoDisplay = PrefabManager.instantiatePrefab(PrefabIndex.GameOverInfoDisplayUI)
                .getComponent(GameOverInfoDisplayUI.class);
        brickDestroyedInfoDisplay.setLabelText(BRICK_DESTROYED_LABEL);
        brickDestroyedInfoDisplay.getTransform().setGlobalPosition(infoPosition);

        // Link component
        gameOverManager.linkBackground(backgroundRenderer);
        gameOverManager.linkBrickDestroyedText(brickDestroyedInfoDisplay);
        gameOverManager.linkRankText(rankInfoDisplay);
        gameOverManager.linkScoreText(scoreInfoDisplay);
        gameOverManager.linkLevelClearedText(levelClearedInfoDisplay);
        gameOverManager.linkGameOverText(gameOverText);

    }

}
