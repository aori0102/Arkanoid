package game.Brick.BrickGenMap;

import game.Brick.BrickType;
import java.util.Random;

import static game.Brick.BrickGenMap.Mathx.*;

/**
 * A {@code final} utility class for selecting "core" {@link BrickType}s
 * (Normal, Steel, Diamond).
 *
 * <p>This class provides various weighted-random-selection algorithms
 * to pick a brick type based on parameters like 'bias' or 'difficulty'.
 * It is not meant to be instantiated.
 */
public final class TypePickers {
    private TypePickers() {}

    /**
     * Defines the set of core, non-special brick types.
     */
    private static final BrickType[] CORE = {
            BrickType.Normal,
            BrickType.Steel,
            BrickType.Diamond
    };

    /**
     * Base weight for Normal bricks in the 5:3:2 ratio.
     */
    private static final double BASE_N = 5.0;
    /**
     * Base weight for Steel bricks in the 5:3:2 ratio.
     */
    private static final double BASE_S = 3.0;
    /**
     * Base weight for Diamond bricks in the 5:3:2 ratio.
     */
    private static final double BASE_D = 2.0;

    /**
     * Selects a {@link BrickType} from the core set based on custom weights.
     * <p>
     * Implements a standard weighted random pick. If the sum of all weights
     * is zero or negative, it defaults to {@link BrickType#Normal}.
     *
     * @param rng The {@link Random} generator to use.
     * @param wN The weight for {@link BrickType#Normal}.
     * @param wS The weight for {@link BrickType#Steel}.
     * @param wD The weight for {@link BrickType#Diamond}.
     * @return The randomly selected {@link BrickType}.
     */
    private static BrickType weightedPick(Random rng, double wN, double wS, double wD) {
        double sum = wN + wS + wD;
        if (sum <= 0.0) {
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
