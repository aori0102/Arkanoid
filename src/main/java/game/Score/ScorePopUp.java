package game.Score;

import javafx.scene.paint.Color;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Layer.RenderLayer;
import org.Text.FontDataIndex;
import org.Text.TextHorizontalAlignment;
import org.Text.TextUI;
import org.Text.TextVerticalAlignment;
import utils.Time;
import utils.Vector2;

public final class ScorePopUp extends MonoBehaviour {

    public static final Color TEXT_COLOR = Color.web("#EEDC13");
    private static final String POP_UP_PREFIX = "+";
    private static final double LIFE_TIME = 0.9;
    private static final double POP_UP_AMPLITUDE = 49.0;

    private final TextUI scoreText = addComponent(TextUI.class);

    private double spawnTime = 0.0;
    private Vector2 entryPoint = Vector2.zero();

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public ScorePopUp(GameObject owner) {
        super(owner);

        scoreText.setRenderLayer(RenderLayer.UI_Middle);
        scoreText.setFontSize(20);
        scoreText.setFont(FontDataIndex.Jersey_25);
        scoreText.setVerticalAlignment(TextVerticalAlignment.Bottom);
        scoreText.setHorizontalAlignment(TextHorizontalAlignment.Right);
        scoreText.setSolidFill(TEXT_COLOR);
    }

    @Override
    public void awake() {
        spawnTime = Time.getTime();
        Time.addCoroutine(() -> GameObjectManager.destroy(gameObject), Time.getTime() + LIFE_TIME);
    }

    @Override
    public void update() {
        var timeInterval = (Time.getTime() - spawnTime) / LIFE_TIME;
        var opacity = Math.sin(3.0 * timeInterval) * (-Math.log(timeInterval / 1.1));
        var altitude = Math.pow(Math.log(14.8 * timeInterval), 3.0) / 20.0 + 0.4;
        //TODO: add a queue here

        scoreText.setOpacity(opacity);
        getTransform().setGlobalPosition(entryPoint.add(Vector2.up().multiply(altitude * POP_UP_AMPLITUDE)));
    }

    public void setAmount(int amount) {
        scoreText.setText(POP_UP_PREFIX + amount);
    }

    public void setEntryPoint(Vector2 entryPoint) {
        this.entryPoint = entryPoint;
    }

}