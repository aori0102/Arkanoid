package game.BrickObj.BrickEvent;

import game.BrickObj.BrickType;
import static game.BrickObj.InitMatrix.*;

public class EndEvent {
    public boolean isEnd() {
        for (int i = 0; i < rowData; i++) {
            for (int j = 0; j < colData; j++) {
                if(matrixObj.isDestroyed(i, j) || matrixObj.getObjType(i, j) == BrickType.Diamond) continue;
                return false;
            }
        }
        return true;
    }
}
