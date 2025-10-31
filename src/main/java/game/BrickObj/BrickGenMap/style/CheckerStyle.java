package game.BrickObj.BrickGenMap.style;

import static game.BrickObj.BrickGenMap.TransTypeNumBer.transTypeToNumber;
import static game.BrickObj.BrickGenMap.Mathx.*;

import game.BrickObj.BrickType;
import game.BrickObj.Init.Matrix;
import game.BrickObj.BrickGenMap.SpecialsSprinkler;
import game.BrickObj.BrickGenMap.StyleGenerator;
import java.util.Random;

/** CHECKER: checkerboard with adjustable cell size from difficulty. */
public final class CheckerStyle implements StyleGenerator {
    @Override
    public Matrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        Matrix g = new Matrix(rows, cols);
        BrickType A = BrickType.Normal, B = BrickType.Rock;
        int cellH = Math.max(1, (int)Math.round(lerp(1, 3, difficulty)));
        int cellW = Math.max(1, (int)Math.round(lerp(1, 3, difficulty)));
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                g.set(r, c, transTypeToNumber((((r / cellH) + (c / cellW)) & 1) == 0 ? A : B));
        SpecialsSprinkler.sprinkle(g, rng, difficulty);
        return g;
    }
}
