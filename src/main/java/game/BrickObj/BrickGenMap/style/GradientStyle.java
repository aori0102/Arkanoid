package game.BrickObj.BrickGenMap.style;

import static game.BrickObj.BrickGenMap.TransTypeNumBer.transTypeToNumber;
import static game.BrickObj.BrickGenMap.Mathx.*;

import game.BrickObj.BrickType;
import game.BrickObj.Init.Matrix;
import game.BrickObj.BrickGenMap.SpecialsSprinkler;
import game.BrickObj.BrickGenMap.StyleGenerator;
import game.BrickObj.BrickGenMap.TypePickers;
import java.util.Random;

/** GRADIENT: vertically increasing hardness. */
public final class GradientStyle implements StyleGenerator {
    @Override
    public Matrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        Matrix g = new Matrix(rows, cols);
        for (int r = 0; r < rows; r++) {
            double rowBias = rows <= 1 ? 0.0 : (double) r / (rows - 1);
            double bias = keep01(0.15 + 0.85 * Math.pow(rowBias, 0.75 + 0.5 * difficulty));
            BrickType t = TypePickers.pickByBias(rng, bias);
            for (int c = 0; c < cols; c++) g.set(r, c, transTypeToNumber(t));
        }
        SpecialsSprinkler.sprinkle(g, rng, difficulty);
        return g;
    }
}
