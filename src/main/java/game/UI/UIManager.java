package game.UI;

import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

public class UIManager extends MonoBehaviour {
    private static UIManager instance;


    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public UIManager(GameObject owner) {
        super(owner);

        if (instance == null) {
            instance = this;
        }
    }

    public static UIManager getInstance(){
        return instance;
    }

    @Override
    protected void destroyComponent() {

    }
}
