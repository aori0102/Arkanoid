package game.Brick.BrickEvent.EventList;

import game.Brick.Brick;
import game.Brick.BrickEvent.Event;
import utils.Time;

import java.util.ArrayList;
import java.util.List;

import static game.Brick.Init.*;

public final class WaveEvent implements Event {

    private static final int DESTROYED = -2;
    private static final int EMPTY = -1;
    private static final int WAVE_LIMIT = 1000;

    private final List<IntPair> justDamaged = new ArrayList<>();
    private final int rowData;
    private final int colData;
    private final List<List<Brick>> brickGrid;
    private final Matrix state;

    private double timeAccum = 0.0;

    public WaveEvent(int rowData, int colData, List<List<Brick>> matrix) {
        this.rowData = rowData;
        this.colData = colData;
        this.brickGrid = matrix;
        state = new Matrix(rowData, colData);
    }

    @Override
    public void runEvent() {
        timeAccum += Time.getDeltaTime();

        if (timeAccum >= UPDATE_INTERVAL) {
            runAllWave();
            timeAccum = 0.0;
        }
    }

    @Override
    public void getStartEvent(int r, int c) {
        if(justDamaged.size() + activeWaves.size() + 1 <= WAVE_LIMIT) {
            justDamaged.add(new IntPair(r, c));
        }
    }

    private static final class WaveLayer {
        final List<IntPair> layer1 = new ArrayList<>();
        final List<IntPair> layer2 = new ArrayList<>();
        final List<IntPair> layer3 = new ArrayList<>();
    }

    private final List<WaveLayer> activeWaves = new ArrayList<>();


    public void runAllWave() {

        state.fill(EMPTY);

        for (int i = 0; i < rowData; i++) {
            for (int j = 0; j < colData; j++) {
                if (!valid(brickGrid, i, j) || isDestroyed(brickGrid, i, j)) {
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
                if (valid(brickGrid, row, col))
                    brickGrid.get(row).get(col).setWaveIndex(state.get(row, col));
            }
        }

        justDamaged.clear();
    }
}
