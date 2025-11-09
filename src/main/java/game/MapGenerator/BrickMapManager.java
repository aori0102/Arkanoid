package game.MapGenerator;

import game.Brick.Brick;
import game.Brick.BrickEvent.BrickEvent;
import game.Brick.BrickEvent.EventType;
import game.Brick.BrickGenMap.GenMap;
import game.PlayerData.DataManager;
import game.PlayerData.ProgressData;
import game.PowerUp.Index.PowerUpManager;
import org.Event.EventActionID;
import org.Event.EventHandler;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;
import utils.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// TODO: Doc
public final class BrickMapManager extends MonoBehaviour {

    public static final int ROW_COUNT = 8;
    public static final int COLUMN_COUNT = 10;
    private static final Vector2 BRICK_MAP_ANCHOR = new Vector2(300.0, 29.0);
    private static final Vector2 BRICK_OFFSET = new Vector2(68.0, 36.0);

    private record Cell(int row, int column) {
    }

    private static BrickMapManager instance = null;
    private final List<List<Brick>> brickGrid = new ArrayList<>();
    private final HashMap<Brick, Cell> brickCoordinateMap = new HashMap<>();

    private final GenMap mapGenerator = new GenMap(ROW_COUNT, COLUMN_COUNT);
    private final BrickEvent brickEvent = new BrickEvent(ROW_COUNT, COLUMN_COUNT, brickGrid);

    private EventActionID brick_onAnyBrickDestroyed_ID = null;
    private EventActionID brick_onAnyBrickHit_ID = null;

    private int brickDestroyed = 0;

    public EventHandler<Void> onMapCleared = new EventHandler<>(BrickMapManager.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public BrickMapManager(GameObject owner) {

        super(owner);

        if (instance != null) {
            throw new ReinitializedSingletonException("BrickMapManager is a singleton!");
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
        brick_onAnyBrickHit_ID = Brick.onAnyBrickHit.addListener(
                this::brick_onAnyBrickHit
        );
        loadSave();
    }

    @Override
    protected void onDestroy() {
        Brick.onAnyBrickDestroyed.removeListener(brick_onAnyBrickDestroyed_ID);
        Brick.onAnyBrickDestroyed.removeListener(brick_onAnyBrickHit_ID);
        instance = null;
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

    private void loadSave() {
        brickDestroyed = DataManager.getInstance().getSave().getBrickDestroyed();
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
            if (cell == null) {
                throw new RuntimeException("Removing a brick which was not registered!");
            }
            brickDestroyed++;
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
                brickGrid.get(row).set(column, null);
                if (brick != null) {
                    GameObjectManager.destroy(brick.getGameObject());
                }
            }

        }
        brickCoordinateMap.clear();

    }

    /**
     * Called when {@link Brick#onAnyBrickHit} is invoked.<br><br>
     *
     * @param sender
     * @param e
     */
    private void brick_onAnyBrickHit(Object sender, Void e) {
        if (sender instanceof Brick brick) {
            var cell = brickCoordinateMap.get(brick);
            if (cell == null) {
                throw new RuntimeException("Hit stranded brick (not assigned into grid)");
            }
            var row = cell.row;
            var col = cell.column;
            var brickType = brick.getBrickType();
            switch (brickType) {
                case Reborn -> brickEvent.getStartEvent(EventType.Reborn, row, col);
                case Rock -> brickEvent.getStartEvent(EventType.Rock, row, col);
                case Rocket -> brickEvent.getStartEvent(EventType.Rocket, row, col);
                case Gift -> brickEvent.getStartEvent(EventType.Gift, row, col);
                case Angel -> brickEvent.getStartEvent(EventType.Angel, row, col);
                case Bomb -> brickEvent.getStartEvent(EventType.Bomb, row, col);
                default -> {
                }
            }
        }
    }

    @Override
    public void update() {
        brickEvent.executeEvent();
    }

    public int getBrickDestroyed() {
        return brickDestroyed;
    }

}