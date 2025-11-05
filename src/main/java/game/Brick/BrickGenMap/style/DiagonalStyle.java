package game.Brick.BrickGenMap.style;

import static game.Brick.BrickGenMap.TransTypeNumBer.transTypeToNumber;
import static game.Brick.BrickGenMap.Mathx.*;

import game.Brick.BrickType;
import game.Brick.Init.Matrix;
import game.Brick.BrickGenMap.SpecialsSprinkler;
import game.Brick.BrickGenMap.StyleGenerator;
import game.Brick.BrickGenMap.TypePickers;
import java.util.Random;

/** DIAGONAL: repeated diagonal hard stripes. */
public final class DiagonalStyle implements StyleGenerator {
    @Override
    public Matrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        Matrix g = new Matrix(rows, cols);

        int period = Math.max(2, (int)Math.round(lerp(6, 3, difficulty)));
        int thick  = Math.max(1, (int)Math.round(lerp(1, 2, difficulty)));
        BrickType wall = TypePickers.pickFromTopHard(rng, 0.55 + 0.3 * difficulty);

        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++) {
                int k = Math.floorMod(r - c, period);
                if (k < thick) g.set(r, c, transTypeToNumber(wall));
            }

        SpecialsSprinkler.sprinkle(g, rng, difficulty);
        return g;
    }
}
