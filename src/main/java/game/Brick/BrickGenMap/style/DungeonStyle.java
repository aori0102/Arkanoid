package game.Brick.BrickGenMap.style;

import static game.Brick.BrickGenMap.Mathx.*;
import static game.Brick.BrickGenMap.TransTypeNumBer.transTypeToNumber;
import static game.Brick.Init.inBounds;

import game.Brick.BrickGenMap.SpecialsSprinkler;
import game.Brick.Init.Matrix;
import game.Brick.BrickType;
import game.Brick.BrickGenMap.StyleGenerator;
import java.util.Random;

/**
 * Generates a "dungeon" layout consisting of several rooms connected by corridors.
 * <p>
 * This generator follows several steps:
 * <ol>
 * <li><b>Create Border:</b> Fills the entire grid's outermost border with
 * 'Diamond' bricks (indestructible).</li>
 * <li><b>Fill Walls:</b> Fills the interior of the grid with 'Steel' bricks
 * to act as the base "rock" or "wall" material.</li>
 * <li><b>Carve Rooms:</b> Randomly generates 3 to 6 {@link Room} objects with
 * random sizes and positions. It carves these rooms out of the 'Steel'
 * interior by filling them with 'Normal' (breakable) bricks.</li>
 * <li><b>Connect Rooms:</b> Connects the rooms in sequence (room 0 to 1, 1 to 2, etc.)
 * by carving "L-shaped" corridors. It draws a vertical corridor from the
 * center of one room and a horizontal corridor from the center of the next,
 * creating a connection where they meet. These corridors are also filled
 * with 'Normal' bricks.</li>
 * <li><b>Add Vertical Shafts (Optional):</b> Iterates over the columns, and with a
 * 30% chance every 3 columns, it carves a vertical shaft of 'Normal' bricks
 * from the bottom-middle of the grid upwards, simulating extra paths or "stalactites."</li>
 * <li><b>Sprinkle Specials:</b> Finally, {@link SpecialsSprinkler} adds special bricks
 * to the 'Normal' brick areas (rooms and corridors).</li>
 * </ol>
 *
 * @see StyleGenerator
 * @see SpecialsSprinkler
 * @see Room
 */
public final class DungeonStyle implements StyleGenerator {
    private static final class Room {
        final int r0, c0, h, w;
        Room(int r0, int c0, int h, int w) { this.r0 = r0; this.c0 = c0; this.h = h; this.w = w; }
    }

    /**
     * Generates the dungeon-style brick layout.
     *
     * @param rows       The number of rows for the grid.
     * @param cols       The number of columns for the grid.
     * @param difficulty The difficulty level (0.0 to 1.0), used only by the
     * {@link SpecialsSprinkler}.
     * @param rng        The {@link Random} instance to use for all randomized
     * operations (room placement, size, connections, etc.).
     * @return A {@link Matrix} containing the integer representations
     * of the {@link BrickType}s, forming a dungeon layout.
     */
    @Override
    public Matrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        Matrix g = new Matrix(rows, cols);
        BrickType n = BrickType.Normal, s = BrickType.Steel, d = BrickType.Diamond;

        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (r == 0 || c == 0 || r == rows - 1 || c == cols - 1)
                    g.set(r, c, transTypeToNumber(d));
                else
                    g.set(r, c, transTypeToNumber(s));

        int rooms = 3 + rng.nextInt(4);
        Room[] rs = new Room[rooms];
        for (int i = 0; i < rooms; i++) {
            int h = 3 + rng.nextInt(Math.max(2, rows / 4));
            int w = 4 + rng.nextInt(Math.max(2, cols / 4));
            int r0 = 1 + rng.nextInt(rows - h - 1);
            int c0 = 1 + rng.nextInt(cols - w - 1);
            rs[i] = new Room(r0, c0, h, w);
            for (int r = r0; r < r0 + h; r++)
                for (int c = c0; c < c0 + w; c++)
                    if (inBounds(r, c, rows, cols))
                        g.set(r, c, transTypeToNumber(n));
        }

        for (int i = 0; i < rooms - 1; i++) {
            Room a = rs[i], b = rs[i + 1];
            int ra = a.r0 + a.h / 2, ca = a.c0 + a.w / 2;
            int rb = b.r0 + b.h / 2, cb = b.c0 + b.w / 2;
            for (int r = Math.min(ra, rb); r <= Math.max(ra, rb); r++)
                if (inBounds(r, ca, rows, cols))
                    g.set(r, ca, transTypeToNumber(n));
            for (int c = Math.min(ca, cb); c <= Math.max(ca, cb); c++)
                if (inBounds(rb, c, rows, cols))
                    g.set(rb, c, transTypeToNumber(n));
        }

        for (int i = 0; i < cols; i += 3)
            if (rng.nextDouble() < 0.3)
                for (int r = rows - 2; r > rows / 2; r--)
                    if (g.get(r, i) != transTypeToNumber(d))
                        g.set(r, i, transTypeToNumber(n));

        SpecialsSprinkler.sprinkle(g, rng, difficulty);
        return g;
    }
}
