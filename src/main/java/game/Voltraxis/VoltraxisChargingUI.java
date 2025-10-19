package game.Voltraxis;

import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

public class VoltraxisChargingUI extends MonoBehaviour {

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
    protected void destroyComponent() {

    }

    public void setTargetRatio(double targetRatio) {
        this.targetRatio = targetRatio;
    }

    public void resetChargingBar() {
        ratio = 0.0;
        targetRatio = 0.0;
    }

}