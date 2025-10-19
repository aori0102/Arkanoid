package game.Voltraxis;

import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Rendering.SpriteRenderer;
import org.Text.TextUI;
import utils.MathUtils;
import utils.Time;

/**
 * Health bar UI.
 */
public class VoltraxisHealthBar extends MonoBehaviour {

    private static final double HEALTH_BAR_UPDATE_RATE = 9.133;
    private static final double HEALTH_LOST_UPDATE_DELAY = 2.113;

    private TextUI healthText = null;
    private SpriteRenderer healthLostImage = null;
    private SpriteRenderer healthRemainImage = null;
    private Voltraxis voltraxis = null;

    private double ratio = 1.0;
    private double healthLostRatio = 1.0;
    private double healthRemainRatio = 1.0;
    private double lastHealthChangeTick = 0.0;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public VoltraxisHealthBar(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {

        healthLostImage.setFillType(SpriteRenderer.FillType.Horizontal_LeftToRight);
        healthRemainImage.setFillType(SpriteRenderer.FillType.Horizontal_LeftToRight);
        healthLostImage.setFillAmount(healthLostRatio);
        healthRemainImage.setFillAmount(healthRemainRatio);

    }

    @Override
    public void update() {

        healthLostRatio
                = MathUtils.lerp(healthLostRatio, ratio, Time.deltaTime * HEALTH_BAR_UPDATE_RATE);
        healthRemainImage.setFillAmount(healthLostRatio);

        if (Time.time > lastHealthChangeTick + HEALTH_LOST_UPDATE_DELAY) {
            healthRemainRatio
                    = MathUtils.lerp(healthRemainRatio, ratio, Time.deltaTime * HEALTH_BAR_UPDATE_RATE);
            healthLostImage.setFillAmount(healthRemainRatio);
        }

    }

    @Override
    protected void destroyComponent() {
        healthText = null;
        healthLostImage = null;
        healthRemainImage = null;
        voltraxis = null;
    }

    /**
     * Set the health text component for this health bar UI.<br><br>
     * <b><i><u>NOTE:</u> Only use within {@link VoltraxisPrefab}
     * as a component linking process.</i></b>
     *
     * @param healthText The health state ot set.
     */
    public void setHealthText(TextUI healthText) {
        this.healthText = healthText;
    }

    /**
     * Set the health lost fill renderer for this health bar UI,
     * which is the yellow bar under the main fill to indicate
     * the amount of lost HP of Voltraxis.<br><br>
     * <b><i><u>NOTE:</u> Only use within {@link VoltraxisPrefab}
     * as a component linking process.</i></b>
     *
     * @param healthLostImage The health lost fill renderer.
     */
    public void setHealthLostImage(SpriteRenderer healthLostImage) {
        this.healthLostImage = healthLostImage;
    }

    /**
     * Set the health remain fill renderer for this health bar UI,
     * which is the amount of current HP of Voltraxis.<br><br>
     * <b><i><u>NOTE:</u> Only use within {@link VoltraxisPrefab}
     * as a component linking process.</i></b>
     *
     * @param healthRemainImage The health remain fill renderer.
     */
    public void setHealthRemainImage(SpriteRenderer healthRemainImage) {
        this.healthRemainImage = healthRemainImage;
    }

    /**
     * Set the central class {@link Voltraxis} of the boss and listen
     * to {@link Voltraxis#onHealthChanged}. This function also
     * initialize the health text according to Voltraxis' HP.
     *
     * @param voltraxis The central boss class.
     */
    public void setVoltraxis(Voltraxis voltraxis) {
        this.voltraxis = voltraxis;
        healthText.setText(voltraxis.getHealth() + " / " + VoltraxisData.BASE_MAX_HEALTH);
        voltraxis.onHealthChanged.addListener(this::voltraxis_onHealthChanged);
    }

    /**
     * Update {@link #ratio} between Voltraxis' current and max
     * HP and modify {@link #healthText} accordingly.<br><br>
     * <b>Called when {@link Voltraxis#onHealthChanged} is invoked.</b>
     *
     * @param sender {@link Voltraxis}.
     * @param e      Empty event argument.
     */
    private void voltraxis_onHealthChanged(Object sender, Void e) {
        ratio = (double) voltraxis.getHealth() / VoltraxisData.BASE_MAX_HEALTH;
        healthText.setText(voltraxis.getHealth() + " / " + VoltraxisData.BASE_MAX_HEALTH);
        lastHealthChangeTick = Time.time;
    }

}
