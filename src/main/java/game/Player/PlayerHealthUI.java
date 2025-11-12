package game.Player;

import game.Entity.EntityHealth;
import game.Player.Paddle.PaddleHealth;
import game.Player.Prefab.PlayerHealthBarPrefab;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.MathUtils;
import utils.Time;

/**
 * UI class to render health bar and lives based on information
 * from {@link PlayerLives}.
 */
public final class PlayerHealthUI extends MonoBehaviour {

    private static final double HEALTH_BAR_UPDATE_RATE = 7.992;

    private double ratio = 1.0;
    private double targetRatio = 1.0;
    private SpriteRenderer fillRenderer = null;
    private SpriteRenderer[] livesRendererArray = new SpriteRenderer[PlayerAttributes.MAX_LIVES];

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PlayerHealthUI(GameObject owner) {
        super(owner);
    }

    @Override
    public void start() {
        Player.getInstance().getPlayerLives().onLivesDecreased
                .addListener(this::playerLives_onLivesChanged);
        Player.getInstance().getPlayerPaddle().getPaddleHealth().onHealthChanged
                .addListener(this::paddleHealth_onHealthChanged);
    }

    @Override
    public void update() {
        ratio = MathUtils.lerp(ratio, targetRatio, Time.getDeltaTime() * HEALTH_BAR_UPDATE_RATE);
        fillRenderer.setFillAmount(ratio);
    }

    /**
     * Called when {@link PlayerLives#onLivesDecreased} is invoked.<br><br>
     * This function updates the health UI as player lives change.
     */
    private void playerLives_onLivesChanged(Object sender, Void e) {
        var lives = Player.getInstance().getPlayerLives().getLives();
        for (int i = 0; i < PlayerAttributes.MAX_LIVES; i++) {
            if (i < lives) {
                livesRendererArray[i].setImage(ImageAsset.ImageIndex.Player_UI_HealthBar_LifeRemain.getImage());
            } else {
                livesRendererArray[i].setImage(ImageAsset.ImageIndex.Player_UI_HealthBar_LifeLost.getImage());
            }
            livesRendererArray[i].setSize(PlayerHealthBarPrefab.LIFE_BAR_SIZE);
        }
    }

    /**
     * Called when {@link PaddleHealth#onHealthChanged} is invoked.<br><br>
     * This function display the vignette for a split moment when the paddle takes damage.
     *
     * @param sender Event caller {@link PaddleHealth}.
     * @param e      Empty event argument.
     */
    private void paddleHealth_onHealthChanged(Object sender, EntityHealth.OnHealthChangedEventArgs e) {
        var currentHealth = Player.getInstance().getPlayerPaddle().getPaddleHealth().getHealth();
        targetRatio = (double) currentHealth / PlayerAttributes.MAX_HEALTH;
    }

    /**
     * Link the health fill bar renderer to this object.<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link game.Player.Prefab.PlayerPrefab}
     * as part of component linking process.</i></b>
     *
     * @param fillRenderer The health fill bar renderer.
     */
    public void linkFillRenderer(SpriteRenderer fillRenderer) {
        this.fillRenderer = fillRenderer;
    }

    /**
     * Link all lives renderer to this object.
     *
     * @param livesRenderers The lives renderer of the health bar.
     */
    public void linkLivesRenderers(SpriteRenderer... livesRenderers) {
        this.livesRendererArray = livesRenderers;
    }

}