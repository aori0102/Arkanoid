package game.Brick.BrickEvent.EventList;

import game.Brick.Brick;
import game.Brick.BrickEvent.Event;
import utils.Time;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static game.Brick.Init.*;

/**
 * Represents a "Wheel" or "Column Strike" event.
 * <p>
 * This is a two-phase event that is functionally similar to {@link RockEvent} but
 * operates on randomly selected columns instead of an expanding cross-wave.
 * <ol>
 * <li><b>Detection Phase:</b> When triggered (by destroying the host brick at (r, c)),
 * this event does *not* act at its own location. Instead, it randomly selects
 * two columns on the grid and spawns a "beam" at the very top (row 0) of each.
 * This beam travels straight down, setting bricks to `maxBrightness` as a warning.</li>
 * <li><b>Destruction Phase:</b> After a beam hits the bottom of the grid, it
 * restarts at the top (row 0) of the same column and enters the destruction phase.
 * It travels down again, but now on a 4-tick cycle: blink (red/yellow) for 3 ticks,
 * then destroy the brick on the 4th tick, then move down.</li>
 * </ol>
 * This process repeats until the destruction beam also travels off the bottom of the grid.
 */
public class WheelEvent implements Event {

    private final int rowData;
    private final int colData;
    private final List<List<Brick>> brickGrid;
    private double timeAccum = 0.0;
    private final Random rng = new Random();

    private List<cellExecute> listDectecPhare = new ArrayList<>();
    private List<cellExecute> listDestroyPhare = new ArrayList<>();

    public WheelEvent(int row, int col, List<List<Brick>> matrix) {
        this.rowData = row;
        this.colData = col;
        this.brickGrid = matrix;
    }

    /**
     * An inner class that represents a single vertical "beam"
     * traveling straight down a column.
     * <p>
     * This object manages the state of one beam, tracking its vertical position
     * and its internal 4-tick destruction timer.
     */
    static class cellExecute {
        private final int row;
        private final int colBound;
        private final IntPair start;
        private IntPair downDirec;
        private int loopTime = 0;

        public cellExecute(IntPair cell, int row, int colBound) {
            this.start = cell;
            this.downDirec = new IntPair(cell.fi(), cell.se());
            this.row = row;
            this.colBound = colBound;
        }

        public IntPair getDownDirec() {
            return downDirec;
        }

        public void execute() {
            downDirec = new IntPair(downDirec.fi() + 1, downDirec.se());
        }

        public void restart() {
            downDirec = start;
        }

        public void restartLoop() {
            loopTime = 0;
        }

        public void runTime() {
            loopTime++;
        }

        public boolean isDestroyTime() {
            return loopTime == 4;
        }

        public int getTime() {
            return loopTime;
        }

        public boolean allOutBounds() {
            return downDirec.fi() >= row;
        }
    }


    /**
     * The main update loop for all active WheelEvent beams.
     * <p>
     * This method manages the two lists of beams (detection and destruction).
     * It processes each beam, transitions beams from detection to destruction when they
     * hit the bottom, and removes destruction beams when they hit the bottom a second time.
     */
    @Override
    public void runEvent() {
        timeAccum += Time.getDeltaTime();

        if (timeAccum >= UPDATE_INTERVAL) {
            timeAccum = 0.0;

            List<cellExecute> newListDectec = new ArrayList<>();
            List<cellExecute> newListDestroy = new ArrayList<>();

            for (cellExecute cell : listDestroyPhare) {
                executeDestroy(cell);
                if (!cell.allOutBounds()) {
                    newListDestroy.add(cell);
                }
            }

            for (cellExecute cell : listDectecPhare) {
                executeDetection(cell);
                if (cell.allOutBounds()) {
                    cell.restart();
                    newListDestroy.add(cell);
                } else {
                    newListDectec.add(cell);
                }
            }

            listDectecPhare = newListDectec;
            listDestroyPhare = newListDestroy;
        }
    }

    private void executeDestroy(cellExecute obj) {
        obj.runTime();

        IntPair cell = obj.getDownDirec();
        int r = cell.fi();
        int c = cell.se();

        if (valid(brickGrid, r, c)) {
            if (obj.isDestroyTime()) {
                destroyBrick(brickGrid, r, c);
            } else {
                if (obj.getTime() % 2 == 0) {
                    brickGrid.get(r).get(c).setRedRender();
                } else {
                    brickGrid.get(r).get(c).setYellowRender();
                }
            }
        }

        if (obj.isDestroyTime()) {
            obj.restartLoop();
            obj.execute();
        }
    }

    private void executeDetection(cellExecute obj) {
        IntPair cell = obj.getDownDirec();
        int r = cell.fi();
        int c = cell.se();

        if (valid(brickGrid, r, c)) {
            brickGrid.get(r).get(c).maxBrightness();
        }

        obj.execute();
    }

    /**
     * Triggers the WheelEvent.
     * <p>
     * This method destroys the host brick at (r, c). It then randomly selects
     * two columns from the grid (or fewer if the grid is narrower than 2 columns).
     * For each selected column, it creates a new {@link cellExecute} beam
     * starting at row 0 of that column and adds it to the 'detection' list.
     *
     * @param r The row index of the brick that triggered the event.
     * @param c The column index of the brick that triggered the event.
     */
    @Override
    public void getStartEvent(int r, int c) {
        if(valid(brickGrid, r, c)) {
            timeAccum = 0.0;
            destroyBrick(brickGrid, r, c);

            List<Integer> allCols = new ArrayList<>();
            for (int i = 0; i < colData; i++) allCols.add(i);
            Collections.shuffle(allCols, rng);

            int numCols = Math.min(2, colData);
            for (int i = 0; i < numCols; i++) {
                int col = allCols.get(i);
                if (!valid(brickGrid, 0, col)) continue;
                IntPair start = new IntPair(0, col);
                listDectecPhare.add(new cellExecute(start, rowData, colData));
            }
        }
    }
}
