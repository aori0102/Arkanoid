package game.Brick.BrickEvent;

import java.util.HashSet;
import java.util.Vector;
import static game.Brick.InitMatrix.*;

public final class WaveEffect {

    record Wave(Vector<IntPair> list) {}
    record LayerWave(Wave layer3, Wave layer2, Wave layer1) {}

    private Vector<IntPair> listObjHitDamage;
    private Vector<LayerWave> listOfLayer;
    private Matrix matrixOfObj;

    public WaveEffect() {
        super();
        matrixOfObj = new Matrix(rowData, colData, -1);
        listObjHitDamage = new Vector<>();
        listOfLayer = new Vector<>();
    }

    public Matrix getStateMatrix() {
        return matrixOfObj;
    }

    /**
     * This list contains all object which just hitted.
     */
    public void getListObjHitDamage() {
        listObjHitDamage.clear();

        for (int i = 0; i < rowData; i++) {
            for (int j = 0; j < colData; j++) {
                if (matrixObj.isJustDamaged(i, j)) {
                    listObjHitDamage.add(new IntPair(i, j));
                    matrixObj.resetJustDamaged(i, j);
                }
            }
        }
    }

    /**
     * For each cell:
     *  -1 means that object is doing nothing.
     *  -2 means that object is dead.
     *  1 means that object is in waveType1.
     *  2, 3 means same thing as 1.
     */
    public void runAllWave() {
        Matrix stateMatrix = new Matrix(rowData, colData, -1);

        for (int i = 0; i < rowData; i++) {
            for (int j = 0; j < colData; j++) {
                if (matrixObj.isDestroyed(i, j)) {
                    stateMatrix.set(i, j, -2);
                }
            }
        }

        for (IntPair p : listObjHitDamage) {
            Vector<IntPair> list = new Vector<>();
            list.add(new IntPair(p.fi(), p.se()));
            LayerWave newWave = new LayerWave(null, null, new Wave(list));
            listOfLayer.add(newWave);
        }

        Vector<LayerWave> newListOfLayer = new Vector<>();

        for (int i = 0; i < listOfLayer.size(); i++) {
            Wave fi = listOfLayer.get(i).layer1();
            Wave se = listOfLayer.get(i).layer2();
            Wave thir = listOfLayer.get(i).layer3();

            Wave newFi = new Wave(new Vector<>());
            Wave newSe = new Wave(new Vector<>());
            Wave newThir = new Wave(new Vector<>());

            HashSet<IntPair> Mp = new HashSet<>();
            if (se != null) {
                for (IntPair p : se.list()) {
                    Mp.add(new IntPair(p.fi(), p.se()));
                }
            }

            if (fi != null) {
                for (IntPair p : fi.list()) {
                    for (int j = 0; j < 8; j++) {
                        int x = p.fi() + fx[j];
                        int y = p.se() + fy[j];

                        if (stateMatrix.valid(x, y) && !Mp.contains(new IntPair(x, y))) {
                            newFi.list().add(new IntPair(x, y));
                        }
                    }
                }
            }

            newSe = fi != null ? new Wave(new Vector<>(fi.list())) : new Wave(new Vector<>());
            newThir = se != null ? new Wave(new Vector<>(se.list())) : new Wave(new Vector<>());

            if (!newFi.list().isEmpty() || !newSe.list().isEmpty() || !newThir.list().isEmpty()) {
                newListOfLayer.add(new LayerWave(newThir, newSe, newFi));
            }
        }

        listOfLayer = newListOfLayer;

        for (LayerWave wave : listOfLayer) {
            if (wave.layer1() != null) {
                for (IntPair p : wave.layer1().list()) {
                    int x = p.fi();
                    int y = p.se();
                    if (stateMatrix.get(x, y) != -2) {
                        stateMatrix.set(x, y, 1);
                    }
                }
            }

            if (wave.layer2() != null) {
                for (IntPair p : wave.layer2().list()) {
                    int x = p.fi();
                    int y = p.se();
                    if (stateMatrix.get(x, y) == -1 || stateMatrix.get(x, y) > 2) {
                        stateMatrix.set(x, y, 2);
                    }
                }
            }

            if (wave.layer3() != null) {
                for (IntPair p : wave.layer3().list()) {
                    int x = p.fi();
                    int y = p.se();
                    if (stateMatrix.get(x, y) == -1) {
                        stateMatrix.set(x, y, 3);
                    }
                }
            }
        }

        matrixOfObj.assignFrom(stateMatrix);
    }
}
