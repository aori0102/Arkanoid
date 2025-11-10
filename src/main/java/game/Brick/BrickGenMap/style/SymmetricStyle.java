package game.Brick.BrickGenMap.style;

import static game.Brick.BrickGenMap.Mathx.*;

import game.Brick.Init.Matrix;
import game.Brick.BrickGenMap.SpecialsSprinkler;
import game.Brick.BrickGenMap.StyleGenerator;

import java.util.Random;

/**
 * SYMMETRIC: random left half mirrored to the right.
 */
public final class SymmetricStyle implements StyleGenerator {
    @Override
    public Matrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        Matrix grid = new Matrix(rows, cols);
        int half = (cols + 1) / 2;
        SpecialsSprinkler.sprinkle(grid, rng, difficulty);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < half; c++) {
                grid.set(r, c, grid.get(r, cols - c - 1));
            }
        }
        return grid;
    }
}
