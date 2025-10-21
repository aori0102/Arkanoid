package game.BrickObj.BrickGenMap.style;

import static game.BrickObj.InitMatrix.*;
import static game.BrickObj.BrickGenMap.Mathx.*;
import static game.BrickObj.BrickGenMap.GridUtils.*;

import game.BrickObj.BrickType;
import game.BrickObj.InitMatrix.BrickMatrix;
import game.BrickObj.BrickGenMap.SpecialsSprinkler;
import game.BrickObj.BrickGenMap.StyleGenerator;
import game.BrickObj.BrickGenMap.TypePickers;
import java.util.Random;

/** SINE: a sinusoidal hard band across the grid. */
public final class SineStyle implements StyleGenerator {
    @Override
    public BrickMatrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        BrickMatrix g = new BrickMatrix(rows, cols);
        fillAll(g, BrickType.Normal);

        double amp = lerp(3, 6, difficulty);
        double freq = lerp(0.8, 1.6, difficulty);
        int thickness = Math.max(1, (int)Math.round(lerp(1, 3, difficulty)));
        BrickType wall = TypePickers.pickFromTopHard(rng, 0.55 + 0.35 * difficulty);

        for (int c = 0; c < cols; c++) {
            double x = (2 * Math.PI * c / cols) * freq;
            int r0 = (int) Math.round(rows / 2.0 + amp * Math.sin(x));
            for (int t = -thickness; t <= thickness; t++)
                if (inBounds(r0 + t, c, rows, cols)) g.set(r0 + t, c, getNewBrick(wall));
        }

        SpecialsSprinkler.sprinkle(g, rng, difficulty);
        return g;
    }
}
