package game.Brick.BrickGenMap.style;

import static game.Brick.InitMatrix.getNewBrick;
import static game.Brick.BrickGenMap.Mathx.*;

import game.Brick.Brick;
import game.Brick.InitMatrix.BrickMatrix;
import game.Brick.BrickGenMap.SpecialsSprinkler;
import game.Brick.BrickGenMap.StyleGenerator;
import game.Brick.BrickGenMap.TypePickers;
import game.Brick.BrickType;
import java.util.Random;

/** SYMMETRIC: random left half mirrored to the right. */
public final class SymmetricStyle implements StyleGenerator {
    @Override
    public BrickMatrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        BrickMatrix g = new BrickMatrix(rows, cols);
        int half = (cols + 1) / 2;
        SpecialsSprinkler.sprinkle(g, rng, difficulty);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < half; c++) {
                Brick val = g.get(r, cols - c - 1);
                g.set(r, c, getNewBrick(val.getBrickType()));
            }
        }
        return g;
    }
}
