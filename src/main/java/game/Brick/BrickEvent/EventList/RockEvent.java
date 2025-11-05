package game.Brick.BrickEvent.EventList;

import game.Brick.Brick;
import game.Brick.BrickEvent.Event;
import game.Brick.Init;

import java.util.*;

import static game.Brick.Init.*;

public class RockEvent implements Event {
    private final int rowData;
    private final int colData;
    private final List<List<Brick>> brickGrid;
    private int timeFrame = 0;

    private final List<Init.IntPair> listDown = new LinkedList<>();
    private final List<Init.IntPair> listUp = new LinkedList<>();
    private final List<Init.IntPair> listLeft = new LinkedList<>();
    private final List<Init.IntPair> listRight = new LinkedList<>();
    public RockEvent(int row, int col, List<List<Brick>> matrix) {
        this.rowData = row;
        this.colData = col;
        this.brickGrid = matrix;
    }

    @Override
    public void runEvent() {
        if (timeFrame != 0) {
            propagate(listUp, -1, 0);
            propagate(listDown, 1, 0);
            propagate(listLeft, 0, -1);
            propagate(listRight, 0, 1);
        }

        timeFrame++;
        timeFrame = timeFrame % NumFrameForEachRunTime;
    }

    private void propagate(List<IntPair> directionList, int dr, int dc) {
        if (directionList.isEmpty()) return;

        List<IntPair> next = new LinkedList<>();

        for (Init.IntPair p : directionList) {
            int r = p.fi(), c = p.se();

            int nextR = r + dr;
            int nextC = c + dc;

            if (valid(brickGrid, nextR, nextC)) {
                next.add(new Init.IntPair(nextR, nextC));
                brickGrid.get(nextR).get(nextC).maxBrightness();
            }

            if (valid(brickGrid, r, c)) {
                destroyBrick(brickGrid, r, c);
            }
        }

        directionList.clear();
        directionList.addAll(next);
    }

    @Override
    public void getStartEvent(int r, int c) {
        if (valid(brickGrid, r, c)) {
            timeFrame = 0;
            destroyBrick(brickGrid, r, c);
            var start = new Init.IntPair(r, c);
            listDown.add(start);
            listUp.add(start);
            listLeft.add(start);
            listRight.add(start);
        }
    }
}
