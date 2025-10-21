package game.Brick.BrickGenMap;

import game.Brick.BrickType;
import java.util.Random;

import static game.Brick.BrickGenMap.Mathx.*;

public final class TypePickers {
    private TypePickers() {}

    private static final BrickType[] CORE = {
            BrickType.Normal,
            BrickType.Steel,
            BrickType.Diamond
    };

    public static BrickType pickByBias(Random rng, double bias) {
        int maxIx = (int) Math.round(keep01(bias) * (CORE.length - 1));
        return CORE[rng.nextInt(maxIx + 1)];
    }

    public static BrickType pickFromTopHard(Random rng, double topFrac) {
        topFrac = keep01(topFrac);
        int start = (int) Math.floor((1.0 - topFrac) * CORE.length);
        if (start >= CORE.length) start = CORE.length - 1;
        return CORE[start + rng.nextInt(CORE.length - start)];
    }

    public static BrickType pickFromWeak(Random rng, double difficulty) {
         double p = 0.1 * keep01(difficulty);
         return rng.nextDouble() < p ? BrickType.Steel : BrickType.Normal;
    }
}
