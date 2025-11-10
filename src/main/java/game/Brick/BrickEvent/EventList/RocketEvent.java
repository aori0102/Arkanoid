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

public class RocketEvent implements Event {

    private final int rowData;
    private final int colData;
    private final List<List<Brick>> brickGrid;
    private final int MAX_TARGETS = 10;
    private final int EXECUTE_TIME = 21;
    private final int DAMAGE = 300000;
    private boolean flag = false;
    private final List<IntPair> targets;
    private int executeTime = 0;
    private double timeAccum = 0.0;

    public RocketEvent(int row, int col, List<List<Brick>> matrix) {
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
                brick.getBrickHealth().alterHealth(EntityHealthAlterType.NormalDamage, DAMAGE);
            }

            executeTime = 0;
            targets.clear();
            flag = false;
        }

        if (flag) {
            if (executeTime % 4 != 0) {
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
                    brick.maxBrightness();
                }
            }
        }

        if (timeAccum >= UPDATE_INTERVAL) {
            if(flag) executeTime++;
            timeAccum = 0;
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
            timeAccum = 0;
            executeTime = 0;
        }
    }
}
