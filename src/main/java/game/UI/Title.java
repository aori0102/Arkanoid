package game.UI;

import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import utils.Time;
import utils.UITween.Ease;
import utils.UITween.Tween;
import utils.Vector2;

import javax.swing.*;

public class Title extends MonoBehaviour {
    protected final double FLUCTUATION_RATE = 1.2;
    private Vector2 originalPosition;
    protected final double TWEEN_DURATION = 1;
    protected final double TWEEN_DISTANCE = 250;
    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public Title(GameObject owner) {
        super(owner);
    }


    public void update() {
        idleAnimation();
    }

    public void startAnimation() {
        Tween.to(getGameObject())
                .moveY(TWEEN_DISTANCE, TWEEN_DURATION)
                .ease(Ease.OUT_BOUNCE)
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
