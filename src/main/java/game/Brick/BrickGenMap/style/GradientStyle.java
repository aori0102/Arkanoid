package game.Brick.BrickGenMap.style;

import static game.Brick.BrickGenMap.TransTypeNumBer.transTypeToNumber;
import static game.Brick.BrickGenMap.Mathx.*;

import game.Brick.BrickType;
import game.Brick.Init.Matrix;
import game.Brick.BrickGenMap.SpecialsSprinkler;
import game.Brick.BrickGenMap.StyleGenerator;
import java.util.Random;

/**
 * Generates a simple vertical gradient of brick types.
 * <p>
 * This generator creates horizontal stripes of bricks, transitioning
 * (with some randomization) between 'Normal', 'Steel', and 'Diamond' types
 * from the top of the grid to the bottom.
 * <ol>
 * <li>It iterates through each row {@code r} from top to bottom.</li>
 * <li>It calculates a factor {@code f} based on the row's vertical position
 * (where {@code f} is 0.0 for the top row and 1.0 for the bottom row).</li>
 * <li>This factor is used to select a {@link BrickType} from a predefined
 * array: {@code {Normal, Steel, Diamond}}. A small random value is added
 * to slightly blur the transition boundaries between stripes.</li>
 * <li>The *entire row* is then filled with the selected brick type.</li>
 * <li>Finally, {@link SpecialsSprinkler} is used to add special bricks
 * over the generated gradient.</li>
 * </ol>
 *
 * @see StyleGenerator
 * @see SpecialsSprinkler
 */
public final class GradientStyle implements StyleGenerator {

    /**
     * Generates the vertical gradient brick layout.
     *
     * @param rows       The number of rows for the grid.
     * @param cols       The number of columns for the grid.
     * @param difficulty The difficulty level (0.0 to 1.0), used only by the
     * {@link SpecialsSprinkler}.
     * @param rng        The {@link Random} instance to use for randomization.
     * @return A {@link Matrix} containing the integer representations
     * of the {@link BrickType}s, forming a vertical gradient.
     */
    @Override
    public Matrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        Matrix g = new Matrix(rows, cols);
        BrickType[] types = {BrickType.Normal, BrickType.Steel, BrickType.Diamond};
        for (int r = 0; r < rows; r++) {
            double f = (double) (rows - 1 - r) / (rows - 1);
            int idx = (int) ((f * 2 + rng.nextDouble() * 0.5) % 3);
            BrickType t = types[idx];
            for (int c = 0; c < cols; c++) g.set(r, c, transTypeToNumber(t));
        }
        SpecialsSprinkler.sprinkle(g, rng, difficulty);
        return g;
    }
}
