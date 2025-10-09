package game.Brick.BrickEvent;

import game.Brick.BrickObj;
import game.Brick.InitBrick;

public class EndEvent extends InitBrick {
    public boolean isEnd() {
        for (int i = 0; i < rowData; i++) {
            for (int j = 0; j < colData; j++) {
                if(matrixObj.isDestroyed(i, j) || matrixObj.getObjType(i, j) == BrickObj.BrickType.Diamond) continue;
                return false;
            }
        }
        return true;
    }
}
