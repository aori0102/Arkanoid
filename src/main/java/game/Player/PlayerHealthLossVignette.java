package game.Player;

import game.Player.Prefab.PlayerPrefabManager;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Rendering.SpriteRenderer;
import utils.Time;

public final class PlayerHealthLossVignette extends MonoBehaviour {

    private static final double PULSE_TIME = 0.9;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PlayerHealthLossVignette(GameObject owner) {
        super(owner);
    }

    private SpriteRenderer vignetteRenderer = null;
    private double pulseStartTime = 0.0;

    @Override
    public void update() {
        double fadeRatio = (Time.time - pulseStartTime) / PULSE_TIME;
        fadeRatio = Math.clamp(fadeRatio, 0, 1);
        double opacity = Math.pow(Math.sin(Math.PI * fadeRatio), 4);
        vignetteRenderer.setOpacity(opacity);
    }

    /**
     * Link the main player component to this object.<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link PlayerPrefabManager}
     * as part of component linking process.</i></b>
     *
     * @param player The main player component.
     */
    public void linkPlayer(Player player) {
        player.getPlayerHealth().onHealthChanged.addListener(this::player_onHealthChanged);
    }

    /**
     * Link the vignette effect object to this object.<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link PlayerPrefabManager}
     * as part of component linking process.</i></b>
     *
     * @param vignetteRenderer The renderer of the vignette object.
     */
    public void linkVignetteRenderer(SpriteRenderer vignetteRenderer) {
        this.vignetteRenderer = vignetteRenderer;
    }

    /**
     * Called when {@link PlayerHealth#onHealthChanged} is invoked.<br><br>
     * This function display the vignette for a split moment when player health changes.
     */
    private void player_onHealthChanged(Object sender, PlayerHealth.OnHealthChangedEventArgs e) {

        if (e.amount < 0) {
            pulseStartTime = Time.time;
        }

    }

}