package game.Brick.BrickGenMap.style;

import static game.Brick.BrickGenMap.Mathx.*;
import static game.Brick.BrickGenMap.TransTypeNumBer.transTypeToNumber;
import static game.Brick.Init.inBounds;

import game.Brick.BrickGenMap.SpecialsSprinkler;
import game.Brick.Init.Matrix;
import game.Brick.BrickType;
import game.Brick.BrickGenMap.StyleGenerator;
import java.util.Random;

public final class DungeonStyle implements StyleGenerator {
    private static final class Room {
        final int r0, c0, h, w;
        Room(int r0, int c0, int h, int w) { this.r0 = r0; this.c0 = c0; this.h = h; this.w = w; }
    }

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
