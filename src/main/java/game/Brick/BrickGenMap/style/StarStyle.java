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
 * A {@link StyleGenerator} that creates a "starburst" pattern.
 *
 * <p>It generates multiple "rays" (straight lines) of hard bricks
 * (Steel or Diamond) that originate from the center of the map.
 * The number of rays and the "hardness" of the bricks scale
 * with difficulty.
 *
 * <p>The thickness of the rays is handled by a simple, but flawed,
 * logic that adds extra pixels if difficulty > 0.5.
 */
public final class StarStyle implements StyleGenerator {

    /**
     * Generates a starburst-style map.
     *
     * <p><b>WARNING:</b> The thickness logic in this method
     * ({@code grid.set(r + 1, c, ...)} and {@code grid.set(r, c + 1, ...)})
     * does <b>NOT</b> perform bounds checking. This will cause an
     * {@code ArrayIndexOutOfBoundsException} if a ray runs along the
     * bottom ({@code r = rows - 1}) or right ({@code c = cols - 1}) edge
     * of the map.
     *
     * @param rows The number of rows for the map.
     * @param cols The number of columns for the map.
     * @param difficulty The difficulty (0.0 to 1.0). Controls:
     * <ul>
     * <li>The number of rays (5 to 8).</li>
     * <li>The hardness of the brick (Steel vs. Diamond).</li>
     * <li>The flawed thickness logic (triggers if > 0.5).</li>
     * </ul>
     * @param rng The {@link Random} generator to use.
     * @return The generated and sprinkled {@link Matrix}.
     */
    @Override
    public Matrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        Matrix grid = new Matrix(rows, cols);

        int rays = 5 + (int)Math.round(lerp(0, 3, difficulty));
        int cx = rows / 2, cy = cols / 2, len = Math.max(rows, cols);
        BrickType wall = TypePickers.pickFromTopHard(rng, 0.6 + 0.35 * difficulty);

        for (int i = 0; i < rays; i++) {
            double ang = (2 * Math.PI * i) / Math.max(1, rays) + rng.nextDouble() * 0.2;
            double dx = Math.cos(ang), dy = Math.sin(ang);
            for (int t = 0; t < len; t++) {
                int r = (int)Math.round(cx + dx * t), c = (int)Math.round(cy + dy * t);
                if (!inBounds(r, c, rows, cols)) break;
                grid.set(r, c, transTypeToNumber(wall));
                if (difficulty > 0.5) {
                    grid.set(r + 1, c, transTypeToNumber(wall));
                    grid.set(r, c + 1, transTypeToNumber(wall));
                }
            }
        }

        SpecialsSprinkler.sprinkle(grid, rng, difficulty);
        return grid;
    }
}
