package game.Brick.BrickGenMap.style;

import static game.Brick.BrickGenMap.TransTypeNumBer.transTypeToNumber;
import static game.Brick.Init.*;
import static game.Brick.BrickGenMap.Mathx.*;

import game.Brick.BrickType;
import game.Brick.BrickGenMap.SpecialsSprinkler;
import game.Brick.BrickGenMap.StyleGenerator;
import game.Brick.BrickGenMap.TypePickers;

import java.util.Random;

/**
 * A {@link StyleGenerator} that creates a map with a rectangular spiral wall,
 * starting from the outer edge and moving inwards.
 *
 * <p>The wall is not solid; it contains "gaps" placed at a specific cadence
 * (frequency). The properties of these gaps are cleverly controlled by the
 * {@code difficulty} parameter in two ways:
 * <ol>
 * <li><b>Gap Frequency ({@code gapEvery}):</b> Higher difficulty
 * <i>decreases</i> this value (from 6 down to 3), making potential gap
 * locations <i>more frequent</i>.</li>
 * <li><b>Gap Chance ({@code lerp}):</b> Higher difficulty <i>decreases</i>
 * the probability (from 80% down to 25%) that a gap, when triggered,
 * will actually be created (i.e., it favors placing a hard wall).</li>
 * </ol>
 * <p>This results in easy maps (low difficulty) having infrequent but
 * reliable gaps, while hard maps (high difficulty) have frequent
 * "potential" gaps that are mostly filled in, creating a more solid wall.
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
            for (int c = left; c <= right; c++) {
                setSpiralCell(g, top, c, wall, step++, gapEvery, difficulty, rng);
            }

            for (int r = top + 1; r <= bottom; r++) {
                setSpiralCell(g, r, right, wall, step++, gapEvery, difficulty, rng);
            }

            if (top < bottom) for (int c = right - 1; c >= left; c--) {
                setSpiralCell(g, bottom, c, wall, step++, gapEvery, difficulty, rng);
            }

            if (left < right) for (int r = bottom - 1; r > top; r--) {
                setSpiralCell(g, r, left, wall, step++, gapEvery, difficulty, rng);
            }
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
