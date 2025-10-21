package game.Brick.BrickGenMap.style;

import static game.Brick.InitMatrix.getNewBrick;
import static game.Brick.BrickGenMap.Mathx.*;

import game.Brick.BrickType;
import game.Brick.InitMatrix.BrickMatrix;
import game.Brick.BrickGenMap.SpecialsSprinkler;
import game.Brick.BrickGenMap.StyleGenerator;
import game.Brick.BrickGenMap.TypePickers;
import java.util.Random;

/** GRADIENT: vertically increasing hardness. */
public final class GradientStyle implements StyleGenerator {
    @Override
    public BrickMatrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        BrickMatrix g = new BrickMatrix(rows, cols);
        for (int r = 0; r < rows; r++) {
            double rowBias = rows <= 1 ? 0.0 : (double) r / (rows - 1);
            double bias = keep01(0.15 + 0.85 * Math.pow(rowBias, 0.75 + 0.5 * difficulty));
            BrickType t = TypePickers.pickByBias(rng, bias);
            for (int c = 0; c < cols; c++) g.set(r, c, getNewBrick(t));
        }
        SpecialsSprinkler.sprinkle(g, rng, difficulty);
        return g;
    }
}
