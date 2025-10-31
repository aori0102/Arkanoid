package game.BrickObj.BrickGenMap.style;

import static game.BrickObj.BrickGenMap.TransTypeNumBer.transTypeToNumber;
import static game.BrickObj.Init.*;
import static game.BrickObj.BrickGenMap.Mathx.*;

import game.BrickObj.BrickType;
import game.BrickObj.BrickGenMap.SpecialsSprinkler;
import game.BrickObj.BrickGenMap.StyleGenerator;
import game.BrickObj.BrickGenMap.TypePickers;

import java.util.Random;

/**
 * SPIRAL: rectangular spiral wall with cadence-based gaps.
 */
public final class SpiralStyle implements StyleGenerator {
    @Override
    public Matrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        Matrix g = new Matrix(rows, cols);
        int top = 0, bottom = rows - 1, left = 0, right = cols - 1;
        int gapEvery = (int) Math.max(3, Math.round(lerp(2, 6, 1 - difficulty)));
        int step = 0;

        while (top <= bottom && left <= right) {
            BrickType wall = TypePickers.pickFromTopHard(rng, 0.55 + 0.45 * difficulty);
            for (int c = left; c <= right; c++) setSpiralCell(g, top, c, wall, step++, gapEvery, difficulty, rng);
            for (int r = top + 1; r <= bottom; r++) setSpiralCell(g, r, right, wall, step++, gapEvery, difficulty, rng);
            if (top < bottom) for (int c = right - 1; c >= left; c--)
                setSpiralCell(g, bottom, c, wall, step++, gapEvery, difficulty, rng);
            if (left < right) for (int r = bottom - 1; r > top; r--)
                setSpiralCell(g, r, left, wall, step++, gapEvery, difficulty, rng);
            top++;
            left++;
            bottom--;
            right--;
        }

        SpecialsSprinkler.sprinkle(g, rng, difficulty);
        return g;
    }

    private void setSpiralCell(Matrix g, int r, int c, BrickType wall, int step, int gapEvery, double difficulty, Random rng) {
        if (!inBounds(r, c, g.rows(), g.columns())) return;
        if (step % gapEvery == 0 && rng.nextDouble() < lerp(0.8, 0.25, difficulty)) {
            g.set(r, c, transTypeToNumber(TypePickers.pickFromWeak(rng, difficulty)));
        } else {
            g.set(r, c, transTypeToNumber(wall));
        }
    }
}
