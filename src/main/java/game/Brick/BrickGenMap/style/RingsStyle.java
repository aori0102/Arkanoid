package game.Brick.BrickGenMap.style;

import static game.Brick.BrickGenMap.Mathx.*;
import static game.Brick.BrickGenMap.TransTypeNumBer.transTypeToNumber;

import game.Brick.BrickType;
import game.Brick.Init.Matrix;
import game.Brick.BrickGenMap.SpecialsSprinkler;
import game.Brick.BrickGenMap.StyleGenerator;
import game.Brick.BrickGenMap.TypePickers;
import java.util.Random;

/** RINGS: concentric rectangular rings with occasional doorways. */
public final class RingsStyle implements StyleGenerator {
    @Override
    public Matrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        Matrix g = new Matrix(rows, cols);
        int layers = Math.min(rows, cols) / 2;
        for (int d = 0; d < layers; d += 2) {
            int r0 = d, c0 = d, h = rows - 2 * d, w = cols - 2 * d;
            if (h <= 1 || w <= 1) break;
            double t = (double) d / Math.max(1, layers - 1);
            BrickType wall = TypePickers.pickByBias(rng, keep01(0.35 + (1.0 - t) * (0.55 * difficulty + 0.15)));

            for (int r = r0; r < r0 + h && r < rows; r++) {
                for (int c = c0; c < c0 + w && c < cols; c++) {
                    boolean isBorder = (r == r0 || r == r0 + h - 1 || c == c0 || c == c0 + w - 1);
                    if (isBorder) {
                        g.set(r, c, transTypeToNumber(wall));
                    }
                }
            }

            if (rng.nextDouble() < lerp(0.8, 0.3, difficulty)) {
                int side = rng.nextInt(4);
                switch (side) {
                    case 0 -> g.set(r0, c0 + rng.nextInt(w),transTypeToNumber(TypePickers.pickFromWeak(rng, difficulty)));
                    case 1 -> g.set(r0 + h - 1, c0 + rng.nextInt(w), transTypeToNumber(TypePickers.pickFromWeak(rng, difficulty)));
                    case 2 -> g.set(r0 + rng.nextInt(h), c0, transTypeToNumber(TypePickers.pickFromWeak(rng, difficulty)));
                    default -> g.set(r0 + rng.nextInt(h), c0 + w - 1, transTypeToNumber(TypePickers.pickFromWeak(rng, difficulty)));
                }
            }
        }

        SpecialsSprinkler.sprinkle(g, rng, difficulty);
        return g;
    }
}
