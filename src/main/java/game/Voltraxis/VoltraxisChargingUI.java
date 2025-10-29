package game.Voltraxis;

import game.Voltraxis.Prefab.VoltraxisPrefab;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Rendering.SpriteRenderer;
import utils.MathUtils;
import utils.Time;

public class VoltraxisChargingUI extends MonoBehaviour {

    private static final double CHARGING_UI_CHANGE_RATE = 6.992;

    private SpriteRenderer fillRenderer = null;
    private GameObject uiObject = null;
    private double ratio = 0.0;
    private double targetRatio = 0.0;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public VoltraxisChargingUI(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {

        uiObject.setActive(false);

        Voltraxis.getInstance().getVoltraxisCharging().onChargingEntered
                .addListener(this::voltraxisCharging_onChargingStarted);
        Voltraxis.getInstance().getVoltraxisCharging().onChargingTerminated
                .addListener(this::voltraxisCharging_onChargingTerminated);
        Voltraxis.getInstance().getVoltraxisCharging().onChargingRatioChanged
                .addListener(this::voltraxisCharging_onChargingRatioChanged);

    }

    @Override
    public void update() {
        ratio = MathUtils.lerp(ratio, targetRatio, Time.getDeltaTime() * CHARGING_UI_CHANGE_RATE);
        fillRenderer.setFillAmount(ratio);
    }

    /**
     * Called when {@link VoltraxisCharging#onChargingEntered} is invoked.<br><br>
     * This function enables the UI for the charging.
     */
    private void voltraxisCharging_onChargingStarted(Object sender, Void e) {
        uiObject.setActive(true);
        fillRenderer.setFillAmount(0.0);
        ratio = 0.0;
        targetRatio = 0.0;
    }

    /**
     * Called when {@link VoltraxisCharging#onChargingTerminated} is invoked.<br><br>
     * This function disables the UI for charging.
     */
    private void voltraxisCharging_onChargingTerminated(Object sender, Void e) {
        uiObject.setActive(false);
    }

    /**
     * Called when {@link VoltraxisCharging#onChargingRatioChanged} is invoked.<br><br>
     * This function updates the charging bar UI to reflects the charging status.
     */
    private void voltraxisCharging_onChargingRatioChanged(Object sender, Double e) {
        targetRatio = e;
    }

    /**
     * Link the charging fill bar to this UI controller.<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link VoltraxisPrefab}
     * as part of component linking process.</i></b>
     *
     * @param fillRenderer The fill renderer.
     */
    public void linkFillRenderer(SpriteRenderer fillRenderer) {
        this.fillRenderer = fillRenderer;
    }

    /**
     * Link the UI parent object with this object<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link VoltraxisPrefab}
     * as part of component linking process.</i></b>
     *
     * @param uiObject The UI object to be set.
     */
    public void linkUIObject(GameObject uiObject) {
        this.uiObject = uiObject;
    }

}