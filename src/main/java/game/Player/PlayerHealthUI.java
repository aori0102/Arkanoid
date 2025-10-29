package game.Player;

import game.Player.Prefab.PlayerHealthBarPrefab;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.MathUtils;
import utils.Time;

/**
 * UI class to render health bar and lives based on information
 * from {@link PlayerHealth}.
 */
public final class PlayerHealthUI extends MonoBehaviour {

    private static final double HEALTH_BAR_UPDATE_RATE = 7.992;

    private double ratio = 1.0;
    private double targetRatio = 1.0;
    private SpriteRenderer fillRenderer = null;
    private SpriteRenderer[] livesRendererArray = new SpriteRenderer[PlayerHealth.MAX_LIVES];

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PlayerHealthUI(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        Player.getInstance().getPlayerHealth().onHealthChanged.addListener(this::player_onHealthChanged);
        Player.getInstance().getPlayerHealth().onLivesChanged.addListener(this::player_onLivesChanged);
    }

    @Override
    public void update() {
        ratio = MathUtils.lerp(ratio, targetRatio, Time.getDeltaTime() * HEALTH_BAR_UPDATE_RATE);
        fillRenderer.setFillAmount(ratio);
    }

    /**
     * Called when {@link PlayerHealth#onLivesChanged} is invoked.<br><br>
     * This function updates the health UI as player lives change.
     */
    private void player_onLivesChanged(Object sender, Void e) {
        var lives = Player.getInstance().getPlayerHealth().getLives();
        for (int i = 0; i < PlayerHealth.MAX_LIVES; i++) {
            if (i < lives) {
                livesRendererArray[i].setImage(ImageAsset.ImageIndex.Player_UI_HealthBar_LifeRemain.getImage());
            } else {
                livesRendererArray[i].setImage(ImageAsset.ImageIndex.Player_UI_HealthBar_LifeLost.getImage());
            }
            livesRendererArray[i].setSize(PlayerHealthBarPrefab.LIFE_BAR_SIZE);
        }
    }

    /**
     * Called when {@link PlayerHealth#onHealthChanged} is invoked.<br><br>
     * This function updates the health UI as player health changes.
     */
    private void player_onHealthChanged(Object sender, PlayerHealth.OnHealthChangedEventArgs e) {
        targetRatio = (double) Player.getInstance().getPlayerHealth().getHealth() / PlayerHealth.MAX_HEALTH;
    }

    /**
     * Link the health fill bar renderer to this object.<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link PlayerPrefabManager}
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