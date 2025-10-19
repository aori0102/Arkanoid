package game.Voltraxis;

import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import utils.Time;

/**
 * Class to handling charging state for Voltraxis towards unleashing
 * EX skill.
 */
public class VoltraxisCharging extends MonoBehaviour {

    private Voltraxis voltraxis = null;
    private VoltraxisChargingUI voltraxisChargingUI = null;
    private boolean isCharging = false;
    private Time.CoroutineID haltDelayCoroutineID = null;

    /**
     * <b>Read-ony</b>
     */
    private VoltraxisData.ChargingState a = VoltraxisData.ChargingState.None;

    /**
     * <b>Read-only. Write via {@link #setCurrentChargingState}.</b>
     */
    private VoltraxisData.ChargingState _currentChargingState = VoltraxisData.ChargingState.None;

    /**
     * <b>Read-only. Write via {@link #setCurrentCharge}.</b>
     */
    private double _currentCharge = 0.0;

    public EventHandler<Void> onChargingLow = new EventHandler<>(this);
    public EventHandler<Void> onChargingMedium = new EventHandler<>(this);
    public EventHandler<Void> onChargingHigh = new EventHandler<>(this);
    public EventHandler<Void> onChargingFull = new EventHandler<>(this);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public VoltraxisCharging(GameObject owner) {
        super(owner);
    }

    @Override
    protected void destroyComponent() {
    }

    @Override
    public void update() {
        if (isCharging) {
            setCurrentCharge(_currentCharge + VoltraxisData.CHARGING_RATE * Time.deltaTime);
            voltraxisChargingUI.setTargetRatio(_currentCharge / VoltraxisData.CHARGING_MAX);
        }
    }

    /**
     * Link the central brain of Voltraxis to this object.<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link VoltraxisPrefab}
     * as part of component linking process.</i></b>
     *
     * @param voltraxis The central brain of Voltraxis.
     */
    public void linkVoltraxis(Voltraxis voltraxis) {
        this.voltraxis = voltraxis;
    }

    /**
     * Link the charging UI to this object.<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link VoltraxisPrefab}
     * as part of component linking process.</i></b>
     *
     * @param voltraxisChargingUI The charging UI to link.
     */
    public void linkVoltraxisChargingUI(VoltraxisChargingUI voltraxisChargingUI) {
        this.voltraxisChargingUI = voltraxisChargingUI;
    }

    /**
     * Start charging towards EX for Voltraxis.<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link Voltraxis}
     * to start charging (when groggy is full).</i></b>
     */
    public void startCharging() {
        isCharging = true;
        onChargingLow.invoke(this, null);
    }

    /**
     * Halt the charging process by
     * {@link VoltraxisData#CHARGING_HALT_AMOUNT}, then
     * delay charging for a set amount of time as defined in
     * {@link VoltraxisData#CHARGING_HALT_DELAY}.<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link Voltraxis}
     * when one of the two power cores is destroyed.</i></b>
     */
    public void haltCharging() {

        isCharging = false;
        setCurrentCharge(_currentCharge - VoltraxisData.CHARGING_HALT_AMOUNT);

        // Delay before charging again
        haltDelayCoroutineID = Time.addCoroutine(() -> isCharging = true, Time.time + VoltraxisData.CHARGING_HALT_DELAY);

    }

    /**
     * Set the current charge and update charging state accordingly.
     *
     * @param amount The amount to set.
     */
    private void setCurrentCharge(double amount) {
        _currentCharge = amount;
        if (_currentCharge > VoltraxisData.ChargingState.Medium.chargePoint) {
            setCurrentChargingState(VoltraxisData.ChargingState.High);
        } else if (_currentCharge > VoltraxisData.ChargingState.Low.chargePoint) {
            setCurrentChargingState(VoltraxisData.ChargingState.Medium);
        } else if (_currentCharge > VoltraxisData.ChargingState.None.chargePoint) {
            setCurrentChargingState(VoltraxisData.ChargingState.Low);
        } else {
            setCurrentChargingState(VoltraxisData.ChargingState.None);
        }
    }

    /**
     * Switch charging state and fire event accordingly.
     *
     * @param currentChargingState The value to set.
     */
    private void setCurrentChargingState(VoltraxisData.ChargingState currentChargingState) {
        if (_currentChargingState != currentChargingState) {
            _currentChargingState = currentChargingState;
            switch (_currentChargingState) {
                case Low -> onChargingLow.invoke(this, null);
                case Medium -> onChargingMedium.invoke(this, null);
                case High -> onChargingHigh.invoke(this, null);
            }
        }
    }

    /**
     * Terminate the charging process and deactivate the
     * charging UI.<br><br>
     * <b><i><u>NOTE</u> : Use within {@link Voltraxis}
     * when both power cores are destroyed, forcing Voltraxis
     * into weakened state.</i></b>
     */
    public void terminateCharging() {

        isCharging = false;
        setCurrentCharge(0.0);
        voltraxisChargingUI.resetChargingBar();

        // Remove delay coroutine in case this function is called within halting
        if (haltDelayCoroutineID != null) {
            Time.removeCoroutine(haltDelayCoroutineID);
            haltDelayCoroutineID = null;
        }

    }

}