package game.Brick.BrickEvent.EventList;

import game.Brick.Brick;
import game.Brick.BrickEvent.Event;
import utils.Time;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static game.Brick.Init.*;

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

    @Override
    public void getStartEvent(int r, int c) {
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
