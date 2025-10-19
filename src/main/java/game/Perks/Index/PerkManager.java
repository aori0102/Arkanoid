package game.Perks.Index;

import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

public class PerkManager extends MonoBehaviour {

    public static PerkManager instance;
    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PerkManager(GameObject owner) {
        super(owner);
        if(instance == null) {
            instance = this;
        }
    }

    @Override
    protected void destroyComponent() {

    }
}
