package game.Brick.BrickEvent;

import game.Brick.BrickType;
import static game.Brick.InitMatrix.*;

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
