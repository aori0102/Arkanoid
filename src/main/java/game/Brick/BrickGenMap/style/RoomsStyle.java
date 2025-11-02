package game.Brick.BrickGenMap.style;

import static game.Brick.BrickGenMap.Mathx.*;
import static game.Brick.BrickGenMap.TransTypeNumBer.transTypeToNumber;

import game.Brick.BrickType;
import game.Brick.Init.Matrix;
import game.Brick.BrickGenMap.SpecialsSprinkler;
import game.Brick.BrickGenMap.StyleGenerator;
import game.Brick.BrickGenMap.TypePickers;
import java.util.Random;

/** ROOMS: hollow rooms with sparse interior. */
public final class RoomsStyle implements StyleGenerator {
    @Override
    public Matrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        Matrix g = new Matrix(rows, cols);

        int roomCount = 3 + (int)Math.round(lerp(2, 5, difficulty));
        for (int k = 0; k < roomCount; k++) {
            int h = 3 + rng.nextInt(Math.max(2, rows / 3));
            int w = 3 + rng.nextInt(Math.max(2, cols / 2));
            h = Math.min(h, rows); w = Math.min(w, cols);
            int r0 = rng.nextInt(Math.max(1, rows - h + 1));
            int c0 = rng.nextInt(Math.max(1, cols - w + 1));
            BrickType wall = TypePickers.pickFromTopHard(rng, 0.5 + 0.5 * difficulty);
            BrickType fill = TypePickers.pickByBias(rng, 0.25 + 0.5 * difficulty);

            for (int r = r0; r < r0 + h && r < rows; r++) {
                for (int c = c0; c < c0 + w && c < cols; c++) {
                    boolean isBorder = (r == r0 || r == r0 + h - 1 || c == c0 || c == c0 + w - 1);
                    if (isBorder) {
                        g.set(r, c, transTypeToNumber(wall));
                    }
                }
            }

            for (int r = r0; r < r0 + h && r < rows; r++) {
                for (int c = c0; c < c0 + w && c < cols; c++) {
                    g.set(r, c, transTypeToNumber(fill));
                }
            }
        }
        SpecialsSprinkler.sprinkle(g, rng, difficulty);
        return g;
    }
}
