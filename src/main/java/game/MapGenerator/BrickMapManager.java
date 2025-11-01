package game.MapGenerator;

import game.Brick.Brick;
import game.Brick.BrickPrefab;
import game.Brick.BrickType;
import game.BrickObj.BrickGenMap.GenMap;
import game.PowerUp.Index.PowerUpManager;
import org.Event.EventActionID;
import org.Event.EventHandler;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import utils.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class BrickMapManager extends MonoBehaviour {

    public static final int ROW_COUNT = 10;
    public static final int COLUMN_COUNT = 10;
    private static final Vector2 BRICK_MAP_ANCHOR = new Vector2(300.0, 5.0);
    private static final Vector2 BRICK_OFFSET = new Vector2(68.0, 36.0);

    private record Cell(int row, int column) {
    }

    private static BrickMapManager instance = null;

    private final GenMap mapGenerator = new GenMap(ROW_COUNT, COLUMN_COUNT);

    private final List<List<Brick>> brickGrid = new ArrayList<>();
    private final HashMap<Brick, Cell> brickCoordinateMap = new HashMap<>();

    private EventActionID brick_onAnyBrickDestroyed_ID = null;

    public EventHandler<Void> onMapCleared = new EventHandler<>(BrickMapManager.class);

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
    public void start() {
        brick_onAnyBrickDestroyed_ID = Brick.onAnyBrickDestroyed.addListener(
                this::brick_onAnyBrickDestroyed
        );
    }

    @Override
    protected void onDestroy() {
        Brick.onAnyBrickDestroyed.removeListener(brick_onAnyBrickDestroyed_ID);
    }

    public void generateMap() {

        clearMap();

        var typeGrid = mapGenerator.generate();

        for (int row = 0; row < ROW_COUNT; row++) {

            for (int column = 0; column < COLUMN_COUNT; column++) {

                var cell = new Cell(row, column);
                var brick = PrefabManager.instantiatePrefab(PrefabIndex.Brick)
                        .getComponent(Brick.class);
                brick.setBrickType(typeGrid.get(row).get(column));
                var position = BRICK_MAP_ANCHOR.add((BRICK_OFFSET).scaleUp(new Vector2(column, row)));
                brick.getTransform().setGlobalPosition(position);
                brickGrid.get(row).set(column, brick);
                brickCoordinateMap.put(brick, cell);

            }

        }

    }

    public static BrickMapManager getInstance() {
        return instance;
    }

    /**
     * Called when {@link Brick#onAnyBrickDestroyed} is invoked.<br><br>
     * This function clears the brick data inside the grid.
     */
    private void brick_onAnyBrickDestroyed(Object sender, Brick.OnBrickDestroyedEventArgs e) {

        if (sender instanceof Brick brick) {
            var cell = brickCoordinateMap.remove(brick);
            brickGrid.get(cell.row).set(cell.column, null);
            PowerUpManager.getInstance().spawnPowerUp(e.brickPosition);
            if (brickCoordinateMap.isEmpty()) {
                onMapCleared.invoke(this, null);
            }
        }

    }

    private void clearMap() {

        for (int row = 0; row < ROW_COUNT; row++) {

            for (int column = 0; column < COLUMN_COUNT; column++) {
                var brick = brickGrid.get(row).get(column);
                brickCoordinateMap.remove(brick);
                brickGrid.get(row).set(column, null);
                if (brick != null) {
                    GameObjectManager.destroy(brick.getGameObject());
                }
            }

        }
        brickCoordinateMap.clear();

    }

}