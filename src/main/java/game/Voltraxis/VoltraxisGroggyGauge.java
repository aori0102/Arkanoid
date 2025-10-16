package game.Voltraxis;

import org.EventHandler;
import org.GameObject;
import org.MonoBehaviour;
import org.SpriteRenderer;
import utils.MathUtils;
import utils.Time;

public class VoltraxisGroggyGauge extends MonoBehaviour {

    private static final double MAX_GROGGY = 1.0;
    private static final double GROGGY_BAR_CHANGE_RATE = 5.962;

    private SpriteRenderer fill = null;
    private Voltraxis voltraxis = null;
    private double groggy = 0.0;
    private double ratio = 0.0;

    public EventHandler<Void> onGroggyReachedMax = new EventHandler<>(this);
    public EventHandler<Void> onGroggyToDeployPowerCore = new EventHandler<>(this);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public VoltraxisGroggyGauge(GameObject owner) {
        super(owner);
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return new VoltraxisGroggyGauge(newOwner);
    }

    @Override
    protected void destroyComponent() {
        fill = null;
        voltraxis = null;
    }

    @Override
    public void update() {
        ratio = MathUtils.lerp(ratio, groggy / MAX_GROGGY, Time.deltaTime * GROGGY_BAR_CHANGE_RATE);
        fill.setFillAmount(ratio);
    }

    protected void setFill(SpriteRenderer fill) {
        this.fill = fill;
    }

    protected void setVoltraxis(Voltraxis voltraxis) {
        this.voltraxis = voltraxis;
        voltraxis.onDamaged.addListener(this::voltraxis_onDamaged);
    }

    private void voltraxis_onDamaged(Object sender, Void e) {

        if (isMaxGroggy()) {
            return;
        }

        groggy += VoltraxisData.GROGGY_DELTA;
        if (isMaxGroggy()) {
            onGroggyReachedMax.invoke(this, null);
        }

    }

    private boolean isMaxGroggy() {
        return groggy >= MAX_GROGGY;
    }

}