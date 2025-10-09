package game.Brick.BrickEvent;

import game.Brick.InitBrick;

public class BrokenEvent extends InitBrick {

    private final int timeAppear = 20;
    private Matrix deathStatus;
    public BrokenEvent(){
        deathStatus = new Matrix(rowData, colData, -1);
    }

    void getDeathStatus(){
        for (int i = 0; i < rowData; i++) {
            for (int j = 0; j < colData; j++) {
                if(matrixObj.isDestroyed(i, j) && matrixObj.getIsNewDeath(i, j)) {
                    deathStatus.set(i, j, 0);
                    matrixObj.setObjDeathStatus(i, j);
                }
            }
        }
    }

    public void runProgcess() {
        for (int i = 0; i <  rowData; i++) {
            for (int j = 0; j < colData; j++) {

                int val = deathStatus.get(i, j);
                if(val == -1) continue;
                if(val > timeAppear) val = -1;
                else val++;

                deathStatus.set(i, j, val);
            }
        }
    }

    public Matrix getDeathMatrix() {
        return deathStatus.clone();
    }
}
