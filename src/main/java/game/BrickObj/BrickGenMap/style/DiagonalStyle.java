package game.BrickObj.BrickGenMap.style;

import static game.BrickObj.InitMatrix.getNewBrick;
import static game.BrickObj.BrickGenMap.Mathx.*;

import game.BrickObj.BrickType;
import game.BrickObj.InitMatrix.BrickMatrix;
import game.BrickObj.BrickGenMap.SpecialsSprinkler;
import game.BrickObj.BrickGenMap.StyleGenerator;
import game.BrickObj.BrickGenMap.TypePickers;
import java.util.Random;

/** DIAGONAL: repeated diagonal hard stripes. */
public final class DiagonalStyle implements StyleGenerator {
    @Override
    public BrickMatrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        BrickMatrix g = new BrickMatrix(rows, cols);

        int period = Math.max(2, (int)Math.round(lerp(6, 3, difficulty)));
        int thick  = Math.max(1, (int)Math.round(lerp(1, 2, difficulty)));
        BrickType wall = TypePickers.pickFromTopHard(rng, 0.55 + 0.3 * difficulty);

        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++) {
                int k = Math.floorMod(r - c, period);
                if (k < thick) g.set(r, c, getNewBrick(wall));
            }

        SpecialsSprinkler.sprinkle(g, rng, difficulty);
        return g;
    }
}
