package game.MapGenerator;

import game.Brick.Brick;
import org.Event.EventHandler;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Physics.BoxCollider;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class BrickMapManager extends MonoBehaviour {

    private static final int ROW_COUNT = 10;
    private static final int COLUMN_COUNT = 10;
    private static final Vector2 BRICK_SIZE = new Vector2(64.0, 32.0);
    private static final Vector2 BRICK_MAP_ANCHOR = new Vector2(300.0, 100.0);
    private static final Vector2 BRICK_OFFSET = new Vector2(2.0, 2.0);

    private record Cell(int row, int column) {
    }

    private static BrickMapManager instance = null;

    private final List<List<Brick>> brickGrid = new ArrayList<>();
    private final HashMap<Brick, Cell> brickCoordinateMap = new HashMap<>();

    public EventHandler<Void> onMapCleared = new EventHandler<>(this);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public BrickMapManager(GameObject owner) {

        super(owner);

        if (instance != null) {
            throw new ReinitializedSingletonException("MapGenerator is a singleton!");
        }
        instance = this;

        for (int i = 0; i < ROW_COUNT; i++) {
            brickGrid.add(new ArrayList<>());
            for (int j = 0; j < COLUMN_COUNT; j++) {
                brickGrid.get(i).add(null);
            }
        }

    }

    @Override
    protected void destroyComponent() {

    }

    public void generateMap() {

        clearMap();

        for (int i = 0; i < ROW_COUNT; i++) {

            for (int j = 0; j < COLUMN_COUNT; j++) {

                var cell = new Cell(i, j);
                var brick = instantiateBrick();
                var position = BRICK_MAP_ANCHOR.add((BRICK_OFFSET.add(BRICK_SIZE)).scaleUp(new Vector2(j, i)));
                brick.getTransform().setGlobalPosition(position);
                brickGrid.get(i).set(j, brick);
                brickCoordinateMap.put(brick, cell);
                brick.onBrickDestroyed.addListener(this::brick_onBrickDestroyed);

            }

        }

    }

    public static BrickMapManager getInstance() {
        return instance;
    }

    /**
     * Called when {@link Brick#onBrickDestroyed} is invoked.<br><br>
     * This function clears the brick data inside the grid.
     */
    private void brick_onBrickDestroyed(Object sender, Void e) {

        if (sender instanceof Brick brick) {
            var cell = brickCoordinateMap.remove(brick);
            brickGrid.get(cell.row).set(cell.column, null);
            brick.onBrickDestroyed.removeAllListeners();
            if (brickCoordinateMap.isEmpty()) {
                onMapCleared.invoke(this, null);
            }
        }

    }

    public Brick instantiateBrick() {

        var brick = GameObjectManager.instantiate("Brick")
                .addComponent(Brick.class);
        var brickRenderer = brick.addComponent(SpriteRenderer.class);
        brickRenderer.setImage(ImageAsset.ImageIndex.GreenBrick.getImage());
        brickRenderer.setSize(BRICK_SIZE);
        brickRenderer.setPivot(new Vector2(0.5, 0.5));
        brick.addComponent(BoxCollider.class).setLocalSize(BRICK_SIZE);

        return brick;

    }

    private void clearMap() {

        for (int i = 0; i < ROW_COUNT; i++) {

            for (int j = 0; j < COLUMN_COUNT; j++) {
                var brick = brickGrid.get(i).get(j);
                brickGrid.get(i).set(j, null);
                if (brick != null) {
                    GameObjectManager.destroy(brick.getGameObject());
                }
            }

        }
        brickCoordinateMap.clear();

    }

}