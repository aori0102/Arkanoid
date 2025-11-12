package game.Brick.BrickGenMap.style;

import static game.Brick.BrickGenMap.TransTypeNumBer.transTypeToNumber;
import static game.Brick.BrickGenMap.Mathx.*;

import game.Brick.BrickType;
import game.Brick.Init.Matrix;
import game.Brick.BrickGenMap.SpecialsSprinkler;
import game.Brick.BrickGenMap.StyleGenerator;
import java.util.Random;

/**
 * A {@link StyleGenerator} that creates a checkerboard-style map.
 * The size of the checkerboard cells ({@code cellH}, {@code cellW}) is
 * adjusted based on the {@code difficulty} parameter.
 *
 * <p>In this original version, the two brick types are <b>hard-coded</b>:
 * <ul>
 * <li>Type A (even cells) is always {@link BrickType#Normal}.</li>
 * <li>Type B (odd cells) is always {@link BrickType#Steel}.</li>
 * </ul>
 * Difficulty only affects the *size* of the cells,
 * not the *type* of bricks used.
 */
public final class CheckerStyle implements StyleGenerator {

    /**
     * Generates a checkerboard-style map.
     *
     * @param rows The number of rows for the map.
     * @param cols The number of columns for the map.
     * @param difficulty The difficulty (0.0 to 1.0). In this version, it
     * controls the size of the checkerboard cells.
     * 0.0 = 1x1 cells, 1.0 = 3x3 cells.
     * @param rng The {@link Random} generator (mainly used for sprinkling).
     * @return The generated and sprinkled {@link Matrix}.
     */
    @Override
    public Matrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        Matrix g = new Matrix(rows, cols);
        BrickType A = BrickType.Normal, B = BrickType.Steel;
        int cellH = Math.max(1, (int)Math.round(lerp(1, 3, difficulty)));
        int cellW = Math.max(1, (int)Math.round(lerp(1, 3, difficulty)));
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++) {
                g.set(r, c, transTypeToNumber(
                        (((r / cellH) + (c / cellW)) & 1) == 0 ? A : B));
            }

        SpecialsSprinkler.sprinkle(g, rng, difficulty);
        return g;
    }
}
