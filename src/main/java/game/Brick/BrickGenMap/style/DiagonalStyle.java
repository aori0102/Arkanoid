package game.Brick.BrickGenMap.style;

import static game.Brick.BrickGenMap.TransTypeNumBer.transTypeToNumber;
import static game.Brick.BrickGenMap.Mathx.*;

import game.Brick.BrickGenMap.SpecialsSprinkler;
import game.Brick.BrickType;
import game.Brick.Init.Matrix;
import game.Brick.BrickGenMap.StyleGenerator;
import java.util.Random;

public final class DiagonalStyle implements StyleGenerator {
    @Override
    public Matrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        Matrix g = new Matrix(rows, cols);
        BrickType normal = BrickType.Normal, steel = BrickType.Steel, diamond = BrickType.Diamond;
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (r == 0 || c == 0 || r == rows - 1 || c == cols - 1)
                    g.set(r, c, transTypeToNumber(diamond));
                else {
                    int k = Math.floorMod(r - c, 4);
                    if (k == 0) g.set(r, c, transTypeToNumber(steel));
                    else if (rng.nextDouble() < 0.1 * difficulty) g.set(r, c, transTypeToNumber(diamond));
                    else g.set(r, c, transTypeToNumber(normal));
                }
        int paths = 2 + rng.nextInt(2);
        for (int p = 0; p < paths; p++) {
            int c = 2 + rng.nextInt(cols - 4);
            for (int r = rows - 2; r > 0; r--) g.set(r, c, transTypeToNumber(normal));
        }
        SpecialsSprinkler.sprinkle(g, rng, difficulty);
        return g;
    }
}
