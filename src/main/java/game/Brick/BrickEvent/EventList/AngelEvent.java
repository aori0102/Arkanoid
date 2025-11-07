package game.Brick.BrickEvent.EventList;

import game.Brick.Brick;
import game.Brick.BrickEvent.Event;
import game.Brick.Init;
import game.Entity.EntityHealthAlterType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static game.Brick.Init.*;

public class AngelEvent implements Event {

    private final int rowData;
    private final int colData;
    private final List<List<Brick>> brickGrid;
    private final int MAX_TARGETS = 10;
    private final int EXECUTE_TIME = 15;
    private final int DAMAGE = 30;
    private boolean flag = false;
    private final List<IntPair> targets;
    private int timeFrame = 0, executeTime = 0;

    public AngelEvent(int row, int col, List<List<Brick>> matrix) {
        this.rowData = row;
        this.colData = col;
        this.brickGrid = matrix;
        this.targets = new ArrayList<>();
    }

    @Override
    public void runEvent() {

        if (timeFrame == 0 && executeTime == EXECUTE_TIME && flag) {
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
            if (timeFrame % 2 == 0) {
                for (var index : targets) {
                    int r = index.fi();
                    int c = index.se();

                    if (!valid(brickGrid, r, c)) continue;

                    var brick = brickGrid.get(r).get(c);
                    brick.setRedRender();
                }
            } else {
                for (int r = 0; r < rowData; r++) {
                    for (int c = 0; c < colData; c++) {
                        if (valid(brickGrid, r, c)) {
                            brickGrid.get(r).get(c).resetRenderColor();
                        }
                    }
                }
            }
        }

        if (timeFrame == 0 && flag) executeTime++;
        timeFrame++;
        timeFrame %= NumFrameForEachRunTime;
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
            timeFrame = 0;
            executeTime = 0;
        }
    }
}
