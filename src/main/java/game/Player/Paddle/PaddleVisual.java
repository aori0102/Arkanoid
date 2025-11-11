package game.Player.Paddle;

import game.Entity.EntityHealth;
import game.Player.Prefab.PaddlePrefab;
import javafx.scene.paint.Color;
import org.Annotation.LinkViaPrefab;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Rendering.SpriteRenderer;
import utils.Time;

public final class PaddleVisual extends MonoBehaviour {

    private static final double DAMAGE_FLASHING_TIME = 0.1;

    private final SpriteRenderer renderer = addComponent(SpriteRenderer.class);

    @LinkViaPrefab
    private PlayerPaddle paddle = null;

    private Time.CoroutineID resetDamageTint_coroutineID = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PaddleVisual(GameObject owner) {
        super(owner);
    }

    @Override
    public void start() {
        paddle.getPaddleHealth().onHealthChanged.addListener(this::paddleHealth_onHealthChanged);
    }

    @Override
    protected void onDestroy() {
        Time.removeCoroutine(resetDamageTint_coroutineID);
    }

    /**
     * Link the paddle central class.<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link PaddlePrefab}
     * as part of component linking process.</i></b>
     *
     * @param paddle The paddle central class.
     */
    public void linkPaddle(PlayerPaddle paddle) {
        this.paddle = paddle;
    }

    /**
     * Called when {@link PaddleHealth#onHealthChanged} is invoked.<br><br>
     * This function shows a brief red tint when {@link PlayerPaddle} is damaged.
     *
     * @param sender Event caller {@link PaddleHealth}.
     * @param e      Event argument containing data about the health change.
     */
    private void paddleHealth_onHealthChanged(Object sender, EntityHealth.OnHealthChangedEventArgs e) {
        if (e.alterType.isDamage()) {
            renderer.setOverlayColor(Color.RED);
            resetDamageTint_coroutineID
                    = Time.addCoroutine(this::resetDamageTint, Time.getTime() + DAMAGE_FLASHING_TIME);
        }
    }

    private void resetDamageTint() {
        renderer.setOverlayColor(Color.WHITE);
    }

}