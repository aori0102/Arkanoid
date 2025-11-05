package game.Brick.BrickGenMap.style;

import static game.Brick.BrickGenMap.TransTypeNumBer.transTypeToNumber;
import static game.Brick.Init.*;
import static game.Brick.BrickGenMap.Mathx.*;

import game.Brick.BrickType;
import game.Brick.BrickGenMap.SpecialsSprinkler;
import game.Brick.BrickGenMap.StyleGenerator;
import game.Brick.BrickGenMap.TypePickers;
import java.util.Random;

/** STAR: multiple rays from center; thickness increases with difficulty. */
public final class StarStyle implements StyleGenerator {
    @Override
    public Matrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        Matrix g = new Matrix(rows, cols);

        int rays = 5 + (int)Math.round(lerp(0, 3, difficulty));
        int cx = rows / 2, cy = cols / 2, len = Math.max(rows, cols);
        BrickType wall = TypePickers.pickFromTopHard(rng, 0.6 + 0.35 * difficulty);

        for (int i = 0; i < rays; i++) {
            double ang = (2 * Math.PI * i) / Math.max(1, rays) + rng.nextDouble() * 0.2;
            double dx = Math.cos(ang), dy = Math.sin(ang);
            for (int t = 0; t < len; t++) {
                int r = (int)Math.round(cx + dx * t), c = (int)Math.round(cy + dy * t);
                if (!inBounds(r, c, rows, cols)) break;
                g.set(r, c, transTypeToNumber(wall));
                if (difficulty > 0.5) {
                    g.set(r + 1, c, transTypeToNumber(wall));
                    g.set(r, c + 1, transTypeToNumber(wall));
                }
            }
        }

        SpecialsSprinkler.sprinkle(g, rng, difficulty);
        return g;
    }
}
