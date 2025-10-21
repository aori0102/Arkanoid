package game.Brick.BrickGenMap.style;

import static game.Brick.InitMatrix.*;
import static game.Brick.BrickGenMap.Mathx.*;
import static game.Brick.BrickGenMap.GridUtils.*;

import game.Brick.BrickType;
import game.Brick.InitMatrix.BrickMatrix;
import game.Brick.BrickGenMap.SpecialsSprinkler;
import game.Brick.BrickGenMap.StyleGenerator;
import game.Brick.BrickGenMap.TypePickers;
import java.util.Random;

/** STAR: multiple rays from center; thickness increases with difficulty. */
public final class StarStyle implements StyleGenerator {
    @Override
    public BrickMatrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        BrickMatrix g = new BrickMatrix(rows, cols);
        fillAll(g, BrickType.Normal);

        int rays = 5 + (int)Math.round(lerp(0, 3, difficulty));
        int cx = rows / 2, cy = cols / 2, len = Math.max(rows, cols);
        BrickType wall = TypePickers.pickFromTopHard(rng, 0.6 + 0.35 * difficulty);

        for (int i = 0; i < rays; i++) {
            double ang = (2 * Math.PI * i) / Math.max(1, rays) + rng.nextDouble() * 0.2;
            double dx = Math.cos(ang), dy = Math.sin(ang);
            for (int t = 0; t < len; t++) {
                int r = (int)Math.round(cx + dx * t), c = (int)Math.round(cy + dy * t);
                if (!inBounds(r, c, rows, cols)) break;
                g.set(r, c, getNewBrick(wall));
                if (difficulty > 0.5) { // thicken rays
                    setIfIn(g, r + 1, c, wall);
                    setIfIn(g, r, c + 1, wall);
                }
            }
        }

        SpecialsSprinkler.sprinkle(g, rng, difficulty);
        return g;
    }
}
