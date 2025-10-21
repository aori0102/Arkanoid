package game.Brick.BrickGenMap;

import static game.Brick.InitMatrix.getNewBrick;

import game.Brick.BrickType;
import game.Brick.InitMatrix.BrickMatrix;

public final class GridUtils {
    private GridUtils() {}

    /**
     * Set a cell in matrix to become a new BrickObject that have type t.
     * @param m matrix
     * @param r row
     * @param c col
     * @param t TypeBrick
     */
    public static void setIfIn(BrickMatrix m, int r, int c, BrickType t) {
        if (m == null) return;
        if (m.inBounds(r, c)) m.set(r, c, getNewBrick(t));
    }


    /**
     * Set all BrickObj to become type t.
     * @param m matrix
     * @param t TypeBrick
     */
    public static void fillAll(BrickMatrix m, BrickType t) {
        for (int r = 0; r < m.rows(); r++) {
            for (int c = 0; c < m.columns(); c++) {
                m.set(r, c, getNewBrick(t));
            }
        }
    }

    /**
     * Fill a rectangle area that all object become type t.
     * @param m matrix
     * @param r TopLeft
     * @param c TopLeft
     * @param h BottomRight
     * @param w BottomRight
     * @param t TypeBrick
     */

    public static void fillRect(BrickMatrix m, int r, int c, int h, int w, BrickType t) {
        for (int i = 0; i < h; i++) {
            int _r = r + i;
            if (_r < 0 || _r >= m.rows()) continue;
            for (int j = 0; j < w; j++) {
                int _c = c + j;
                if (_c < 0 || _c >= m.columns()) continue;
                m.set(_r, _c, getNewBrick(t));
            }
        }
    }

    public static void drawRect(BrickMatrix m, int r, int c, int h, int w, BrickType t, boolean hollow) {
        if (!hollow) {
            fillRect(m, r, c, h, w, t);
            return;
        }

        for (int i = 0; i < w; i++) {
            setIfIn(m, r, c + i, t);
            setIfIn(m, r + h - 1, c + i, t);
        }

        for (int i = 0; i < h; i++) {
            setIfIn(m, r + i, c, t);
            setIfIn(m, r + i, c + w - 1, t);
        }
    }
}
