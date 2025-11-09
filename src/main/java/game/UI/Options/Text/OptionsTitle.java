package game.UI.Options.Text;

import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Main;
import org.Text.FontDataIndex;
import org.Text.TextHorizontalAlignment;
import org.Text.TextUI;
import org.Text.TextVerticalAlignment;
import utils.Time;
import utils.UITween.Ease;
import utils.UITween.Tween;
import utils.Vector2;

public class OptionsTitle extends MonoBehaviour {
    private TextUI textUI;

    private final double FLUCTUATION_RATE = 1.2;
    private Vector2 originalPosition;
    private final double TWEEN_DURATION = 1;
    private final double TWEEN_DISTANCE = 250;
    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public OptionsTitle(GameObject owner) {
        super(owner);

        textUI = owner.addComponent(TextUI.class);
        textUI.setVerticalAlignment(TextVerticalAlignment.Middle);
        textUI.setHorizontalAlignment(TextHorizontalAlignment.Center);
        textUI.setFont(FontDataIndex.Jersey_25);
        textUI.setText("OPTIONS");
        textUI.getText().getStyleClass().add("neon-text");
        textUI.setFontSize(100);

        getTransform().setGlobalPosition(new Vector2(Main.STAGE_WIDTH/2, - TWEEN_DISTANCE/2 ));


    }

    @Override
    public void start() {
        startAnimation();
    }

    public void update() {
        idleAnimation();
    }

    public void startAnimation() {
        Tween.to(getGameObject())
                .moveY(TWEEN_DISTANCE, TWEEN_DURATION)
                .ease(Ease.OUT_BACK)
                .setDelay(0.0)
                .ignoreTimeScale(true)
                .play();
    }
    public void exitAnimation() {
        Tween.to(getGameObject())
                .moveY(- TWEEN_DISTANCE, TWEEN_DURATION)
                .ease(Ease.OUT_BOUNCE)
                .setDelay(0.0)
                .ignoreTimeScale(true)
                .play();
    }
    private void idleAnimation() {
        if(originalPosition == null) {
            originalPosition = getTransform().getGlobalPosition();
        }
        double time = Time.getRealTime();
        double phase = (getGameObject().hashCode() % 1000) / 1000.0 * Math.PI * 2; // unique offset
        double swing = Math.sin(FLUCTUATION_RATE * time * Math.PI + phase) * 5.0; // ±5 px
        getTransform().setGlobalPosition(originalPosition.add(new Vector2(0, swing)));
    }
}
