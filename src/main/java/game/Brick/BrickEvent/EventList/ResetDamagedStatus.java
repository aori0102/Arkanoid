package game.Brick.BrickEvent.EventList;

import game.Brick.Brick;
import game.Brick.BrickEvent.Event;

import java.util.List;

public class ResetDamagedStatus implements Event {

    private final int rowData;
    private final int colData;
    private final List<List<Brick>> brickGrid;
    private int timeFrame = 0;

    public ResetDamagedStatus(int row, int col, List<List<Brick>> grid) {
        this.rowData = row;
        this.colData = col;
        this.brickGrid = grid;
    }

    @Override
    public void runEvent() {
        if (timeFrame == 0) {
            for (int r = 0; r < rowData; r++) {
                for (int c = 0; c < colData; c++) {
                    if(brickGrid.get(r).get(c) != null) {
                        brickGrid.get(r).get(c).resetJustDamaged();
                    }
                }
            }
        }
        timeFrame++;
        timeFrame %= NumFrameForEachRunTime;
    }

    @Override
    public void getStartEvent(int r, int c) {

    }
}
