package game.Brick.BrickGenMap;

import static game.Brick.BrickGenMap.TransTypeNumBer.transNumberToType;
import static game.Brick.BrickGenMap.TransTypeNumBer.transTypeToNumber;
import static game.Brick.BrickGenMap.Mathx.*;

import game.Brick.BrickType;
import game.Brick.Init.Matrix;
import java.util.Random;

/**
 * A {@code final} utility class responsible for "sprinkling" special bricks
 * (like Bomb, Gift, etc.) onto a pre-generated map {@link Matrix}.
 *
 * <p>It iterates over an existing map and replaces non-special, non-indestructible
 * bricks (e.g., Normal) with special bricks. This is based on a set of
 * probabilities that are influenced by a {@code difficulty} parameter.
 *
 * <p>It enforces maximum limits for each special brick type using an
 * internal {@link Counters} class to prevent over-population.
 *
 * <p>This class is not meant to be instantiated.
 */
public final class SpecialsSprinkler {

    private static final double BASE_RATIO_BOMB = 0.3;
    private static final double BASE_RATIO_ROCK = 0.2;
    private static final double BASE_RATIO_GIFT = 0.2;
    private static final double BASE_RATIO_REBORN = 0.1;
    private static final double BASE_RATIO_OTHER = 0.3;

    private SpecialsSprinkler() {}

    private static final class Counters {
        final int maxBomb, maxRock, maxReborn, maxGift, maxOtherEach;
        int bomb, rock, reborn, gift, rocket, angel;

        Counters(int maxBomb, int maxRock, int maxReborn, int maxGift, int maxOtherEach) {
            this.maxBomb = maxBomb;
            this.maxRock = maxRock;
            this.maxReborn = maxReborn;
            this.maxGift = maxGift;
            this.maxOtherEach = maxOtherEach;
        }

        void inc(BrickType t) {
            if (t == null) return;
            switch (t) {
                case Bomb   -> bomb++;
                case Rock   -> rock++;
                case Wheel -> reborn++;
                case Gift   -> gift++;
                case Rocket -> rocket++;
                case Angel  -> angel++;
                default     -> {}
            }
        }

        void dec(BrickType t) {
            if (t == null) return;
            switch (t) {
                case Bomb   -> bomb   = Math.max(0, bomb   - 1);
                case Rock   -> rock   = Math.max(0, rock   - 1);
                case Wheel -> reborn = Math.max(0, reborn - 1);
                case Gift   -> gift   = Math.max(0, gift   - 1);
                case Rocket -> rocket = Math.max(0, rocket - 1);
                case Angel  -> angel  = Math.max(0, angel  - 1);
                default     -> {}
            }
        }

        void applyReplaceDelta(BrickType oldT, BrickType newT) {
            if (oldT != newT) {
                dec(oldT);
                inc(newT);
            }
        }
        boolean canPlace(BrickType t) {
            return switch (t) {
                case Bomb   -> bomb   < maxBomb;
                case Rock   -> rock   < maxRock;
                case Wheel -> reborn < maxReborn;
                case Gift   -> gift   < maxGift;
                case Rocket -> rocket < maxOtherEach;
                case Angel  -> angel  < maxOtherEach;
                default     -> true;
            };
        }
        boolean allReached() {
            return bomb   >= maxBomb
                    && rock   >= maxRock
                    && reborn >= maxReborn
                    && gift   >= maxGift
                    && rocket >= maxOtherEach
                    && angel  >= maxOtherEach;
        }
    }

