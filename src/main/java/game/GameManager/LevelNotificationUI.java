package game.GameManager;

import javafx.scene.paint.Color;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Layer.RenderLayer;
import org.Main;
import org.Text.FontDataIndex;
import org.Text.TextHorizontalAlignment;
import org.Text.TextUI;
import org.Text.TextVerticalAlignment;
import utils.Time;
import utils.Vector2;

public final class LevelNotificationUI extends MonoBehaviour {

    private static final String LEVEL_PREFIX = "Level ";
    private static final String LEVEL_SUFFIX = " cleared!";
    private static final double LEVEL_TEXT_SIZE = 80.0;

    private final TextUI levelText = addComponent(TextUI.class);

    private double startTick = 0.0;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public LevelNotificationUI(GameObject owner) {
        super(owner);

        levelText.setFontSize(LEVEL_TEXT_SIZE);
        levelText.setRenderLayer(RenderLayer.UI_Middle);
        levelText.setTextColor(Color.BLUE);
        levelText.setVerticalAlignment(TextVerticalAlignment.Middle);
        levelText.setHorizontalAlignment(TextHorizontalAlignment.Center);
        levelText.setFont(FontDataIndex.Jersey_25);
    }

    @Override
    public void awake() {
        startTick = Time.getTime();
        Time.addCoroutine(() -> GameObjectManager.destroy(gameObject), Time.getTime() + GameManager.LEVEL_INTRODUCTION_TIME);
    }

    @Override
    public void update() {
        var delta = (Time.getTime() - startTick) / GameManager.LEVEL_INTRODUCTION_TIME;
        var positionRatio = Math.pow(1.92 * delta - 0.9, 3.0) + 0.5;
        getTransform().setGlobalPosition(new Vector2(Main.STAGE_WIDTH * positionRatio, Main.STAGE_HEIGHT / 2.0));
    }

    public void setLevel(int level) {
        levelText.setText(LEVEL_PREFIX + level);
    }

    public void setLevelClear(int level) {
        levelText.setText(LEVEL_PREFIX + level + LEVEL_SUFFIX);
    }

}