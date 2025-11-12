package game.Player.Prefab;

import game.Player.PlayerStatUI;
import javafx.scene.paint.Color;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Layer.RenderLayer;
import org.Prefab.Prefab;
import org.Text.FontDataIndex;
import org.Text.TextHorizontalAlignment;
import org.Text.TextUI;
import org.Text.TextVerticalAlignment;
import utils.Vector2;

public final class PlayerStatUIPrefab extends Prefab {

    private static final double TEXT_SIZE = 21.0;
    private static final double OFFSET_FROM_MIDDLE = 110.0;

    @Override
    public GameObject instantiatePrefab() {

        var playerStatUIObject = GameObjectManager.instantiate("PlayerStatUI")
                .addComponent(PlayerStatUI.class)
                .getGameObject();
        var playerStateUI = playerStatUIObject.getComponent(PlayerStatUI.class);

        // Label
        var labelText = GameObjectManager.instantiate("LabelText")
                .addComponent(TextUI.class);
        labelText.setFont(FontDataIndex.Jersey_25);
        labelText.setFontSize(TEXT_SIZE);
        labelText.setSolidFill(Color.YELLOW);
        labelText.setHorizontalAlignment(TextHorizontalAlignment.Left);
        labelText.setVerticalAlignment(TextVerticalAlignment.Bottom);
        labelText.setRenderLayer(RenderLayer.UI_3);
        labelText.getTransform().setLocalPosition(Vector2.left().multiply(OFFSET_FROM_MIDDLE));
        labelText.getGameObject().setParent(playerStatUIObject);

        // Label
        var amountText = GameObjectManager.instantiate("AmountText")
                .addComponent(TextUI.class);
        amountText.setFont(FontDataIndex.Jersey_25);
        amountText.setFontSize(TEXT_SIZE);
        amountText.setSolidFill(Color.YELLOW);
        amountText.setHorizontalAlignment(TextHorizontalAlignment.Right);
        amountText.setVerticalAlignment(TextVerticalAlignment.Bottom);
        amountText.setRenderLayer(RenderLayer.UI_3);
        amountText.getTransform().setLocalPosition(Vector2.right().multiply(OFFSET_FROM_MIDDLE));
        amountText.getGameObject().setParent(playerStatUIObject);

        // Link component
        playerStateUI.linkAmountText(amountText);
        playerStateUI.linkLabelText(labelText);

        return playerStatUIObject;

    }
}
