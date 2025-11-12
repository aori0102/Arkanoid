package game.Level;

import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
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

/**
 * LevelNotificationUI is responsible for displaying a notification
 * on the screen when a level starts or is cleared.
 * <p>
 * It shows text in the middle of the screen, animates its horizontal position,
 * and destroys itself after the level introducing duration.
 * </p>
 */
public final class LevelNotificationUI extends MonoBehaviour {

    private static final Color GRADIENT_START = Color.web("#2B59FF");
    private static final Color GRADIENT_END = Color.web("#452957");
    private static final Vector2 GRADIENT_ANCHOR_START = new Vector2(0.5, 0.0);
    private static final Vector2 GRADIENT_ANCHOR_END = new Vector2(0.5, 1.0);
    private static final double LEVEL_TEXT_SIZE = 80.0;

    /**
     * TextUI component used to display the level notification.
     */
    private final TextUI levelText = addComponent(TextUI.class);

    /**
     * Timestamp when this UI element was created.
     */
    private double startTick = 0.0;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public LevelNotificationUI(GameObject owner) {
        super(owner);

        // Configure the TextUI component
        levelText.setFontSize(LEVEL_TEXT_SIZE);
        levelText.setRenderLayer(RenderLayer.UI_Middle);
        levelText.setGradientFill(
                GRADIENT_ANCHOR_START,
                GRADIENT_ANCHOR_END,
                new Stop(0.0, GRADIENT_START),
                new Stop(1.0, GRADIENT_END)
        );
        levelText.setVerticalAlignment(TextVerticalAlignment.Middle);
        levelText.setHorizontalAlignment(TextHorizontalAlignment.Center);
        levelText.setFont(FontDataIndex.Jersey_25);
    }

    /**
     * Awake is called when this MonoBehaviour is initialized.
     * It records the creation time and schedules this object to be destroyed
     * after the level introducing duration.
     */
    @Override
    public void awake() {
        startTick = Time.getTime();
        Time.addCoroutine(() -> GameObjectManager.destroy(gameObject), LevelManager.LEVEL_INTRODUCING_TIME);
    }

    /**
     * Update is called every frame.
     * It animates the level notification's horizontal position across the screen
     * based on the elapsed time.
     */
    @Override
    public void update() {
        var delta = (Time.getTime() - startTick) / LevelManager.LEVEL_INTRODUCING_TIME;
        var positionRatio = Math.pow(1.92 * delta - 0.9, 3.0) + 0.5;
        getTransform().setGlobalPosition(
                new Vector2(Main.STAGE_WIDTH * positionRatio, Main.STAGE_HEIGHT / 2.0)
        );
    }

    /**
     * Set the text notification to display.
     *
     * @param notification The text to display on screen.
     */
    public void setNotification(String notification) {
        levelText.setText(notification);
    }

}
