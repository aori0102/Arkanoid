package game.Brick.BrickGenMap.style;

import static game.Brick.BrickGenMap.TransTypeNumBer.transTypeToNumber;
import static game.Brick.Init.*;
import static game.Brick.BrickGenMap.Mathx.*;

import game.Brick.BrickType;
import game.Brick.BrickGenMap.SpecialsSprinkler;
import game.Brick.BrickGenMap.StyleGenerator;
import game.Brick.BrickGenMap.TypePickers;
import game.Brick.Init.Matrix;

import java.util.Random;

/**
 * A {@link StyleGenerator} that creates a "Cross" pattern by combining
 * vertical, horizontal, and diagonal lines.
 *
 * <p>This generator follows a specific randomized logic:
 * <ul>
 * <li>There is a 50% chance it draws <b>both</b> a plus sign (+) and an X (X).</li>
 * <li>There is a 25% chance it draws <b>only</b> a plus sign (+).</li>
 * <li>There is a 25% chance it draws <b>only</b> an X (X).</li>
 * </ul>
 *
 * <p>All lines are drawn from a randomly offset center point ({@code midR}, {@code midC}).
 * The background is initialized to {@link BrickType#Normal} (value 0) and then
 * filled by the {@link SpecialsSprinkler}.
 *
 * <p>Note: This version does <em>not</em> create a "hole" in the center.
 */
public final class CrossStyle implements StyleGenerator {

    /**
     * Generates the cross-pattern map.
     *
     * @param rows The number of rows for the map.
     * @param cols The number of columns for the map.
     * @param difficulty The difficulty (0.0 to 1.0). This controls:
     * <ul>
     * <li>The hardness (Steel/Diamond) of the {@code hard} brick.</li>
     * <li>The thickness of the lines (interpolating from 0 to 2).</li>
     * </ul>
     * @param rng The {@link Random} generator, used for logic branching,
     * offsets, and brick selection.
     * @return The generated and sprinkled {@link Matrix}.
     */
    @Override
    public Matrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        Matrix g = new Matrix(rows, cols);

        BrickType hard = TypePickers.pickFromTopHard(rng, 0.55 + 0.4 * difficulty);

        int thick = Math.max(1, (int)Math.round(lerp(1, 2, difficulty)));

        int rOffsetRange = Math.max(1, rows / 4);
        int cOffsetRange = Math.max(1, cols / 4);
        int midR = (rows / 2) + rng.nextInt(-rOffsetRange, rOffsetRange + 1);
        int midC = (cols / 2) + rng.nextInt(-cOffsetRange, cOffsetRange + 1);

        if (rng.nextBoolean()) {
            if (rng.nextBoolean()) {
                drawVerticalLine(g, rows, cols, midC, thick, hard, midR, midC);
                drawHorizontalLine(g, rows, cols, midR, thick, hard, midR, midC);
            } else {
                drawDiagonals(g, rows, cols, thick, hard, midR, midC);
            }
        }
        else {
            drawVerticalLine(g, rows, cols, midC, thick, hard, midR, midC);
            drawHorizontalLine(g, rows, cols, midR, thick, hard, midR, midC);
            drawDiagonals(g, rows, cols, thick, hard, midR, midC);
        }


        SpecialsSprinkler.sprinkle(g, rng, difficulty);
        return g;
    }

    private void drawVerticalLine(Matrix g, int rows, int cols, int midC,
                                  int thick, BrickType hard, int midR, int holeC) {

        int r_up = midR;
        while (r_up >= 0) {
            for (int t = -thick; t <= thick; t++) {
                int nc = midC + t;
                if (inBounds(r_up, nc, rows, cols)) {
                    g.set(r_up, nc, transTypeToNumber(hard));
                }
            }
            r_up--;
        }

        int r_down = midR + 1;
        while (r_down < rows) {
            for (int t = -thick; t <= thick; t++) {
                int nc = midC + t;
                if (inBounds(r_down, nc, rows, cols)) {
                    g.set(r_down, nc, transTypeToNumber(hard));
                }
            }
            r_down++;
        }
    }

    private void drawHorizontalLine(Matrix g, int rows, int cols,
                                    int midR, int thick, BrickType hard, int holeR, int midC) {

        int c_left = midC;
        while (c_left >= 0) {
            for (int t = -thick; t <= thick; t++) {
                int nr = midR + t;
                if (inBounds(nr, c_left, rows, cols)) {
                    g.set(nr, c_left, transTypeToNumber(hard));
                }
            }
            c_left--;
        }

        int c_right = midC + 1;
        while (c_right < cols) {
            for (int t = -thick; t <= thick; t++) {
                int nr = midR + t;
                if (inBounds(nr, c_right, rows, cols)) {
                    g.set(nr, c_right, transTypeToNumber(hard));
                }
            }
            c_right++;
        }
    }

    private void drawDiagonals(Matrix g, int rows, int cols, int thick,
                               BrickType hard, int midR, int midC) {

        int[] dirX = {1, 1, -1, -1};
        int[] dirY = {1, -1, 1, -1};

        int maxLen = Math.max(rows, cols);

        for (int i = 0; i < 4; i++) {
            int dr = dirX[i];
            int dc = dirY[i];

            for (int t = 0; t < maxLen; t++) {
                int r_center = midR + t * dr;
                int c_center = midC + t * dc;

                boolean isOutside = true;
                for (int dt = -thick; dt <= thick; dt++) {
                    int nr = r_center + dt;
                    int nc = c_center;

                    if (inBounds(nr, nc, rows, cols)) {
                        isOutside = false;
                        g.set(nr, nc, transTypeToNumber(hard));
                    }
                }

                if (isOutside) {
                    break;
                }
            }
        }
    }

    private boolean inBounds(int r, int c, int rows, int cols) {
        return r >= 0 && r < rows && c >= 0 && c < cols;
    }
}