    /**
     * The main static method that sprinkles special bricks onto a given {@link Matrix}.
     *
     * <p>It first calculates the probabilities for each special brick based on the
     * {@code difficulty}. It then performs two passes:
     * <ol>
     * <li><b>Pre-count Pass:</b> Iterates over the entire grid to count any special
     * bricks that may already exist (e.g., placed by a StyleGenerator).</li>
     * <li><b>Sprinkle Pass:</b> Iterates again, replacing eligible bricks
     * (non-Steel, non-Diamond, non-null) with special bricks based on random rolls
     * and probability, until all maximums are met.</li>
     * </ol>
     *
     * <p><b>Note:</b> The iteration is sequential (top-to-bottom). If the
     * {@code allReached()} bug were fixed, special bricks might cluster
     * in the upper portion of the map.
     *
     * @param g The {@link Matrix} (map) to be modified in-place.
     * @param rng The {@link Random} generator to use for all probabilistic checks.
     * @param difficulty The difficulty value (e.g., 0.0 to 1.0) which modifies the
     * spawn probabilities. Higher difficulty generally *decreases* Bomb/Rock
     * spawns and *increases* Gift/Wheel spawns.
     */
    public static void sprinkle(Matrix g, Random rng, double difficulty) {
        if (g == null || rng == null) return;

        final double bombP = keep01(BASE_RATIO_BOMB - 6 * difficulty);
        final double rockP = keep01(BASE_RATIO_ROCK - 6 * difficulty);

        final double rebornP = keep01(BASE_RATIO_REBORN + 4 * difficulty);
        final double giftP   = keep01(BASE_RATIO_GIFT + 5 * difficulty);
        final double otherP  = keep01(BASE_RATIO_OTHER + 3 * difficulty);

        final Counters cnt = new Counters(
                /*maxBomb*/3,
                /*maxRock*/2,
                /*maxWheel*/2,
                /*maxGift*/5,
                /*maxOtherEach*/4
        );


        for (int r = 0; r < g.rows(); r++) {
            for (int c = 0; c < g.columns(); c++) {
                BrickType tmp = transNumberToType(g.get(r, c));
                if(tmp != null) cnt.inc(tmp);
            }
        }


        outer:
        for (int r = 0; r < g.rows(); r++) {
            for (int c = 0; c < g.columns(); c++) {
                if (cnt.allReached()) break outer;

                BrickType current = transNumberToType(g.get(r, c));
                if (current == null) continue;

                if (current == BrickType.Steel || current == BrickType.Diamond) continue;

                double x = rng.nextDouble();

                if (x < bombP) {
                    if (current != BrickType.Bomb && cnt.canPlace(BrickType.Bomb)) {
                        g.set(r, c, transTypeToNumber(BrickType.Bomb));
                        cnt.applyReplaceDelta(current, BrickType.Bomb);
                    }
                    continue;
                }

                if (x < bombP + rockP) {
                    if (current != BrickType.Rock && cnt.canPlace(BrickType.Rock)) {
                        g.set(r, c, transTypeToNumber(BrickType.Rock));
                        cnt.applyReplaceDelta(current, BrickType.Rock);
                    }
                    continue;
                }

                if (x < bombP + rockP + rebornP) {
                    if (current != BrickType.Wheel && cnt.canPlace(BrickType.Wheel)) {
                        g.set(r, c, transTypeToNumber(BrickType.Wheel));
                        cnt.applyReplaceDelta(current, BrickType.Wheel);
                    }
                    continue;
                }

                if (x < bombP + rockP + rebornP + giftP) {
                    if (current != BrickType.Gift && cnt.canPlace(BrickType.Gift)) {
                        g.set(r, c,  transTypeToNumber(BrickType.Gift));
                        cnt.applyReplaceDelta(current, BrickType.Gift);
                    }
                    continue;
                }

                if (x < bombP + rockP + rebornP + giftP + otherP) {
                    BrickType[] pool = { BrickType.Rocket, BrickType.Angel};
                    int start = rng.nextInt(pool.length);
                    for (int i = 0; i < pool.length; i++) {
                        BrickType pick = pool[(start + i) % pool.length];
                        if (current == pick) break;
                        if (!cnt.canPlace(pick)) continue;
                        g.set(r, c, transTypeToNumber(pick));
                        cnt.applyReplaceDelta(current, pick);
                        break;
                    }
                }
            }
        }
    }
}
