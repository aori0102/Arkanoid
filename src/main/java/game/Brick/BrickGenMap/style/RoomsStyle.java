package game.Brick.BrickGenMap.style;

import static game.Brick.BrickGenMap.Mathx.*;
import static game.Brick.BrickGenMap.TransTypeNumBer.transTypeToNumber;

import game.Brick.BrickType;
import game.Brick.Init.Matrix;
import game.Brick.BrickGenMap.SpecialsSprinkler;
import game.Brick.BrickGenMap.StyleGenerator;
import game.Brick.BrickGenMap.TypePickers;
import java.util.Random;

/**
 * A {@link StyleGenerator} that attempts to create hollow rooms with a
 * sparse interior, as per its description.
 *
 * <p><b>CRITICAL BUG:</b> The current implementation contains a logical flaw
 * that prevents it from matching its description.
 *
 * <p><b>Intended Logic:</b>
 * <ol>
 * <li>Draw a border ("wall") of hard bricks.</li>
 * <li>Fill the <i>inside</i> of that border with "fill" bricks.</li>
 * </ol>
 *
 * <p><b>Actual Logic:</b>
 * <ol>
 * <li>It correctly draws the "wall" border in the first loop.</li>
 * <li>A <b>second loop</b> then iterates over the <b>entire</b> room area
 * (including the border) and overwrites <b>everything</b> with the "fill"
 * brick type.</li>
 * </ol>
 *
 * <p><b>Result:</b> The "wall" is instantly erased. The final map consists of
 * several overlapping, solid rectangles made entirely of the "fill" brick,
 * not hollow rooms.
 */
public final class RoomsStyle implements StyleGenerator {

    /**
     * Generates the "rooms" map.
     *
     * @param rows The number of rows for the map.
     * @param cols The number of columns for the map.
     * @param difficulty The difficulty (0.0 to 1.0). This controls:
     * <ul>
     * <li>The number of rooms to be drawn (from 5 to 8).</li>
     * <li>The hardness of the "wall" (Steel/Diamond).</li>
     * <li>The type of the "fill" (Normal/Steel/Diamond).</li>
     * </ul>
     * @param rng The {@link Random} generator to use.
     * @return The generated {@link Matrix}, which (due to a bug) will
     * contain solid rectangles, not hollow rooms.
     */
    @Override
    public Matrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        Matrix g = new Matrix(rows, cols);

        int roomCount = 3 + (int)Math.round(lerp(2, 5, difficulty));
        for (int k = 0; k < roomCount; k++) {
            int h = 3 + rng.nextInt(Math.max(2, rows / 3));
            int w = 3 + rng.nextInt(Math.max(2, cols / 2));
            h = Math.min(h, rows); w = Math.min(w, cols);
            int r0 = rng.nextInt(Math.max(1, rows - h + 1));
            int c0 = rng.nextInt(Math.max(1, cols - w + 1));
            BrickType wall = TypePickers.pickFromTopHard(rng, 0.5 + 0.5 * difficulty);
            BrickType fill = TypePickers.pickByBias(rng, 0.25 + 0.5 * difficulty);

            for (int r = r0; r < r0 + h && r < rows; r++) {
                for (int c = c0; c < c0 + w && c < cols; c++) {
                    boolean isBorder = (r == r0 || r == r0 + h - 1 || c == c0 || c == c0 + w - 1);
                    if (isBorder) {
                        g.set(r, c, transTypeToNumber(wall));
                    }
                }
            }

            for (int r = r0; r < r0 + h && r < rows; r++) {
                for (int c = c0; c < c0 + w && c < cols; c++) {
                    g.set(r, c, transTypeToNumber(fill));
                }
            }
        }
        SpecialsSprinkler.sprinkle(g, rng, difficulty);
        return g;
    }
}
