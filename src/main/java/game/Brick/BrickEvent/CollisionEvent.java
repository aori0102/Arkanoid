package game.Brick.BrickEvent;

import game.Brick.BrickObj;
import game.Brick.InitBrick;

import java.util.Collections;
import java.util.Vector;

public final class CollisionEvent extends InitBrick {

    private final static int damage = 10;

    public static void ColliEvent(int r, int c, BrickObj.BrickType brickType) {
        if (!matrixObj.inBounds(r, c)) return;

        matrixObj.hitDamage(r, c, damage);
        if(matrixObj.isDestroyed(r, c)) matrixObj.setObjDeathStatus(r, c);

        switch (brickType) {
            case Ball -> {
                // để trống
            }

            case Bomb -> {
                int splash = Math.max(1, damage / 2);
                for (int i = 0; i < 8; i++) {
                    int x = r + fx[i];
                    int y = c + fy[i];
                    if (matrixObj.inBounds(x, y) && !matrixObj.isDestroyed(x, y)) {
                        matrixObj.decHealth(x, y, splash);
                    }
                }
            }

            case Reborn -> {
                Vector<IntPair> died = new Vector<>();
                for (int i = 0; i < rowData; i++) {
                    for (int j = 0; j < colData; j++) {
                        if (matrixObj.isDestroyed(i, j)) died.add(new IntPair(i, j));
                    }
                }
                if (!died.isEmpty()) {
                    Collections.shuffle(died);
                    int revive = died.size() / 2;
                    for (int i = 0; i < revive; i++) {
                        int x = died.get(i).fi();
                        int y = died.get(i).se(); // FIX: dùng se()
                        matrixObj.set(x, y, new BrickObj(BrickObj.BrickType.Normal));
                    }
                }
            }

            case Angel -> {
                // để trống
            }

            case Gift -> {
                int heal = Math.max(1, damage / 2);
                for (int i = 0; i < 8; i++) {
                    int x = r + fx[i];
                    int y = c + fy[i];
                    if (matrixObj.inBounds(x, y) && !matrixObj.isDestroyed(x, y)) {
                        matrixObj.incHealth(x, y, heal);
                    }
                }
            }

            case Rocket -> {
                // để trống
            }

            case Rock -> {
                for (int i = 0; i < rowData; i++) {
                    matrixObj.beDestroy(i, c);
                }

                for (int i = 0; i < colData; i++) {
                    matrixObj.beDestroy(r, i);
                }
            }

            default -> {
                // các loại còn lại: không có hiệu ứng thêm
            }
        }
    }
}
