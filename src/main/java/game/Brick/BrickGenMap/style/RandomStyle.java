package game.Brick.BrickGenMap.style;

import static game.Brick.BrickGenMap.TransTypeNumBer.transTypeToNumber;
import static game.Brick.BrickGenMap.Mathx.*;

import game.Brick.Init.Matrix;
import game.Brick.BrickGenMap.SpecialsSprinkler;
import game.Brick.BrickGenMap.StyleGenerator;
import game.Brick.BrickGenMap.TypePickers;
import java.util.Random;


public final class RandomStyle implements StyleGenerator {
    @Override
    public Matrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        Matrix g = new Matrix(rows, cols);
        double density = lerp(0.55, 0.85, 1.0 - difficulty);
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (rng.nextDouble() < density)
                    g.set(r, c, transTypeToNumber(TypePickers.pickByBias(rng, difficulty)));
        SpecialsSprinkler.sprinkle(g, rng, difficulty);
        return g;
    }
}
