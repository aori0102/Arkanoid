package game.Brick.BrickEvent.EventList;

import game.Brick.Brick;
import game.Brick.BrickEvent.Event;
import utils.Time;

import java.util.*;

import static game.Brick.Init.*;

public class RockEvent implements Event {
    private final int rowData;
    private final int colData;
    private final List<List<Brick>> brickGrid;
    private double timeAccum = 0.0;

    private List<cellExecute> listDectecPhare = new ArrayList<>();
    private List<cellExecute> listDestroyPhare = new ArrayList<>();

    public RockEvent(int row, int col, List<List<Brick>> matrix) {
        this.rowData = row;
        this.colData = col;
        this.brickGrid = matrix;
    }

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
