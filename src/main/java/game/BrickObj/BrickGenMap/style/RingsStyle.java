package game.BrickObj.BrickGenMap.style;

import static game.BrickObj.BrickGenMap.Mathx.*;
import static game.BrickObj.BrickGenMap.GridUtils.*;

import game.BrickObj.BrickType;
import game.BrickObj.InitMatrix.BrickMatrix;
import game.BrickObj.BrickGenMap.SpecialsSprinkler;
import game.BrickObj.BrickGenMap.StyleGenerator;
import game.BrickObj.BrickGenMap.TypePickers;
import java.util.Random;

/** RINGS: concentric rectangular rings with occasional doorways. */
public final class RingsStyle implements StyleGenerator {
    @Override
    public BrickMatrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        BrickMatrix g = new BrickMatrix(rows, cols);
        fillAll(g, BrickType.Normal);
        int layers = Math.min(rows, cols) / 2;
        for (int d = 0; d < layers; d += 2) {
            int r0 = d, c0 = d, h = rows - 2 * d, w = cols - 2 * d;
            if (h <= 1 || w <= 1) break;
            double t = (double) d / Math.max(1, layers - 1);
            BrickType wall = TypePickers.pickByBias(rng, keep01(0.35 + (1.0 - t) * (0.55 * difficulty + 0.15)));
            drawRect(g, r0, c0, h, w, wall, true);

            // open a small doorway per ring (harder → rarer)
            if (rng.nextDouble() < lerp(0.8, 0.3, difficulty)) {
                int side = rng.nextInt(4);
                switch (side) {
                    case 0 -> setIfIn(g, r0, c0 + rng.nextInt(w), TypePickers.pickFromWeak(rng, difficulty));
                    case 1 -> setIfIn(g, r0 + h - 1, c0 + rng.nextInt(w), TypePickers.pickFromWeak(rng, difficulty));
                    case 2 -> setIfIn(g, r0 + rng.nextInt(h), c0, TypePickers.pickFromWeak(rng, difficulty));
                    default-> setIfIn(g, r0 + rng.nextInt(h), c0 + w - 1, TypePickers.pickFromWeak(rng, difficulty));
                }
            }
        }
        SpecialsSprinkler.sprinkle(g, rng, difficulty);
        return g;
    }
}
