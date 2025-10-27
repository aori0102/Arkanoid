package game.BrickObj;

import game.BrickObj.BrickEvent.CollisionEvent;
import game.BrickObj.BrickGenMap.MapStyle;
import org.Event.EventHandler;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Physics.BoxCollider;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

public class BrickManager extends MonoBehaviour {

    public static final Vector2 BRICK_SIZE = new Vector2(64.0, 32.0);
    public static final Vector2 BRICK_MAP_ANCHOR = new Vector2(300.0, 100.0);

    private static BrickManager instance = null;

    private BrickFactory brickFactory = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public BrickManager(GameObject owner) {
        super(owner);
        if (instance != null) {
            throw new IllegalStateException("BrickManager is a singleton!");
        }
        brickFactory = new BrickFactory(10, 10, MapStyle.RANDOM, 0.3);
        instance = this;
    }

    public static BrickManager getInstance() {
        return instance;
    }

    @Override
    public void update() {
        brickFactory.runProgress();
    }

    @Override
    protected void destroyComponent() {

    }

}