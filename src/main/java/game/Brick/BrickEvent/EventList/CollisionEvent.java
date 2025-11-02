package game.Brick.BrickEvent.EventList;

import game.Brick.Brick;
import game.Brick.BrickEvent.Event;
import game.Brick.BrickType;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import static game.BrickObj.Init.*;
import static game.BrickObj.Init.colData;
import static game.BrickObj.Init.fx;
import static game.BrickObj.Init.fy;
import static game.BrickObj.Init.getNewBrick;
import static game.BrickObj.Init.matrixObj;
import static game.BrickObj.Init.rowData;

public class CollisionEvent implements Event {

    private final int rowData;
    private final int colData;
    private final List<List<Brick>> brickGrid;

    public  CollisionEvent(int row, int col, List<List<Brick>> brickGrid) {
        this.rowData = row;
        this.colData = col;
        this.brickGrid = brickGrid;
    }

    @Override
    public void runEvent() {

        for (int r = 0; r < rowData; r++) {
            for (int c = 0; c < colData; c++) {
                if(brickGrid.get(r).get(c) != null
                    && brickGrid.get(r).get(c).isJustDamaged()) {
                    excuteBrick(r, c);
                }
            }
        }
    }

    private void excuteBrick(int r, int c) {
        BrickType type = brickGrid.get(r).get(c).getBrickType();
//        switch (type) {
//
//            case Bomb -> {
//                int splash = Math.max(1, damage / 2);
//                for (int i = 0; i < 8; i++) {
//                    int x = r + fx[i];
//                    int y = c + fy[i];
//                    if (matrixObj.inBounds(x, y) && !matrixObj.isDestroyed(x, y)) {
//                        matrixObj.decHealth(x, y, splash);
//                    }
//                }
//            }
//
//            case Reborn -> {
//                Vector<IntPair> died = new Vector<>();
//                for (int i = 0; i < rowData; i++) {
//                    for (int j = 0; j < colData; j++) {
//                        if (matrixObj.isDestroyed(i, j)) died.add(new IntPair(i, j));
//                    }
//                }
//                if (!died.isEmpty()) {
//                    Collections.shuffle(died);
//                    int revive = died.size() / 2;
//                    for (int i = 0; i < revive; i++) {
//                        int x = died.get(i).fi();
//                        int y = died.get(i).se();
//
//                        matrixObj.set(x, y, getNewBrick(game.BrickObj.BrickType.Normal));
//                    }
//                }
//            }
//
//            case Angel -> {
//                // để trống
//            }
//
//            case Gift -> {
//                int heal = Math.max(1, damage / 2);
//                for (int i = 0; i < 8; i++) {
//                    int x = r + fx[i];
//                    int y = c + fy[i];
//                    if (matrixObj.inBounds(x, y) && !matrixObj.isDestroyed(x, y)) {
//                        matrixObj.incHealth(x, y, heal);
//                    }
//                }
//            }
//
//            case Rocket -> {
//                // để trống
//            }
//
//            case Rock -> {
//                for (int i = 0; i < rowData; i++) {
//                    matrixObj.beDestroy(i, c);
//                }
//
//                for (int i = 0; i < colData; i++) {
//                    matrixObj.beDestroy(r, i);
//                }
//            }
//
//            default -> {
//                // các loại còn lại: không có hiệu ứng thêm
//            }
//        }
    }
}
