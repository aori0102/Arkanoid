package game.BrickObj.BrickGenMap.style;

import static game.BrickObj.BrickGenMap.Mathx.*;

import game.BrickObj.Init.Matrix;
import game.BrickObj.BrickGenMap.SpecialsSprinkler;
import game.BrickObj.BrickGenMap.StyleGenerator;

import java.util.Random;

/** SYMMETRIC: random left half mirrored to the right. */
public final class SymmetricStyle implements StyleGenerator {
    @Override
    public Matrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        Matrix g = new Matrix(rows, cols);
        int half = (cols + 1) / 2;
        SpecialsSprinkler.sprinkle(g, rng, difficulty);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < half; c++) {
                g.set(r, c, g.get(r, cols - c - 1));
            }
        }
        return g;
    }
}
