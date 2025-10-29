package game.Voltraxis.Object;

import game.Voltraxis.Prefab.VoltraxisPrefab;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Rendering.SpriteRenderer;
import utils.MathUtils;
import utils.Time;

/**
 * Health bar UI component for {@link PowerCore}.
 */
public class PowerCoreHealthBar extends MonoBehaviour {

    private static final double HEALTH_BAR_UPDATE_RATE = 10.221;
    private static final double HEALTH_LOST_UPDATE_DELAY = 1.692;

    private SpriteRenderer fillRemain = null;
    private SpriteRenderer fillLost = null;
    private PowerCore powerCore = null;

    private double ratio = 1.0;
    private double healthLostRatio = 1.0;
    private double healthRemainRatio = 1.0;
    private double lastHealthChangeTick = 0.0;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PowerCoreHealthBar(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {

        fillLost.setFillAmount(healthLostRatio);
        fillRemain.setFillAmount(healthRemainRatio);

    }

    @Override
    public void update() {

        healthRemainRatio
                = MathUtils.lerp(healthRemainRatio, ratio, Time.getDeltaTime() * HEALTH_BAR_UPDATE_RATE);
        fillRemain.setFillAmount(healthRemainRatio);

        if (Time.getTime() > lastHealthChangeTick + HEALTH_LOST_UPDATE_DELAY) {
            healthLostRatio
                    = MathUtils.lerp(healthLostRatio, ratio, Time.getDeltaTime() * HEALTH_BAR_UPDATE_RATE);
            fillLost.setFillAmount(healthLostRatio);
        }

    }

    /**
     * Called when {@link PowerCore#onHealthChanged} is invoked. This function
     * updates the ratio between health and max health for filling.
     *
     * @param sender {@link PowerCore}.
     * @param e      Empty event argument.
     */
    private void powerCore_onHealthChanged(Object sender, Void e) {
        ratio = (double) powerCore.getHealth() / powerCore.getMaxHealth();
        lastHealthChangeTick = Time.getTime();
    }

    /**
     * Set the fill bar renderer for the health bar UI.<br><br>
     * <b><i><u>NOTE: </u> Only use within {@link VoltraxisPrefab}
     * as a component linking process.</i></b>
     *
     * @param fillRemain The fill bar renderer to set.
     */
    public void setFillRemain(SpriteRenderer fillRemain) {
        this.fillRemain = fillRemain;
    }

    /**
     * Set the fill lost bar renderer for the health bar UI, which
     * is the bar under the main bar to indicate the amount of HP lost.<br><br>
     * <b><i><u>NOTE: </u> Only use within {@link VoltraxisPrefab}
     * as a component linking process.</i></b>
     *
     * @param fillLost The fill bar renderer to set.
     */
    public void setFillLost(SpriteRenderer fillLost) {
        this.fillLost = fillLost;
    }

    /**
     * Set the main power core component.<br><br>
     * <b><i><u>NOTE:</u> Only use within {@link VoltraxisPrefab}
     * as a component linking process.</i></b>
     *
     * @param powerCore The fill bar renderer to set.
     */
    public void setPowerCore(PowerCore powerCore) {
        powerCore.onHealthChanged.addListener(this::powerCore_onHealthChanged);
        this.powerCore = powerCore;
    }

}