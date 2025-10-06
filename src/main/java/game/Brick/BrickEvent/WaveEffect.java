package game.Brick.BrickEvent;
import game.Brick.InitBrick;

import java.util.HashSet;
import java.util.Vector;

public final class WaveEffect extends InitBrick {

    record Wave(Vector<IntPair> list) {}
    record LayerWave(Wave layer3, Wave layer2, Wave layer1) {}

    Vector<IntPair> listObjHitDamage;
    Vector<LayerWave> listOfLayer;
    private Matrix matrixOfObj;

    public WaveEffect() {
        super();
        matrixOfObj = new Matrix(rowData, colData, -1);
    }

    public Matrix getStateMatrix() {
        return matrixOfObj;
    }

    public void getListObjHitDamage() {
        listObjHitDamage.clear();
        for (int i = 0; i < rowData; i++) {
            for (int j = 0; j < colData; j++) {
                if(matrixObj.isJustDamaged(i, j)) {
                    listObjHitDamage.add(new IntPair(i, j));
                    matrixObj.resetJustDamaged(i, j);
                }
            }
        }
    }

    public void runAllWave() {
        Matrix stateMatrix = new Matrix(rowData, colData, -1);

        for(int i = 0; i < rowData; i++){
            for(int j = 0; j < colData; j++){
                if(matrixObj.isDestroyed(i, j)) {
                    stateMatrix.set(i, j, -2);
                }
            }
        }

        for(IntPair p : listObjHitDamage) {
            Vector<IntPair> list = new Vector<>();
            list.add(new IntPair(p.fi(), p.se()));
            LayerWave newWave = new LayerWave(null, null, new Wave(list));
        }

        Vector<LayerWave> newListOfLayer = new Vector<>();

        for (int i = 0; i < listOfLayer.size(); i++) {
            Wave fi = listOfLayer.get(i).layer1;
            Wave se = listOfLayer.get(i).layer2;
            Wave thir = listOfLayer.get(i).layer3;

            Wave newFi = new Wave(new Vector<>());
            Wave newSe = new Wave(new Vector<>());
            Wave newThir = new Wave(new Vector<>());

            HashSet<IntPair> Mp = new HashSet<>();
            for(IntPair p : se.list) {
                Mp.add(new IntPair(p.fi(), p.se()));
            }

            for(IntPair p : fi.list) {
                for(int j = 0; j < 8; j++) {
                    int x = p.fi() + fx[i];
                    int y = p.se() + fy[i];

                    if(stateMatrix.valid(x, y) && !Mp.contains(new IntPair(x,y))) {
                        newFi.list.add(new IntPair(x,y));
                    }
                }
            }

            newSe = fi;
            newThir = se;
            if(newFi.list.isEmpty() && newSe.list.isEmpty() && newThir.list.isEmpty()) {
                newListOfLayer.set(i, new LayerWave(newThir, newSe, newFi));
            }
        }

        listOfLayer = newListOfLayer;

        for(LayerWave wave: listOfLayer) {
            for(IntPair p: wave.layer1.list) {
                int x = p.fi();
                int y = p.se();
                if(stateMatrix.get(x, y) != -2) {
                    stateMatrix.set(x, y, 1);
                }
            }

            for(IntPair p: wave.layer2.list) {
                int x = p.fi();
                int y = p.se();
                if(stateMatrix.get(x, y) == -1 ||  stateMatrix.get(x, y) > 2) {
                    stateMatrix.set(x, y, 2);
                }
            }

            for(IntPair p: wave.layer3.list) {
                int x = p.fi();
                int y = p.se();
                if(stateMatrix.get(x, y) == -1) {
                    stateMatrix.set(x, y, 3);
                }
            }
        }

        matrixOfObj.assignFrom(stateMatrix);
    }
}

