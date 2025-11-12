package game.Brick.BrickEvent.EventList;

import game.Brick.Brick;
import game.Brick.BrickEvent.Event;
import utils.Time;

import java.util.*;

import static game.Brick.Init.*;

/**
 * Represents a "Rock" or "Cross-Wave" event on the brick grid.
 * <p>
 * This is a two-phase event.
 * <ol>
 * <li><b>Detection Phase:</b> When triggered, a wave starts from the origin (r, c)
 * and expands one cell at a time in all four cardinal directions (up, down, left, right),
 * forming an expanding "plus" sign. Bricks on this path are set to max brightness as a warning.
 * This phase lasts until all four arms of the wave are off the grid.</li>
 * <li><b>Destruction Phase:</b> The wave then resets to the origin and expands outwards again.
 * This time, it operates on a 4-tick cycle: for 3 ticks, it blinks the bricks
 * on its path (red/yellow), and on the 4th tick, it destroys them. This
 * pulsing destruction continues until the wave expands off the grid a second time.</li>
 * </ol>
 */
public class RockEvent implements Event {
    private final int rowData;
    private final int colData;
    private final List<List<Brick>> brickGrid;
    private double timeAccum = 0.0;

    private List<cellExecute> listDectecPhare = new ArrayList<>();
    private List<cellExecute> listDestroyPhare = new ArrayList<>();

    /**
     * Initializes a new RockEvent.
     *
     * @param row    The number of rows in the grid.
     * @param col    The number of columns in the grid.
     * @param matrix A reference to the main 2D brick grid.
     */
    public RockEvent(int row, int col, List<List<Brick>> matrix) {
        this.rowData = row;
        this.colData = col;
        this.brickGrid = matrix;
    }

    /**
     * An inner class that represents a single expanding "cross-wave".
     * <p>
     * This object manages the state of one event, tracking the four expanding
     * "arms" of the wave and its internal state (like the destruction timer).
     */
    static class cellExecute {
        private final int row, col;
        private final IntPair start;
        private IntPair downDirec, upDirec, leftDirec, rightDirec;
        private int loopTime = 0;

        public cellExecute(IntPair cell, int row, int col) {
            start = cell;
            upDirec = new IntPair(cell.fi(), cell.se());
            leftDirec = new IntPair(cell.fi(), cell.se());
            rightDirec = new IntPair(cell.fi(), cell.se());
            downDirec = new IntPair(cell.fi(), cell.se());

            this.row =  row;
            this.col = col;
        }

        public IntPair getDownDirec() {
            return downDirec;
        }

        public IntPair getUpDirec() {
            return upDirec;
        }

        public IntPair getLeftDirec() {
            return leftDirec;
        }

        public IntPair getRightDirec() {
            return rightDirec;
        }

        public IntPair getStart() {
            return start;
        }

        public void execute() {
            downDirec = new IntPair(downDirec.fi() + 1, downDirec.se());
            upDirec = new IntPair(upDirec.fi() - 1, upDirec.se());
            leftDirec = new IntPair(leftDirec.fi(), leftDirec.se() - 1);
            rightDirec = new IntPair(rightDirec.fi(), rightDirec.se() + 1);
        }

        public void restart() {
            downDirec = start;
            upDirec = start;
            leftDirec = start;
            rightDirec = start;
        }

        public void restartLoop() {
            loopTime = 0;
        }

        public boolean isDestroyTime() {
            return loopTime == 4;
        }

        public void runTime() {
            loopTime++;
        }

        public int getTime() {
            return loopTime;
        }

        public boolean allOutBounds() {
            return (downDirec.fi() >= row) && (upDirec.fi() < 0) && (leftDirec.se() < 0) &&  (rightDirec.se() >= col);
        }
    }

    /**
     * The main update loop for all active RockEvents.
     * <p>
     * This method manages the two lists of waves (detection and destruction).
     * It processes each wave, moves waves from detection to destruction when they
     * go out of bounds, and removes waves from destruction when they go out of bounds
     * a second time.
     */
    @Override
    public void runEvent() {

        timeAccum += Time.getDeltaTime();

        if (timeAccum >= UPDATE_INTERVAL) {
            timeAccum = 0.0;
            List<cellExecute> newListDectec = new ArrayList<>();
            List<cellExecute> newListDestroy = new ArrayList<>();

            for (cellExecute cell: listDestroyPhare) {
                executeDestroy(cell);
                if (!cell.allOutBounds()) {
                    newListDestroy.add(cell);
                }
            }

            for (cellExecute cell : listDectecPhare) {
                executeDetection(cell);
                if(cell.allOutBounds()) {
                    cell.restart();
                    newListDestroy.add(cell);
                }
                else {
                    newListDectec.add(cell);
                }
            }

            listDectecPhare.clear();
            listDectecPhare = newListDectec;
            listDestroyPhare.clear();
            listDestroyPhare = newListDestroy;
        }
    }

    private void executeDestroy(cellExecute obj) {
        obj.runTime();

        List<IntPair> listCell = new ArrayList<>();
        listCell.add(obj.getDownDirec());
        listCell.add(obj.getUpDirec());
        listCell.add(obj.getLeftDirec());
        listCell.add(obj.getRightDirec());

        for (IntPair cell: listCell) {
            if (valid(brickGrid, cell.fi(), cell.se())) {
                if (obj.isDestroyTime()) {
                    destroyBrick(brickGrid,  cell.fi(), cell.se());
                }
                else {
                    if (obj.getTime() % 2 == 0) {
                        brickGrid.get(cell.fi()).get(cell.se()).setRedRender();
                    }
                    else {
                        brickGrid.get(cell.fi()).get(cell.se()).setYellowRender();
                    }
                }
            }
        }

        if(obj.isDestroyTime()) {
            obj.restartLoop();
            obj.execute();
        }
    }

    private void executeDetection(cellExecute obj) {

        List<IntPair> listCell = new ArrayList<>();
        listCell.add(obj.getDownDirec());
        listCell.add(obj.getUpDirec());
        listCell.add(obj.getLeftDirec());
        listCell.add(obj.getRightDirec());

        for (IntPair cell: listCell) {
            if (valid(brickGrid, cell.fi(), cell.se())) {
                brickGrid.get(cell.fi()).get(cell.se()).maxBrightness();
            }
        }

        obj.execute();
    }

    /**
     * Triggers the rock event at a specific location.
     * <p>
     * This method is typically called when the brick containing this event is destroyed.
     * It destroys the host brick at (r, c) and creates a new {@link cellExecute} wave
     * at that position, adding it to the 'detection' list to start the two-phase process.
     *
     * @param r The row index of the brick that triggered the event.
     * @param c The column index of the brick that triggered the event.
     */
    @Override
    public void getStartEvent(int r, int c) {
        if (valid(brickGrid, r, c)) {
            timeAccum = 0;
            destroyBrick(brickGrid, r, c);
            var start = new IntPair(r, c);
            listDectecPhare.add(new cellExecute(start, rowData, colData));
        }
    }
}
