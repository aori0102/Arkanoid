package game.Brick.BrickGenMap.style;

import static game.Brick.BrickGenMap.Mathx.*;

import game.Brick.Init.Matrix;
import game.Brick.BrickGenMap.SpecialsSprinkler;
import game.Brick.BrickGenMap.StyleGenerator;

import java.util.Random;

/**
 * SYMMETRIC: (Original description: "random left half mirrored to the right").
 *
 * <p><b>Actual Generation Logic:</b> This generator creates a symmetric map
 * using a specific two-step process:
 * <ol>
 * <li>It creates a base {@link Matrix} (filled with 0s, i.e.,
 * {@link game.Brick.BrickType#Normal}).</li>
 * <li>It runs {@link SpecialsSprinkler} over the <b>entire</b> grid. This
 * creates a <b>full, non-symmetric</b> map of Normal bricks + special bricks.</li>
 * <li>It then <b>forces symmetry</b> by iterating over the left half of the grid
 * and overwriting each cell with the value from its "mirror" cell
 * on the right half.</li>
 * </ol>
 *
 * <p><b>Result:</b> The final map is horizontally symmetric, where the
 * left side is a perfect mirror of the right side. The originally
 * generated content of the left side is discarded.
 */
public final class SymmetricStyle implements StyleGenerator {
    @Override
    public Matrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        Matrix grid = new Matrix(rows, cols);
        int half = (cols + 1) / 2;
        SpecialsSprinkler.sprinkle(grid, rng, difficulty);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < half; c++) {
                grid.set(r, c, grid.get(r, cols - c - 1));
            }
        }
        return grid;
    }
}
