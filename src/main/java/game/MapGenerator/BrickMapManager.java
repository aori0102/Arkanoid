package game.MapGenerator;

import game.Brick.Brick;
import game.Brick.BrickEvent.BrickEvent;
import game.Brick.BrickEvent.EventType;
import game.Brick.BrickGenMap.GenMap;
import game.Player.PlayerData.DataManager;
import game.Player.PlayerData.IPlayerProgressHolder;
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

/**
 * BrickMapManager handles the creation, management, and events
 * of the brick grid for a level.
 * <p>
 * Responsibilities:
 * - Generate brick maps based on GenMap.
 * - Track destroyed bricks.
 * - Handle brick hit and destruction events.
 * - Notify when the entire map is cleared.
 * </p>
 */
public final class BrickMapManager extends MonoBehaviour implements
        IPlayerProgressHolder {

    /** Number of rows in the brick grid. */
    public static final int ROW_COUNT = 8;

    /** Number of columns in the brick grid. */
    public static final int COLUMN_COUNT = 10;

    /** Top-left anchor position of the brick map. */
    private static final Vector2 BRICK_MAP_ANCHOR = new Vector2(300.0, 29.0);

    /** Distance between bricks horizontally and vertically. */
    private static final Vector2 BRICK_OFFSET = new Vector2(68.0, 36.0);

    /**
     * Internal record to store brick cell coordinates.
     */
    private record Cell(int row, int column) {
    }

    /** Singleton instance of BrickMapManager. */
    private static BrickMapManager instance = null;

    /** 2D list representing the brick grid. */
    private final List<List<Brick>> brickGrid = new ArrayList<>();

    /** Map of Brick to its cell coordinates for easy lookup. */
    private final HashMap<Brick, Cell> brickCoordinateMap = new HashMap<>();

    /** Map generator used to produce brick layouts. */
    private final GenMap mapGenerator = new GenMap(ROW_COUNT, COLUMN_COUNT);

    /** Handles brick events like wave, wheel, rocket, etc. */
    private final BrickEvent brickEvent = new BrickEvent(ROW_COUNT, COLUMN_COUNT, brickGrid);

    /** Event listener IDs for removing listeners on destroy. */
    private EventActionID brick_onAnyBrickDestroyed_ID = null;
    private EventActionID brick_onAnyBrickHit_ID = null;

    /** Count of destroyed bricks in the current level. */
    private int brickDestroyed = 0;

    /** Event triggered when all bricks are destroyed in a map. */
    public EventHandler<Void> onMapCleared = new EventHandler<>(BrickMapManager.class);

    /**
     * Create this MonoBehaviour and initialize the brick grid.
     *
     * @param owner The owner GameObject of this component.
     */
    public BrickMapManager(GameObject owner) {
        super(owner);

        if (instance != null) {
            throw new ReinitializedSingletonException("BrickMapManager is a singleton!");
        }
        instance = this;

        // Initialize empty brick grid
        for (int i = 0; i < ROW_COUNT; i++) {
            brickGrid.add(new ArrayList<>());
            for (int j = 0; j < COLUMN_COUNT; j++) {
                brickGrid.get(i).add(null);
            }
        }
    }

    /**
     * Start is called after awake.
     * Registers brick events for destruction and hits.
     */
    @Override
    public void start() {
        brick_onAnyBrickDestroyed_ID = Brick.onAnyBrickDestroyed.addListener(
                this::brick_onAnyBrickDestroyed
        );
        brick_onAnyBrickHit_ID = Brick.onAnyBrickHit.addListener(
                this::brick_onAnyBrickHit
        );
    }

    /**
     * On destroy, remove event listeners and clear singleton.
     */
    @Override
    protected void onDestroy() {
        Brick.onAnyBrickDestroyed.removeListener(brick_onAnyBrickDestroyed_ID);
        Brick.onAnyBrickDestroyed.removeListener(brick_onAnyBrickHit_ID);
        instance = null;
    }

    /**
     * Generates a new brick map using GenMap and positions bricks on screen.
     */
    public void generateMap() {

        clearMap();

        var typeGrid = mapGenerator.generate();

        for (int row = 0; row < ROW_COUNT; row++) {

            for (int column = 0; column < COLUMN_COUNT; column++) {

                var cell = new Cell(row, column);
                var brick = PrefabManager.instantiatePrefab(PrefabIndex.Brick)
                        .getComponent(Brick.class);
                brick.setBrickType(typeGrid.get(row).get(column));
                var position = BRICK_MAP_ANCHOR.add(BRICK_OFFSET.scaleUp(new Vector2(column, row)));
                brick.getTransform().setGlobalPosition(position);
                brickGrid.get(row).set(column, brick);
                brickCoordinateMap.put(brick, cell);

            }

        }

    }

    /** Loads saved progress of destroyed bricks. */
    @Override
    public void loadProgress() {
        brickDestroyed = DataManager.getInstance().getProgress().getBrickDestroyed();
    }

    /** Returns the singleton instance of BrickMapManager. */
    public static BrickMapManager getInstance() {
        return instance;
    }

    /**
     * Handles a brick being destroyed.
     * Removes the brick from the grid and coordinate map,
     * spawns any associated power-ups, and invokes onMapCleared if empty.
     *
     * @param sender Event caller (Brick).
     * @param e Event arguments containing brick position.
     */
    private void brick_onAnyBrickDestroyed(Object sender, Brick.OnBrickDestroyedEventArgs e) {

        if (sender instanceof Brick brick) {
            var cell = brickCoordinateMap.remove(brick);
            if (cell == null) {
                return;
            }
            brickDestroyed++;
            brickGrid.get(cell.row).set(cell.column, null);
            PowerUpManager.getInstance().spawnPowerUp(e.brickPosition);
            if (brickCoordinateMap.isEmpty()) {
                onMapCleared.invoke(this, null);
            }
        }

    }

    /**
     * Clears the entire brick map and destroys all brick GameObjects.
     */
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
     * Handles a brick being hit.
     * Triggers any appropriate BrickEvent based on the brick type.
     *
     * @param sender Event caller (Brick).
     * @param e Empty event argument.
     */
    private void brick_onAnyBrickHit(Object sender, Void e) {
        if (sender instanceof Brick brick) {
            var cell = brickCoordinateMap.get(brick);
            if (cell == null) {
                return;
            }
            var row = cell.row;
            var col = cell.column;
            var brickType = brick.getBrickType();

            brickEvent.getStartEvent(EventType.Wave, row, col);

            switch (brickType) {
                case Wheel -> brickEvent.getStartEvent(EventType.Wheel, row, col);
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

    /** Update is called every frame to execute ongoing brick events. */
    @Override
    public void update() {
        brickEvent.executeEvent();
    }

    /** Returns the number of destroyed bricks so far. */
    public int getBrickDestroyed() {
        return brickDestroyed;
    }

}
