package game.Brick.BrickGenMap.style;

import static game.Brick.BrickGenMap.TransTypeNumBer.transTypeToNumber;
import static game.Brick.BrickGenMap.Mathx.*;

import game.Brick.BrickType;
import game.Brick.Init.Matrix;
import game.Brick.BrickGenMap.SpecialsSprinkler;
import game.Brick.BrickGenMap.StyleGenerator;
import java.util.Random;

public final class GradientStyle implements StyleGenerator {
    @Override
    public Matrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        Matrix g = new Matrix(rows, cols);
        BrickType[] types = {BrickType.Normal, BrickType.Steel, BrickType.Diamond};
        for (int r = 0; r < rows; r++) {
            double f = (double) (rows - 1 - r) / (rows - 1);
            int idx = (int) ((f * 2 + rng.nextDouble() * 0.5) % 3);
            BrickType t = types[idx];
            for (int c = 0; c < cols; c++) g.set(r, c, transTypeToNumber(t));
        }
        SpecialsSprinkler.sprinkle(g, rng, difficulty);
        return g;
    }
}
