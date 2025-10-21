package game.Brick.BrickGenMap.style;

import static game.Brick.InitMatrix.getNewBrick;
import static game.Brick.BrickGenMap.Mathx.*;

import game.Brick.BrickType;
import game.Brick.InitMatrix.BrickMatrix;
import game.Brick.BrickGenMap.SpecialsSprinkler;
import game.Brick.BrickGenMap.StyleGenerator;
import java.util.Random;

/** STRIPES: horizontal bands; band height scales slightly with difficulty. */
public final class StripesStyle implements StyleGenerator {
    @Override
    public BrickMatrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        BrickMatrix g = new BrickMatrix(rows, cols);
        BrickType[] palette = { BrickType.Normal, BrickType.Rock, BrickType.Steel, BrickType.Diamond };
        int stripeH = Math.max(2, (int)Math.round(lerp(2, 4, difficulty * 0.5)));
        for (int r = 0; r < rows; r++) {
            BrickType ty = palette[(r / stripeH) % palette.length];
            for (int c = 0; c < cols; c++) g.set(r, c, getNewBrick(ty));
        }
        SpecialsSprinkler.sprinkle(g, rng, difficulty);
        return g;
    }
}
