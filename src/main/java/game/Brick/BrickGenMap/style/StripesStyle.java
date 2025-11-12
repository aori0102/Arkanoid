package game.Brick.BrickGenMap.style;

import static game.Brick.BrickGenMap.Mathx.*;
import static game.Brick.BrickGenMap.TransTypeNumBer.transTypeToNumber;

import game.Brick.BrickType;
import game.Brick.Init.Matrix;
import game.Brick.BrickGenMap.SpecialsSprinkler;
import game.Brick.BrickGenMap.StyleGenerator;
import java.util.Random;

/**
 * A {@link StyleGenerator} that creates a map with horizontal bands (stripes).
 *
 * <p>The height of each stripe ({@code stripeH}) scales slightly based on
 * the {@code difficulty} parameter.
 *
 * <p>In this original version, the "palette" of bricks is <b>hard-coded</b>
 * as {@code {Normal, Steel, Diamond}}. The stripes will repeat in this order.
 * Difficulty only affects the stripe *height*, not the *type* of bricks.
 */
public final class StripesStyle implements StyleGenerator {

    /**
     * Generates a horizontal stripe-style map.
     *
     * @param rows The number of rows for the map.
     * @param cols The number of columns for the map.
     * @param difficulty The difficulty (0.0 to 1.0). Used to calculate
     * the stripe height (from 2 to 4).
     * @param rng The {@link Random} generator (mainly used for sprinkling).
     * @return The generated and sprinkled {@link Matrix}.
     */
    @Override
    public Matrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        Matrix g = new Matrix(rows, cols);
        BrickType[] palette = { BrickType.Normal, BrickType.Steel, BrickType.Diamond };
        int stripeH = Math.max(2, (int)Math.round(lerp(2, 4, difficulty * 0.5)));
        for (int r = 0; r < rows; r++) {
            BrickType ty = palette[(r / stripeH) % palette.length];
            for (int c = 0; c < cols; c++) g.set(r, c, transTypeToNumber(ty));
        }
        SpecialsSprinkler.sprinkle(g, rng, difficulty);
        return g;
    }
}
