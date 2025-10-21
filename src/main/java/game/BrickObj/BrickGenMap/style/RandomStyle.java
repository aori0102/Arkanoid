package game.BrickObj.BrickGenMap.style;

import static game.BrickObj.InitMatrix.getNewBrick;
import static game.BrickObj.BrickGenMap.Mathx.*;

import game.BrickObj.InitMatrix.BrickMatrix;
import game.BrickObj.BrickGenMap.SpecialsSprinkler;
import game.BrickObj.BrickGenMap.StyleGenerator;
import game.BrickObj.BrickGenMap.TypePickers;
import java.util.Random;


public final class RandomStyle implements StyleGenerator {
    @Override
    public BrickMatrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        BrickMatrix g = new BrickMatrix(rows, cols);
        double density = lerp(0.55, 0.85, 1.0 - difficulty);
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (rng.nextDouble() < density)
                    g.set(r, c, getNewBrick(TypePickers.pickByBias(rng, difficulty)));
        SpecialsSprinkler.sprinkle(g, rng, difficulty);
        return g;
    }
}
