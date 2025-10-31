package game.BrickObj.BrickEvent;

import java.util.*;
import static game.BrickObj.Init.*;

public final class WaveEffect {

    private static final int DESTROYED = -2;
    private static final int EMPTY = -1;

    private final Matrix matrixOfObj;
    private final List<IntPair> justDamaged = new ArrayList<>();

    private static final class WaveLayer {
        final List<IntPair> layer1 = new ArrayList<>();
        final List<IntPair> layer2 = new ArrayList<>();
        final List<IntPair> layer3 = new ArrayList<>();
    }

    private final List<WaveLayer> activeWaves = new ArrayList<>();

    public WaveEffect() {
        matrixOfObj = new Matrix(rowData, colData, EMPTY);
    }

    public Matrix getStateMatrix() {
        return matrixOfObj;
    }

    public void collectJustDamaged() {
        justDamaged.clear();
        for (int r = 0; r < rowData; r++) {
            for (int c = 0; c < colData; c++) {
                if (!matrixObj.invalid(r, c) && matrixObj.isJustDamaged(r, c)) {
                    justDamaged.add(new IntPair(r, c));
                    matrixObj.resetJustDamaged(r, c);
                }
            }
        }
    }

    public void runAllWave() {
        final Matrix state = new Matrix(rowData, colData, EMPTY);

        for (int i = 0; i < rowData; i++) {
            for (int j = 0; j < colData; j++) {
                if (matrixObj.isDestroyed(i, j)) {
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

        matrixObj.setWaveIndex(state);
    }

    public static double mapWaveToBrightness(int index, int maxWave) {
        index += 2;
        double minB = -0.4;
        double maxB =  0.8;
        return minB + (maxB - minB) * ((double) index / maxWave);
    }
}
