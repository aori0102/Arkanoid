package game.Brick.BrickGenMap.style;

import static game.Brick.InitMatrix.getNewBrick;
import static game.Brick.BrickGenMap.Mathx.*;

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
        double density = lerp(0.55, 0.85, 1.0 - difficulty);
        int half = (cols + 1) / 2;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < half; c++) {
                if (rng.nextDouble() < density) {
                    BrickType t = TypePickers.pickByBias(rng, difficulty);
                    g.set(r, c, getNewBrick(t));
                    int m = cols - 1 - c;
                    if (m != c) g.set(r, m, getNewBrick(t));
                }
            }
        }
        SpecialsSprinkler.sprinkle(g, rng, difficulty);
        return g;
    }
}
