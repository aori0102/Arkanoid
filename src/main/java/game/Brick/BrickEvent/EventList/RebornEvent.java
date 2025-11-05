package game.Brick.BrickEvent.EventList;

import game.Brick.Brick;
import game.Brick.BrickEvent.Event;

import java.util.List;

public class RebornEvent implements Event {
    private final int rowData;
    private final int colData;
    private final List<List<Brick>> brickGrid;
    private int timeFrame = 0;

    public RebornEvent(int row, int col, List<List<Brick>> matrix) {
        this.rowData = row;
        this.colData = col;
        this.brickGrid = matrix;
    }


    @Override
    public void runEvent() {

    }

    @Override
    public void getStartEvent(int r, int c) {

    }
}
