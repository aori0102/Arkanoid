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

    /**
     * Base ratio 5:3:2 (Normal:Steel:Diamond).
     */
    private static final double BASE_N = 5.0;
    private static final double BASE_S = 3.0;
    private static final double BASE_D = 2.0;

    private static BrickType weightedPick(Random rng, double wN, double wS, double wD) {
        double sum = wN + wS + wD;
        if (sum <= 0.0) {
            // Degenerate: fall back to Normal
            return BrickType.Normal;
        }
        double r = rng.nextDouble() * sum;
        if (r < wN) return BrickType.Normal;
        r -= wN;
        if (r < wS) return BrickType.Steel;
        return BrickType.Diamond;
    }

    /**
     * Higher bias → favor harder (Steel/Diamond) more often.
     * Still anchored at base ratio 5:3:2, but we apply smooth multipliers.
     */
    public static BrickType pickByBias(Random rng, double bias) {
        double b = keep01(bias);

        double nMul = 1.20 - 0.60 * b;
        double sMul = 0.90 + 0.20 * b;
        double dMul = 0.80 + 0.60 * b;

        double wN = BASE_N * nMul;
        double wS = BASE_S * sMul;
        double wD = BASE_D * dMul;

        return weightedPick(rng, wN, wS, wD);
    }

    public static BrickType pickFromTopHard(Random rng, double topFrac) {
        double t = keep01(topFrac);

        double wN = 0.0;

        double sMul = 1.0 - 0.20 * t; // 1.00 → 0.80
        double dMul = 1.0 + 0.60 * t; // 1.00 → 1.60

        double wS = BASE_S * sMul;    // ≈ 3.0 → 2.4
        double wD = BASE_D * dMul;    // ≈ 2.0 → 3.2

        return weightedPick(rng, wN, wS, wD);
    }


    public static BrickType pickFromWeak(Random rng, double difficulty) {
        double d = keep01(difficulty);

        double nMul = 1.60 + 0.20 * (1.0 - d);
        double sMul = 0.70 + 0.20 * d;
        double dMul = 0.50 + 0.20 * d;

        double wN = BASE_N * nMul;
        double wS = BASE_S * sMul;
        double wD = BASE_D * dMul;

        return weightedPick(rng, wN, wS, wD);
    }
}
