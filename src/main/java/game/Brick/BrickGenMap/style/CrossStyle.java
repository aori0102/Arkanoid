package game.Brick.BrickGenMap.style;

import static game.Brick.BrickGenMap.TransTypeNumBer.transTypeToNumber;
import static game.Brick.Init.*;
import static game.Brick.BrickGenMap.Mathx.*;

import game.Brick.BrickType;
import game.Brick.BrickGenMap.SpecialsSprinkler;
import game.Brick.BrickGenMap.StyleGenerator;
import game.Brick.BrickGenMap.TypePickers;
import java.util.Random;

/** CROSS: plus sign and X diagonals of hard walls. */
public final class CrossStyle implements StyleGenerator {
    @Override
    public Matrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        Matrix g = new Matrix(rows, cols);

        BrickType hard = TypePickers.pickFromTopHard(rng, 0.55 + 0.4 * difficulty);
        int midR = rows / 2, midC = cols / 2, thick = Math.max(1, (int)Math.round(lerp(1, 2, difficulty)));

        // vertical & horizontal bars
        for (int r = 0; r < rows; r++)
            for (int t = -thick; t <= thick; t++)
                if (inBounds(r, midC + t, rows, cols)) g.set(r, midC + t, transTypeToNumber(hard));
        for (int c = 0; c < cols; c++)
            for (int t = -thick; t <= thick; t++)
                if (inBounds(midR + t, c, rows, cols)) g.set(midR + t, c, transTypeToNumber(hard));

        // diagonals
        for (int r = 0; r < rows; r++) {
            int c1 = (int) Math.round((long) r * cols / (double) rows);
            int c2 = cols - 1 - c1;
            for (int t = -thick; t <= thick; t++) {
                if (inBounds(r, c1 + t, rows, cols)) g.set(r, c1 + t, transTypeToNumber(hard));
                if (inBounds(r, c2 + t, rows, cols)) g.set(r, c2 + t, transTypeToNumber(hard));
            }
        }

        SpecialsSprinkler.sprinkle(g, rng, difficulty);
        return g;
    }
}
