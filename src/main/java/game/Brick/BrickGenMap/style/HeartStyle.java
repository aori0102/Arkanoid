package game.Brick.BrickGenMap.style;

import static game.Brick.BrickGenMap.TransTypeNumBer.transTypeToNumber;
import static game.Brick.BrickGenMap.Mathx.*;
import static game.Brick.Init.*;

import game.Brick.BrickType;
import game.Brick.BrickGenMap.StyleGenerator;
import java.util.Random;

/**
 * Generates a brick layout based on an implicit equation for a heart shape.
 * <p>
 * This generator plots a mathematical heart by mapping the grid coordinates (r, c)
 * to a 2D coordinate system defined by ({@code X_MIN}, {@code X_MAX}, {@code Y_MIN}, {@code Y_MAX}).
 * <p>
 * The implicit equation {@code (x^2 + y^2 - 1)^3 - x^2*y^3 = 0} is used.
 * <ul>
 * <li>If a cell's (x, y) coordinates satisfy {@code f <= 0.0}, it is
 * considered <b>inside</b> the heart.</li>
 * <li>Bricks inside the heart are selected using a bias (see {@link #selectByBias(Random, double)}).
 * This bias is stronger (producing harder bricks) near the mathematical edge
 * of the heart, creating a denser "outline."</li>
 * <li>If a cell is <b>outside</b> the heart ({@code f > 0.0}), it is
 * mostly left empty, with a small {@code NOISE_BRICK_CHANCE} of
 * placing a single 'Normal' brick.</li>
 * <li>A frame of "hard" bricks is also generated around the
 * perimeter of the entire grid.</li>
 * </ul>
 *
 * @see StyleGenerator
 * @see TypePickers
 */
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


    /**
     * Generates the heart-shaped brick layout.
     * <p>
     * It iterates through each cell, checking if it's part of the grid edge.
     * If not, it maps the cell to the heart's coordinate system, evaluates the
     * implicit equation ({@code f}), and sets a brick type based on whether
     * the cell is inside ({@code f <= 0.0}) or outside the shape.
     *
     * @param rows       The number of rows for the grid.
     * @param cols       The number of columns for the grid.
     * @param difficulty The difficulty level (0.0 to 1.0), which influences
     * the hardness of the bricks.
     * @param rng        The {@link Random} instance to use for all randomized
     * operations.
     * @return A {@link Matrix} containing the integer representations
     * of the {@link BrickType}s, forming a heart shape.
     */
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

    /**
     * Selects a random {@link BrickType} using a biased-low approach.
     * <p>
     * The {@code bias} (clamped 0.0 to 1.0) determines the *maximum index*
     * (as a fraction of the total number of brick types) from which to choose.
     * A low bias (e.g., 0.1) will only select from the first 10% (easiest) bricks,
     * while a high bias (1.0) can select from any brick.
     *
     * @param rng  The {@link Random} instance.
     * @param bias The selection bias (0.0 = easiest, 1.0 = all types).
     * @return A randomly selected {@link BrickType} based on the bias.
     */
    private static BrickType selectByBias(Random rng, double bias) {
        BrickType[] arr = BrickType.values();
        int maxIx = (int) Math.round(keep01(bias) * (arr.length - 1));

        if (maxIx < 0) maxIx = 0;

        return arr[rng.nextInt(maxIx + 1)];
    }

    /**
     * A helper method to select a 'hard' brick.
     *
     * @param rng     The {@link Random} instance.
     * @param topFrac The fraction of the hardest bricks to select from (e.g.,
     * 0.25 means pick from the top 25% hardest bricks).
     * @return A randomly selected "hard" {@link BrickType}.
     */
    private static BrickType selectHard(Random rng, double topFrac) {
        return game.Brick.BrickGenMap.TypePickers.pickFromTopHard(rng, topFrac);
    }
}