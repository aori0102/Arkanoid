package game.Brick.BrickEvent.EventList;

import game.Brick.Brick;
import game.Brick.BrickEvent.Event;
import game.Brick.Init.*;

import java.util.ArrayList;
import java.util.List;

import static game.Brick.Init.fx;
import static game.Brick.Init.fy;

public final class WaveEvent implements Event {

    private static final int DESTROYED = -2;
    private static final int EMPTY = -1;

    private final List<IntPair> justDamaged = new ArrayList<>();
    private final int rowData;
    private final int colData;
    private final List<List<Brick>> brickGrid;
    private final Matrix state;

    private int timeFrame = 0;

    public WaveEvent(int rowData, int colData, List<List<Brick>> matrix) {
        this.rowData = rowData;
        this.colData = colData;
        this.brickGrid = matrix;
        state = new Matrix(rowData, colData);
    }

    private boolean isJustDamaged(int row, int col) {
        return brickGrid.get(row).get(col).isJustDamaged();
    }

    private void resetJustDamaged(int row, int col) {
        brickGrid.get(row).get(col).resetJustDamaged();
    }

    private boolean isDestroyed(int row, int col) {
        return brickGrid.get(row).get(col) == null
                || brickGrid.get(row).get(col).getHealth() <= 0;
    }

    private boolean valid(int row, int col) {
        return row >= 0 && row < rowData && col >= 0 && col < colData
                && brickGrid.get(row).get(col) != null;
    }

    @Override
    public void runEvent() {
        if (timeFrame == 0) {
            runAllWave();
        }
        timeFrame++;
        timeFrame %= FrameForEachRunTime;
    }

    private static final class WaveLayer {
        final List<IntPair> layer1 = new ArrayList<>();
        final List<IntPair> layer2 = new ArrayList<>();
        final List<IntPair> layer3 = new ArrayList<>();
    }

    private final List<WaveLayer> activeWaves = new ArrayList<>();


    public void runAllWave() {

        justDamaged.clear();
        for (int r = 0; r < rowData; r++) {
            for (int c = 0; c < colData; c++) {
                if (valid(r, c) && isJustDamaged(r, c)) {
                    justDamaged.add(new IntPair(r, c));
                }
            }
        }

        state.fill(EMPTY);

        for (int i = 0; i < rowData; i++) {
            for (int j = 0; j < colData; j++) {
                if (!valid(i, j) && isDestroyed(i, j)) {
                    state.set(i, j, DESTROYED);
                }
            }
        }

        List<WaveLayer> nextWaves = new ArrayList<>();

        for (WaveLayer wave : activeWaves) {
            boolean[][] visited = new boolean[rowData][colData];

            for (IntPair u : wave.layer2) {
                visited[u.fi()][u.se()] = true;
            }

            for (IntPair u : wave.layer3) {
                visited[u.fi()][u.se()] = true;
            }

            for (IntPair u : wave.layer1) {
                visited[u.fi()][u.se()] = true;
            }

            WaveLayer newWave = new WaveLayer();
            newWave.layer1.addAll(wave.layer2);
            newWave.layer2.addAll(wave.layer3);

            for (IntPair p : wave.layer3) {
                for (int k = 0; k < 8; k++) {
                    int x = p.fi() + fx[k];
                    int y = p.se() + fy[k];
                    if (state.valid(x, y) && !visited[x][y] && state.get(x, y) != DESTROYED) {
                        visited[x][y] = true;
                        newWave.layer3.add(new IntPair(x, y));
                    }
                }
            }


            if (!newWave.layer1.isEmpty() || !newWave.layer2.isEmpty())
                nextWaves.add(newWave);
        }


        activeWaves.clear();
        activeWaves.addAll(nextWaves);

        for (IntPair p : justDamaged) {
            WaveLayer w = new WaveLayer();
            w.layer3.add(p);
            activeWaves.add(w);
        }

        for (WaveLayer wave : activeWaves) {
            for (IntPair p : wave.layer1)
                if (state.get(p.fi(), p.se()) != DESTROYED)
                    state.set(p.fi(), p.se(), 3);
            for (IntPair p : wave.layer2)
                if (state.get(p.fi(), p.se()) > 2 || state.get(p.fi(), p.se()) == EMPTY)
                    state.set(p.fi(), p.se(), 2);
            for (IntPair p : wave.layer3)
                if (state.get(p.fi(), p.se()) == EMPTY)
                    state.set(p.fi(), p.se(), 1);
        }

        for (int row = 0; row < rowData; row++) {
            for (int col = 0; col < colData; col++) {
                if (valid(row, col))
                    brickGrid.get(row).get(col).setWaveIndex(state.get(row, col));
            }
        }
    }
}
