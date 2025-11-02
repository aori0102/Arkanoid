package game.Brick.BrickGenMap.style;

import static game.Brick.BrickGenMap.TransTypeNumBer.transTypeToNumber;
import static game.Brick.BrickGenMap.Mathx.*;
import static game.Brick.Init.*;

import game.Brick.BrickType;
import game.Brick.BrickGenMap.SpecialsSprinkler;
import game.Brick.BrickGenMap.StyleGenerator;
import java.util.Random;

/** HEART: parametric heart shape; edges hardened. */
public final class HeartStyle implements StyleGenerator {
    @Override
    public Matrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        Matrix g = new Matrix(rows, cols);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                double x = map(c, 0, cols - 1, -1.3, 1.3);
                double y = map(r, 0, rows - 1,  1.3, -1.1);
                double f = Math.pow(x*x + y*y - 1.0, 3) - x*x*Math.pow(y, 3);
                if (f <= 0.0) {
                    double edge = keep01(1.0 - Math.min(1.0, Math.abs(f) * 40));
                    double bias = keep01(0.25 + 0.6 * difficulty + 0.15 * edge);
                    g.set(r, c, transTypeToNumber(selectByBias(rng, bias)));
                } else if (rng.nextDouble() < 0.06) {
                    g.set(r, c, transTypeToNumber(BrickType.Normal));
                }
            }
        }

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                boolean edgeCell = false;
                for (int dr = -1; dr <= 1 && !edgeCell; dr++)
                    for (int dc = -1; dc <= 1 && !edgeCell; dc++) {
                        if (dr == 0 && dc == 0) continue;
                        int nr = r + dr, nc = c + dc;
                        if (!inBounds(nr, nc, rows, cols)) edgeCell = true;
                    }
                if (edgeCell && rng.nextDouble() < 0.75) {
                    g.set(r, c, transTypeToNumber(selectHard(rng, 0.6 + 0.35 * difficulty)));
                }
            }
        }

        SpecialsSprinkler.sprinkle(g, rng, difficulty);
        return g;
    }

    private static BrickType selectByBias(Random rng, double bias) {
        BrickType[] arr = BrickType.values();
        int maxIx = (int) Math.round(keep01(bias) * (arr.length - 1));
        return arr[rng.nextInt(maxIx + 1)];
    }
    private static BrickType selectHard(Random rng, double topFrac) {
        return game.Brick.BrickGenMap.TypePickers.pickFromTopHard(rng, topFrac);
    }
}
