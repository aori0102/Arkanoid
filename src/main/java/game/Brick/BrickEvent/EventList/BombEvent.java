package game.Brick.BrickEvent.EventList;

import game.Brick.Brick;
import game.Brick.BrickEvent.Event;
import game.Entity.EntityHealthAlterType;
import utils.Time;

import java.util.ArrayList;
import java.util.List;

import static game.Brick.Init.*;

/**
 * Represents a "Bomb" event on the brick grid.
 * <p>
 * When triggered by its host brick's destruction, this event targets the
 * eight adjacent (neighboring) bricks for a delayed explosion.
 * The targeted bricks will blink for a short duration ({@code EXECUTE_TIME} ticks)
 * before taking a large amount of damage.
 */
public class BombEvent implements Event {

    private final int rowData;
    private final int colData;
    private final List<List<Brick>> brickGrid;
    private final int EXECUTE_TIME = 18;
    private final int DAMAGE = 3000;
    private boolean flag = false;
    private final List<IntPair> targets;
    private int executeTime = 0;
    private double timeAccum = 0.0;

    public BombEvent(int row, int col, List<List<Brick>> matrix) {
        this.rowData = row;
        this.colData = col;
        this.brickGrid = matrix;
        this.targets = new ArrayList<>();
    }

    /**
     * Initializes a new BombEvent.
     *
     * @param row    The number of rows in the grid.
     * @param col    The number of columns in the grid.
     * @param matrix A reference to the main 2D brick grid.
     */
    @Override
    public void runEvent() {
        timeAccum += Time.getDeltaTime();
        if (timeAccum >= UPDATE_INTERVAL && executeTime == EXECUTE_TIME && flag) {
            for (var index : targets) {
                int r = index.fi();
                int c = index.se();

                if (!valid(brickGrid, r, c)) continue;

                var brick = brickGrid.get(r).get(c);
                brick.getBrickHealth().alterHealth(EntityHealthAlterType.NormalDamage, null, DAMAGE);
            }

            executeTime = 0;
            targets.clear();
            flag = false;
        }

        if (flag) {
            if (executeTime % 3 != 0) {
                for (var index : targets) {
                    int r = index.fi();
                    int c = index.se();

                    if (!valid(brickGrid, r, c)) continue;
                    brickGrid.get(r).get(c).setRedRender();
                }
            } else {
                for (var index : targets) {
                    int r = index.fi();
                    int c = index.se();

                    if (!valid(brickGrid, r, c)) continue;
                    brickGrid.get(r).get(c).setYellowRender();
                }
            }
        }

        if (timeAccum >= UPDATE_INTERVAL) {
            if(flag) executeTime++;
            timeAccum = 0;
        }
    }

    /**
     * Executes the event's update logic (the countdown).
     * <p>
     * This method is called continuously. It handles the timing,
     * creates the blinking effect (red/yellow) for the 8 target bricks,
     * and finally applies the explosion damage ({@code DAMAGE}) when the
     * countdown ({@code EXECUTE_TIME}) finishes. It then resets itself.
     */
    @Override
    public void getStartEvent(int r, int c) {
        if (valid(brickGrid, r, c)) {

            destroyBrick(brickGrid, r, c);

            targets.clear();

            for (int i = 0; i < 8; i++) {
                int dx = fx[i];
                int dy = fy[i];
                int newRow = r + dx;
                int newCol = c + dy;

                if (valid(brickGrid, newRow, newCol)) {
                    targets.add(new IntPair(newRow, newCol));
                }
            }

            flag = true;
            timeAccum = 0;
            executeTime = 0;
        }
    }
}
