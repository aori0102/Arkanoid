package game.BrickObj.BrickEvent;

import java.util.*;
import static game.BrickObj.InitMatrix.*;

public final class WaveEffect {

    private static final int DESTROYED = -2;
    private static final int EMPTY = -1;

    private final Matrix matrixOfObj;
    private final List<IntPair> justDamaged = new ArrayList<>();

    // mỗi wave giữ 3 tầng, mỗi tầng 1 danh sách các ô
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
        for (int i = 0; i < rowData; i++) {
            for (int j = 0; j < colData; j++) {
                if (matrixObj.isJustDamaged(i, j)) {
                    justDamaged.add(new IntPair(i, j));
                    matrixObj.resetJustDamaged(i, j);
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

        for (IntPair p : justDamaged) {
            WaveLayer w = new WaveLayer();
            w.layer3.add(p);
            activeWaves.add(w);
        }

        List<WaveLayer> nextWaves = new ArrayList<>();

        for (WaveLayer wave : activeWaves) {
            boolean[][] visited = new boolean[rowData][colData];

            for (IntPair p : wave.layer1) {
                for (int k = 0; k < 8; k++) {
                    int x = p.fi() + fx[k];
                    int y = p.se() + fy[k];
                    if (state.valid(x, y) && !visited[x][y] && state.get(x, y) != DESTROYED) {
                        visited[x][y] = true;
                        wave.layer3.add(new IntPair(x, y));
                    }
                }
            }

            WaveLayer newWave = new WaveLayer();
            newWave.layer1.addAll(wave.layer2);
            newWave.layer2.addAll(wave.layer3);

            if (!newWave.layer1.isEmpty() || !newWave.layer2.isEmpty())
                nextWaves.add(newWave);
        }

        activeWaves.clear();
        activeWaves.addAll(nextWaves);

        for (WaveLayer wave : activeWaves) {
            for (IntPair p : wave.layer1)
                if (state.get(p.fi(), p.se()) != DESTROYED)
                    state.set(p.fi(), p.se(), 1);
            for (IntPair p : wave.layer2)
                if (state.get(p.fi(), p.se()) > 2 || state.get(p.fi(), p.se()) == EMPTY)
                    state.set(p.fi(), p.se(), 2);
            for (IntPair p : wave.layer3)
                if (state.get(p.fi(), p.se()) == EMPTY)
                    state.set(p.fi(), p.se(), 3);
        }

        matrixOfObj.assignFrom(state);
    }
}
