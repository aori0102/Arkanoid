package game.Brick.BrickEvent.EventList;

import game.Brick.Brick;
import game.Brick.BrickEvent.Event;
import game.Entity.EntityHealthAlterType;
import utils.Time;

import java.util.ArrayList;
import java.util.List;

import static game.Brick.Init.*;

public class GiftEvent implements Event {

    private final int rowData;
    private final int colData;
    private final List<List<Brick>> brickGrid;
    private final int EXECUTE_TIME = 15;
    private final int DAMAGE = 30;
    private boolean flag = false;
    private final List<IntPair> targets;
    private int executeTime = 0;
    private double timeAccum = 0;

    public GiftEvent(int row, int col, List<List<Brick>> matrix) {
        this.rowData = row;
        this.colData = col;
        this.brickGrid = matrix;
        this.targets = new ArrayList<>();
    }

    @Override
    public void runEvent() {
        timeAccum += Time.getDeltaTime();

        if (timeAccum >= UPDATE_INTERVAL && executeTime == EXECUTE_TIME && flag) {
            for (var index : targets) {
                int r = index.fi();
                int c = index.se();

                if (!valid(brickGrid, r, c)) continue;

                var brick = brickGrid.get(r).get(c);
                brick.getBrickHealth().alterHealth(EntityHealthAlterType.NormalDamage, null, DAMAGE);
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
