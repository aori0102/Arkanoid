package game.Voltraxis;

import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Rendering.SpriteRenderer;
import org.Text.TextUI;
import utils.MathUtils;
import utils.Time;

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
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
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

    public void setHealthText(TextUI healthText) {
        this.healthText = healthText;
    }

    public void setHealthLostImage(SpriteRenderer healthRemainImage) {
        this.healthLostImage = healthRemainImage;
    }

    public void setHealthRemainImage(SpriteRenderer healthRemainImage) {
        this.healthRemainImage = healthRemainImage;
    }

    public void setVoltraxis(Voltraxis voltraxis) {
        this.voltraxis = voltraxis;
        healthText.setText(voltraxis.getHealth() + " / " + VoltraxisData.BASE_MAX_HEALTH);
        voltraxis.onHealthChanged.addListener(this::voltraxis_onHealthChanged);
    }

    private void voltraxis_onHealthChanged(Object sender, Void e) {
        ratio = (double) voltraxis.getHealth() / VoltraxisData.BASE_MAX_HEALTH;
        healthText.setText(voltraxis.getHealth() + " / " + VoltraxisData.BASE_MAX_HEALTH);
        lastHealthChangeTick = Time.time;
    }

}
