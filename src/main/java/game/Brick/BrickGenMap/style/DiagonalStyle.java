package game.Brick.BrickGenMap.style;

import static game.Brick.BrickGenMap.TransTypeNumBer.transTypeToNumber;
import static game.Brick.BrickGenMap.Mathx.*;

import game.Brick.BrickGenMap.SpecialsSprinkler;
import game.Brick.BrickType;
import game.Brick.Init.Matrix;
import game.Brick.BrickGenMap.StyleGenerator;
import java.util.Random;

/**
 * Generates a layout featuring repeating diagonal stripes, complemented by a few
 * vertical "corridors."
 * <p>
 * This generator follows these steps:
 * <ol>
 * <li><b>Border:</b> Creates an indestructible 'Diamond' brick border around the grid.</li>
 * <li><b>Diagonal Stripes:</b> Fills the interior using a modulo operation on the
 * coordinates ({@code (row - col) % 4}).
 * <ul>
 * <li>If {@code (r - c) % 4 == 0}, a diagonal stripe of 'Steel' bricks is created.</li>
 * <li>Otherwise, the stripes are filled with 'Normal' bricks.</li>
 * <li>Within these 'Normal' stripes, there is a small, difficulty-scaled
 * chance ({@code 0.1 * difficulty}) of a 'Diamond' brick spawning,
 * creating a "minefield" effect.</li>
 * </ul>
 * </li>
 * <li><b>Vertical Paths:</b> Carves 2 or 3 vertical corridors of 'Normal' bricks
 * at random column positions, cutting through the diagonal stripes from the
 * bottom border upwards.</li>
 * <li><b>Sprinkle Specials:</b> Finally, {@link SpecialsSprinkler} adds special bricks
 * to the 'Normal' brick areas.</li>
 * </ol>
 *
 * @see StyleGenerator
 * @see SpecialsSprinkler
 */
public final class DiagonalStyle implements StyleGenerator {
    /**
     * Generates the diagonal-stripe-based brick layout.
     *
     * @param rows       The number of rows for the grid.
     * @param cols       The number of columns for the grid.
     * @param difficulty The difficulty level (0.0 to 1.0), which influences the
     * chance of 'Diamond' bricks spawning and the density
     * of special bricks.
     * @param rng        The {@link Random} instance to use for all randomized
     * operations (diamond placement, path columns).
     * @return A {@link Matrix} containing the integer representations
     * of the {@link BrickType}s, forming a diagonal pattern.
     */
    @Override
    public Matrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        Matrix g = new Matrix(rows, cols);
        BrickType normal = BrickType.Normal, steel = BrickType.Steel, diamond = BrickType.Diamond;
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (r == 0 || c == 0 || r == rows - 1 || c == cols - 1)
                    g.set(r, c, transTypeToNumber(diamond));
                else {
                    int k = Math.floorMod(r - c, 4);
                    if (k == 0) g.set(r, c, transTypeToNumber(steel));
                    else if (rng.nextDouble() < 0.1 * difficulty) g.set(r, c, transTypeToNumber(diamond));
                    else g.set(r, c, transTypeToNumber(normal));
                }
        int paths = 2 + rng.nextInt(2);
        for (int p = 0; p < paths; p++) {
            int c = 2 + rng.nextInt(cols - 4);
            for (int r = rows - 2; r > 0; r--) g.set(r, c, transTypeToNumber(normal));
        }
        SpecialsSprinkler.sprinkle(g, rng, difficulty);
        return g;
    }
}
