package game.BrickObj.BrickGenMap.style;

import static game.BrickObj.InitMatrix.getNewBrick;
import static game.BrickObj.BrickGenMap.Mathx.*;

import game.BrickObj.Brick;
import game.BrickObj.InitMatrix.BrickMatrix;
import game.BrickObj.BrickGenMap.SpecialsSprinkler;
import game.BrickObj.BrickGenMap.StyleGenerator;

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
