package game.Voltraxis.PowerCore;

import org.GameObject;
import org.MonoBehaviour;
import org.SpriteRenderer;
import utils.MathUtils;
import utils.Time;

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
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
    }

    @Override
    public void awake() {

        fillLost.setFillAmount(healthLostRatio);
        fillRemain.setFillAmount(healthRemainRatio);

    }

    @Override
    public void update() {

        healthRemainRatio
                = MathUtils.lerp(healthRemainRatio, ratio, Time.deltaTime * HEALTH_BAR_UPDATE_RATE);
        fillRemain.setFillAmount(healthRemainRatio);

        if (Time.time > lastHealthChangeTick + HEALTH_LOST_UPDATE_DELAY) {
            healthLostRatio
                    = MathUtils.lerp(healthLostRatio, ratio, Time.deltaTime * HEALTH_BAR_UPDATE_RATE);
            fillLost.setFillAmount(healthLostRatio);
        }

    }

    @Override
    protected void destroyComponent() {
        fillLost = null;
        fillRemain = null;
        powerCore = null;
    }

    private void powerCore_onHealthChanged(Object sender, Void e) {
        ratio = (double) powerCore.getHealth() / powerCore.getMaxHealth();
        lastHealthChangeTick = Time.time;
    }

    public void setFillRemain(SpriteRenderer fillRemain) {
        this.fillRemain = fillRemain;
    }

    public void setFillLost(SpriteRenderer fillLost) {
        this.fillLost = fillLost;
    }

    public void setPowerCore(PowerCore powerCore) {
        powerCore.onHealthChanged.addListener(this::powerCore_onHealthChanged);
        this.powerCore = powerCore;
    }

}