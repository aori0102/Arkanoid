package game.Player;

import game.Entity.EntityHealth;
import game.Entity.EntityHealthAlterType;
import game.Player.Paddle.PaddleHealth;
import org.Annotation.LinkViaPrefab;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Rendering.SpriteRenderer;
import utils.Time;

public final class PlayerHealthLossVignette extends MonoBehaviour {

    private static final double PULSE_TIME = 0.9;

    @LinkViaPrefab
    private SpriteRenderer vignetteRenderer = null;

    private double pulseStartTime = 0.0;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PlayerHealthLossVignette(GameObject owner) {
        super(owner);
    }

    @Override
    public void start() {
        Player.getInstance().getPlayerPaddle().getPaddleHealth().onHealthChanged
                .addListener(this::paddleHealth_onHealthChanged);
    }

    @Override
    public void update() {
        double fadeRatio = (Time.getTime() - pulseStartTime) / PULSE_TIME;
        fadeRatio = Math.clamp(fadeRatio, 0, 1);
        double opacity = Math.pow(Math.sin(Math.PI * fadeRatio), 4);
        vignetteRenderer.setOpacity(opacity);
    }

    /**
     * Link the vignette effect object to this object.<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link game.Player.Prefab.PlayerPrefab}
     * as part of component linking process.</i></b>
     *
     * @param vignetteRenderer The renderer of the vignette object.
     */
    public void linkVignetteRenderer(SpriteRenderer vignetteRenderer) {
        this.vignetteRenderer = vignetteRenderer;
    }

    /**
     * Called when {@link PaddleHealth#onHealthChanged} is invoked.<br><br>
     * This function display the vignette for a split moment when the paddle takes damage.
     *
     * @param sender Event caller {@link PaddleHealth}.
     * @param e      Empty event argument.
     */
    private void paddleHealth_onHealthChanged(Object sender, EntityHealth.OnHealthChangedEventArgs e) {
        if (e.alterType == EntityHealthAlterType.PlayerTakeDamage) {
            pulseStartTime = Time.getTime();
        }
    }

}