package game.Brick.BrickEvent.EventList;

import game.Brick.Brick;
import game.Brick.BrickEvent.Event;
import game.Entity.EntityHealthAlterType;
import utils.Time;

import java.util.ArrayList;
import java.util.List;

import static game.Brick.Init.*;

/**
 * Represents a "Gift" event on the brick grid.
 * <p>
 * This event is functionally similar to a {@link BombEvent} but with
 * a different visual effect and a much smaller damage value. When triggered
 * by its host brick's destruction, it targets the eight adjacent (neighboring) bricks.
 * The targeted bricks will blink (alternating yellow and max brightness) for a short
 * duration ({@code EXECUTE_TIME} ticks) before taking a small amount of damage.
 */
public class GiftEvent implements Event {

    private final int rowData;
    private final int colData;
    private final List<List<Brick>> brickGrid;
    private final int EXECUTE_TIME = 15;
    private final int HEALING_AMOUNT = 30;
    private boolean flag = false;
    private final List<IntPair> targets;
    private int executeTime = 0;
    private double timeAccum = 0;

    /**
     * Initializes a new GiftEvent.
     *
     * @param row    The number of rows in the grid.
     * @param col    The number of columns in the grid.
     * @param matrix A reference to the main 2D brick grid.
     */
    public GiftEvent(int row, int col, List<List<Brick>> matrix) {
        this.rowData = row;
        this.colData = col;
        this.brickGrid = matrix;
        this.targets = new ArrayList<>();
    }

    /**
     * Executes the event's update logic (the countdown).
     * <p>
     * This method is called continuously. It handles the timing,
     * creates the blinking effect (yellow/max-brightness) for the 8 target bricks,
     * and finally applies the damage ({@code DAMAGE}) when the
     * countdown ({@code EXECUTE_TIME}) finishes. It then resets itself.
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
                brick.getBrickHealth()
                        .alterHealth(EntityHealthAlterType.Regeneration, null, HEALING_AMOUNT);
            }

            timeAccum = 0;
            executeTime = 0;
            targets.clear();
            flag = false;
        }

        if (flag) {
            if (executeTime % 2 == 0) {
                for (var index : targets) {
                    int r = index.fi();
                    int c = index.se();

                    if (!valid(brickGrid, r, c)) continue;
                    brickGrid.get(r).get(c).setYellowRender();
                }
            } else {
                for (var index : targets) {
                    int r = index.fi();
                    int c = index.se();

                    if (!valid(brickGrid, r, c)) continue;
                    brickGrid.get(r).get(c).maxBrightness();
                }
            }
        }

        if (timeAccum >= UPDATE_INTERVAL && flag) {
            executeTime++;
            timeAccum = 0;
        }
    }

    /**
     * Triggers the gift event at a specific location.
     * <p>
     * This method is typically called when the brick containing this event is destroyed.
     * It will destroy the brick at (r, c), identify all 8 valid adjacent neighbors,
     * and add them to the {@code targets} list. It then sets {@code flag = true}
     * to begin the event countdown.
     *
     * @param r The row index of the brick that triggered the event.
     * @param c The column index of the brick that triggered the event.
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
