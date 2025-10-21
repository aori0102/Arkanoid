package game.Brick.BrickEvent;

import game.Brick.BrickFactory;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

public class BrickManager extends MonoBehaviour {
    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public BrickManager(GameObject owner) {
        super(owner);
    }

    public BrickFactory brickFactory;

    public void update() {
        if (brickFactory != null) {
            brickFactory.runProgress();
        }
    }

    @Override
    protected void destroyComponent() {

    }
}
