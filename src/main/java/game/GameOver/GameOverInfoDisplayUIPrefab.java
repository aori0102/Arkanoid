package game.GameOver;

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

public final class GameOverInfoDisplayUIPrefab extends Prefab {

    private static final Color TEXT_COLOR_GRADIENT_START = Color.web("#FF0000");
    private static final Color TEXT_COLOR_GRADIENT_END = Color.web("#A41010");
    private static final Color BACKING_COLOR_GRADIENT_START = Color.web("#9A5A1F");
    private static final Color BACKING_COLOR_GRADIENT_END = Color.web("#5A1212");
    private static final Vector2 TEXT_GRADIENT_START_ANCHOR = new Vector2(0.5, 0.0);
    private static final Vector2 TEXT_GRADIENT_END_ANCHOR = new Vector2(0.5, 1.0);
    private static final double LABEL_TEXT_SIZE = 24.0;
    private static final double INFO_TEXT_SIZE = 48.0;

    @Override
    public GameObject instantiatePrefab() {

        var gameOverInfoDisplayObject = GameObjectManager.instantiate("GameOverInfoDisplayUI")
                .addComponent(GameOverInfoDisplayUI.class)
                .getGameObject();
        var gameOverInfoDisplay = gameOverInfoDisplayObject.getComponent(GameOverInfoDisplayUI.class);

        // Label label text
        var labelText = GameObjectManager.instantiate("LabelText")
                .addComponent(TextUI.class);
        labelText.setFontSize(LABEL_TEXT_SIZE);
        labelText.setFont(FontDataIndex.Jersey_25);
        labelText.setHorizontalAlignment(TextHorizontalAlignment.Right);
        labelText.setVerticalAlignment(TextVerticalAlignment.Bottom);
        labelText.setGradientFill(
                TEXT_GRADIENT_START_ANCHOR,
                TEXT_GRADIENT_END_ANCHOR,
                new Stop(0.0, TEXT_COLOR_GRADIENT_START),
                new Stop(1.0, TEXT_COLOR_GRADIENT_END)
        );
        labelText.setRenderLayer(RenderLayer.UI_Top);
        labelText.getGameObject().setParent(gameOverInfoDisplayObject);

        // Backing amount text
        var backingAmountText = GameObjectManager.instantiate("BackingAmountText")
                .addComponent(TextUI.class);
        backingAmountText.setFontSize(INFO_TEXT_SIZE);
        backingAmountText.setFont(FontDataIndex.Jersey_25);
        backingAmountText.setHorizontalAlignment(TextHorizontalAlignment.Left);
        backingAmountText.setVerticalAlignment(TextVerticalAlignment.Bottom);
        backingAmountText.setGradientFill(
                TEXT_GRADIENT_START_ANCHOR,
                TEXT_GRADIENT_END_ANCHOR,
                new Stop(0.0, BACKING_COLOR_GRADIENT_START),
                new Stop(1.0, BACKING_COLOR_GRADIENT_END)
        );
        backingAmountText.setRenderLayer(RenderLayer.UI_Top);
        backingAmountText.getGameObject().setParent(gameOverInfoDisplayObject);

        // Amount text
        var amountText = GameObjectManager.instantiate("AmountText")
                .addComponent(TextUI.class);
        amountText.setFontSize(INFO_TEXT_SIZE);
        amountText.setFont(FontDataIndex.Jersey_25);
        amountText.setHorizontalAlignment(TextHorizontalAlignment.Left);
        amountText.setVerticalAlignment(TextVerticalAlignment.Bottom);
        amountText.setGradientFill(
                TEXT_GRADIENT_START_ANCHOR,
                TEXT_GRADIENT_END_ANCHOR,
                new Stop(0.0, TEXT_COLOR_GRADIENT_START),
                new Stop(1.0, TEXT_COLOR_GRADIENT_END)
        );
        amountText.setRenderLayer(RenderLayer.UI_Top);
        amountText.getGameObject().setParent(gameOverInfoDisplayObject);

        // Link component
        gameOverInfoDisplay.linkLabelText(labelText);
        gameOverInfoDisplay.linkAmountText(amountText);
        gameOverInfoDisplay.linkBackingText(backingAmountText);

        return gameOverInfoDisplayObject;

    }

}