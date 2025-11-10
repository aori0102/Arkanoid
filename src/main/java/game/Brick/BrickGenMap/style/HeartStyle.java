package game.Brick.BrickGenMap.style;

import static game.Brick.BrickGenMap.TransTypeNumBer.transTypeToNumber;
import static game.Brick.BrickGenMap.Mathx.*;
import static game.Brick.Init.*;

import game.Brick.BrickType;
import game.Brick.BrickGenMap.StyleGenerator;
import java.util.Random;

public final class HeartStyle implements StyleGenerator {

    private static final double X_MIN = -1.3;
    private static final double X_MAX = 1.3;
    private static final double Y_MIN = 1.3;
    private static final double Y_MAX = -1.1;

    private static final double EDGE_GRADIENT_FACTOR = 40.0;
    private static final double NOISE_BRICK_CHANCE = 0.06;

    private static final double BASE_BIAS = 0.25;
    private static final double BIAS_DIFFICULTY_SCALE = 0.6;
    private static final double BIAS_EDGE_SCALE = 0.15;

    private static final double GRID_EDGE_HARDEN_CHANCE = 0.75;
    private static final double EDGE_HARDNESS_BASE = 0.6;
    private static final double EDGE_HARDNESS_DIFFICULTY_SCALE = 0.35;


    @Override
    public Matrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        Matrix g = new Matrix(rows, cols);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                boolean isGridEdgeCell = (r == 0 || r == rows - 1 || c == 0 || c == cols - 1);

                if (isGridEdgeCell && rng.nextDouble() < GRID_EDGE_HARDEN_CHANCE) {
                    double hardness = keep01(EDGE_HARDNESS_BASE + EDGE_HARDNESS_DIFFICULTY_SCALE * difficulty);
                    g.set(r, c, transTypeToNumber(selectHard(rng, hardness)));

                } else {
                    double x = map(c, 0, cols - 1, X_MIN, X_MAX);
                    double y = map(r, 0, rows - 1, Y_MIN, Y_MAX);

                    double f = Math.pow(x*x + y*y - 1.0, 3) - x*x*Math.pow(y, 3);

                    if (f <= 0.0) {
                        double edge = keep01(1.0 - Math.min(1.0, Math.abs(f) * EDGE_GRADIENT_FACTOR));
                        double bias = keep01(BASE_BIAS + BIAS_DIFFICULTY_SCALE * difficulty + BIAS_EDGE_SCALE * edge);
                        g.set(r, c, transTypeToNumber(selectByBias(rng, bias)));

                    } else if (rng.nextDouble() < NOISE_BRICK_CHANCE) {
                        g.set(r, c, transTypeToNumber(BrickType.Normal));
                    }
                }
            }
        }

        return g;
    }

    private static BrickType selectByBias(Random rng, double bias) {
        BrickType[] arr = BrickType.values();
        int maxIx = (int) Math.round(keep01(bias) * (arr.length - 1));

        if (maxIx < 0) maxIx = 0;

        return arr[rng.nextInt(maxIx + 1)];
    }

    private static BrickType selectHard(Random rng, double topFrac) {
        return game.Brick.BrickGenMap.TypePickers.pickFromTopHard(rng, topFrac);
    }
}