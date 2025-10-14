package game.object;

import org.GameObject;
import org.MonoBehaviour;
import org.SpriteRenderer;

public class Arrow extends MonoBehaviour {
    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */

    public Arrow(GameObject owner) {
        super(owner);
        addComponent(SpriteRenderer.class);
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
    }

    @Override
    protected void destroyComponent() {

    }
}
