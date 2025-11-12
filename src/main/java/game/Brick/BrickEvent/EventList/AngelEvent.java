package game.Brick.BrickEvent.EventList;

import game.Brick.Brick;
import game.Brick.BrickEvent.Event;
import game.Brick.Init;
import game.Entity.EntityHealthAlterType;
import utils.Time;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static game.Brick.Init.*;

/**
 * Represents an "Angel" event on the brick grid.
 * <p>
 * When this event is triggered (usually by destroying its host brick),
 * it randomly selects several other bricks on the board and "heals"
 * (increases their health) after a delay.
 * During the delay, the target bricks will blink to indicate they are targeted.
 */
public class AngelEvent implements Event {

    private final int rowData; // size of row
    private final int colData; // size of col
    private final List<List<Brick>> brickGrid; // Reference to the grid manager all the brick
    private static final int MAX_TARGETS = 10; // the maximum number of object that have effected
    private static final int EXECUTE_TIME = 15; // Delay time maximum
    private static final int DAMAGE = 30; // Amount increase health
    private boolean flag = false; // Detection
    private final List<IntPair> targets; // List of object that will be effected
    private int executeTime = 0; // Delay time
    private double timeAccumulated = 0; // Time from the last execution

    public AngelEvent(int row, int col, List<List<Brick>> matrix) {
        this.rowData = row;
        this.colData = col;
        this.brickGrid = matrix;
        this.targets = new ArrayList<>();
    }

    /**
     * Initializes a new AngelEvent.
     */
    @Override
    public void runEvent() {
        timeAccumulated += Time.getDeltaTime();

        if (timeAccumulated >= UPDATE_INTERVAL && executeTime == EXECUTE_TIME && flag) {
            for (var index : targets) {
                int r = index.fi();
                int c = index.se();

                if (!valid(brickGrid, r, c)) continue;

                var brick = brickGrid.get(r).get(c);
                brick.getBrickHealth().alterHealth(EntityHealthAlterType.Regeneration, null, DAMAGE);
            }

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

                    var brick = brickGrid.get(r).get(c);
                    brick.setRedRender();
                }
            } else {
                for (var index : targets) {
                    int r = index.fi();
                    int c = index.se();

                    if (!valid(brickGrid, r, c)) continue;

                    var brick = brickGrid.get(r).get(c);
                    brick.setYellowRender();
                }
            }
        }

        if (timeAccumulated >= UPDATE_INTERVAL) {
            if (flag) executeTime++;
            timeAccumulated = 0;
        }
    }

    @Override
    public void getStartEvent(int r, int c) {
        if (valid(brickGrid, r, c)) {
            destroyBrick(brickGrid, r, c);

            Random rng = new Random();
            List<Init.IntPair> allAlive = new ArrayList<>();

            for (int i = 0; i < rowData; i++) {
                for (int j = 0; j < colData; j++) {
                    if (!isDestroyed(brickGrid, i, j)) {
                        allAlive.add(new Init.IntPair(i, j));
                    }
                }
            }

            Collections.shuffle(allAlive, rng);
            targets.clear();
            for (int k = 0; k < Math.min(MAX_TARGETS, allAlive.size()); k++) {
                targets.add(allAlive.get(k));
            }

            flag = true;
            timeAccumulated = 0;
            executeTime = 0;
        }
    }
}
