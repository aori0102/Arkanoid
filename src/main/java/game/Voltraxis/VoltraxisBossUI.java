package game.Voltraxis;

import org.GameObject;
import org.MonoBehaviour;

public class VoltraxisBossUI extends MonoBehaviour {

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public VoltraxisBossUI(GameObject owner) {
        super(owner);
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return new VoltraxisBossUI(newOwner);
    }

    @Override
    protected void destroyComponent() {

    }
}
