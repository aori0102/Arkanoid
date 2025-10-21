package game.Brick.BrickGenMap.style;

import static game.Brick.InitMatrix.getNewBrick;
import static game.Brick.BrickGenMap.Mathx.*;

import game.Brick.BrickType;
import game.Brick.InitMatrix.BrickMatrix;
import game.Brick.BrickGenMap.SpecialsSprinkler;
import game.Brick.BrickGenMap.StyleGenerator;
import java.util.Random;

/** CHECKER: checkerboard with adjustable cell size from difficulty. */
public final class CheckerStyle implements StyleGenerator {
    @Override
    public BrickMatrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        BrickMatrix g = new BrickMatrix(rows, cols);
        BrickType A = BrickType.Normal, B = BrickType.Rock;
        int cellH = Math.max(1, (int)Math.round(lerp(1, 3, difficulty)));
        int cellW = Math.max(1, (int)Math.round(lerp(1, 3, difficulty)));
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                g.set(r, c, getNewBrick((((r / cellH) + (c / cellW)) & 1) == 0 ? A : B));
        SpecialsSprinkler.sprinkle(g, rng, difficulty);
        return g;
    }
}
