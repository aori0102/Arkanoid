package game.BrickObj.BrickEvent;

import game.BrickObj.BrickType;
import static game.BrickObj.Init.*;

public class EndEvent {
    public boolean isEnd() {
        for (int r = 0; r < rowData; r++) {
            for (int c = 0; c < colData; c++) {
                if (matrixObj.invalid(r, c)) continue;
                if (matrixObj.isDestroyed(r, c) || matrixObj.getObjType(r, c) == BrickType.Diamond) continue;
                return false;
            }
        }
        return true;
    }
}
