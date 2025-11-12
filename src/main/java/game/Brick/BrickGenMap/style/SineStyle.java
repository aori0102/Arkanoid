package game.Brick.BrickGenMap.style;

import static game.Brick.BrickGenMap.TransTypeNumBer.transTypeToNumber;
import static game.Brick.Init.*;
import static game.Brick.BrickGenMap.Mathx.*;

import game.Brick.BrickType;
import game.Brick.BrickGenMap.SpecialsSprinkler;
import game.Brick.BrickGenMap.StyleGenerator;
import game.Brick.BrickGenMap.TypePickers;
import java.util.Random;

/**
 * A {@link StyleGenerator} that creates a map with a "hard band" (a line of
 * hard bricks) that follows a sinusoidal (sine wave) path
 * horizontally across the grid.
 *
 * <p>The remaining cells (the "background") are defaulted to
 * {@link BrickType#Normal} and are then processed by the
 * {@link SpecialsSprinkler}.
 */
public final class SineStyle implements StyleGenerator {
    @Override
    public Matrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        Matrix g = new Matrix(rows, cols);

        double amp = lerp(3, 6, difficulty);
        double freq = lerp(0.8, 1.6, difficulty);
        int thickness = Math.max(1, (int)Math.round(lerp(1, 2, difficulty)));
        BrickType wall = TypePickers.pickFromTopHard(rng, 0.55 + 0.35 * difficulty);

        for (int c = 0; c < cols; c++) {
            double x = (2 * Math.PI * c / cols) * freq;
            int r0 = (int) Math.round(rows / 2.0 + amp * Math.sin(x));
            for (int t = -thickness; t <= thickness; t++)
                if (inBounds(r0 + t, c, rows, cols)) g.set(r0 + t, c, transTypeToNumber(wall));
        }

        SpecialsSprinkler.sprinkle(g, rng, difficulty);
        return g;
    }
}
