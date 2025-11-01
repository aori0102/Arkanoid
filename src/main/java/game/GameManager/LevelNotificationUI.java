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

    private static final double STARTING_RATIO = -0.5;
    private static final double ENDING_RATIO = 1.9;
    private static final double DURATION = 3.2;
    private static final String LEVEL_PREFIX = "Level ";
    private static final double LEVEL_TEXT_SIZE = 80.0;

    private TextUI levelText = addComponent(TextUI.class);
    private double startTick = 0.0;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public LevelNotificationUI(GameObject owner) {
        super(owner);

        levelText.setFontSize(LEVEL_TEXT_SIZE);
        levelText.setRenderLayer(RenderLayer.UI);
        levelText.setTextColor(Color.BLUE);
        levelText.setVerticalAlignment(TextVerticalAlignment.Middle);
        levelText.setHorizontalAlignment(TextHorizontalAlignment.Center);
        levelText.setFont(FontDataIndex.Jersey_25);
    }

    @Override
    public void awake() {
        startTick = Time.getTime();
    }

    @Override
    public void update() {
        var delta = (Time.getTime() - startTick) / DURATION + STARTING_RATIO;
        var positionRatio = Math.pow(1.6 * delta - 0.8, 3.0) + 0.5;
        if (delta > ENDING_RATIO) {
            GameObjectManager.destroy(gameObject);
        } else {
            getTransform().setGlobalPosition(new Vector2(Main.STAGE_WIDTH * positionRatio, Main.STAGE_HEIGHT / 2.0));
        }
    }

    public void setLevel(int level) {
        levelText.setText(String.format("%s%d", LEVEL_PREFIX, level));
    }

}