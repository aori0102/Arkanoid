package game.UI;

import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import utils.Time;
import utils.UITween.Ease;
import utils.UITween.Tween;
import utils.Vector2;

import javax.swing.*;

/**
 * Manages the behavior and animation of the title UI element.
 * This class provides methods for a smooth entry/exit animation and a continuous
 * subtle "idle" floating animation.
 */
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

    /**
     * Initiates the smooth entry animation (move down) for the title.
     * Uses the {@link Ease#OUT_BOUNCE} function for a dramatic effect.
     */
    public void startAnimation() {
        Tween.to(getGameObject())
                .moveY(TWEEN_DISTANCE, TWEEN_DURATION)
                .ease(Ease.OUT_BOUNCE)
                .setDelay(0.0)
                .ignoreTimeScale(true)
                .play();
    }

    /**
     * Initiates the smooth exit animation (move up and off-screen) for the title.
     * Uses the {@link Ease#OUT_BOUNCE} function.
     */
    public void exitAnimation() {
        Tween.to(getGameObject())
                .moveY(-TWEEN_DISTANCE, TWEEN_DURATION)
                .ease(Ease.OUT_BOUNCE)
                .setDelay(0.0)
                .ignoreTimeScale(true)
                .play();
    }

    /**
     * Executes the continuous subtle vertical floating animation (idle animation).
     * <p>
     * It uses a sine wave function based on real time to create a smooth,
     * low-amplitude vertical fluctuation (±5 pixels). A unique phase offset is
     * calculated based on the GameObject's hash code to ensure multiple titles
     * don't swing in perfect sync.
     * </p>
     */
    private void idleAnimation() {
        if (originalPosition == null) {
            originalPosition = getTransform().getGlobalPosition();
        }
        double time = Time.getRealTime();
        double phase = (getGameObject().hashCode() % 1000) / 1000.0 * Math.PI * 2; // unique offset
        double swing = Math.sin(FLUCTUATION_RATE * time * Math.PI + phase) * 5.0; // ±5 px
        getTransform().setGlobalPosition(originalPosition.add(new Vector2(0, swing)));
    }
}
