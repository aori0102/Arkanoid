package game.Brick;

import game.Brick.BrickGenMap.MapStyle;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Physics.BoxCollider;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

public class BrickManager extends MonoBehaviour {

    private static final Vector2 BRICK_SIZE = new Vector2(80.0, 20.0);

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
        instance = this;
        brickFactory = new BrickFactory(10, 10, MapStyle.CAVES, 0.3);
        brickFactory.setup();
    }

    public Brick instantiateBrick() {

        var brick = GameObjectManager.instantiate("Brick").addComponent(Brick.class);
        var renderer = brick.addComponent(SpriteRenderer.class);
        renderer.setImage(ImageAsset.ImageIndex.GreenBrick.getImage());
        renderer.setPivot(new Vector2(0.5, 0.5));
        renderer.setSize(BRICK_SIZE);
        brick.addComponent(BoxCollider.class).setLocalSize(BRICK_SIZE);

        return brick;

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
