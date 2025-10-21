package game.Brick.BrickGenMap;

import static game.Brick.InitMatrix.getNewBrick;
import static game.Brick.BrickGenMap.Mathx.*;

import game.Brick.Brick;
import game.Brick.BrickType;
import game.Brick.InitMatrix.BrickMatrix;
import java.util.Random;

public final class SpecialsSprinkler {
    private SpecialsSprinkler() {}

    private static final class Counters {
        final int maxBomb, maxRock, maxReborn, maxGift, maxOtherEach;
        int bomb, rock, reborn, gift, rocket, angel, ball;

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
                case Reborn -> reborn++;
                case Gift   -> gift++;
                case Rocket -> rocket++;
                case Angel  -> angel++;
                case Ball   -> ball++;
                default     -> {}
            }
        }
        void dec(BrickType t) {
            if (t == null) return;
            switch (t) {
                case Bomb   -> bomb   = Math.max(0, bomb   - 1);
                case Rock   -> rock   = Math.max(0, rock   - 1);
                case Reborn -> reborn = Math.max(0, reborn - 1);
                case Gift   -> gift   = Math.max(0, gift   - 1);
                case Rocket -> rocket = Math.max(0, rocket - 1);
                case Angel  -> angel  = Math.max(0, angel  - 1);
                case Ball   -> ball   = Math.max(0, ball   - 1);
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
                case Reborn -> reborn < maxReborn;
                case Gift   -> gift   < maxGift;
                case Rocket -> rocket < maxOtherEach;
                case Angel  -> angel  < maxOtherEach;
                case Ball   -> ball   < maxOtherEach;
                default     -> true;
            };
        }
        boolean allReached() {
            return bomb   >= maxBomb
                    && rock   >= maxRock
                    && reborn >= maxReborn
                    && gift   >= maxGift
                    && rocket >= maxOtherEach
                    && angel  >= maxOtherEach
                    && ball   >= maxOtherEach;
        }
    }

    public static void openDimondGate(BrickMatrix matrix, Random rng) {
        final int row = matrix.rows();
        final int col = matrix.columns();

        boolean[][] isDimond  = new boolean[row][col];
        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++) {
                if(matrix.getObjType(r, c) == BrickType.Diamond) {
                    isDimond[r][c] = true;
                }
            }
        }
    }

    public static void sprinkle(BrickMatrix g, Random rng, double difficulty) {
        if (g == null || rng == null) return;

        final double bombP = keep01(0.2 - 6 * difficulty);
        final double rockP = keep01(0.1 - 6 * difficulty);

        final double rebornP = keep01(0.1 + 4 * difficulty);
        final double giftP   = keep01(0.2 + 5 * difficulty);
        final double otherP  = keep01(0.3 + 3 * difficulty);

        final Counters cnt = new Counters(
                /*maxBomb*/3,
                /*maxRock*/3,
                /*maxReborn*/4,
                /*maxGift*/5,
                /*maxOtherEach*/6
        );


        for (int r = 0; r < g.rows(); r++) {
            for (int c = 0; c < g.columns(); c++) {
                Brick b = g.get(r, c);
                if (b != null) cnt.inc(b.getBrickType());
            }
        }


        outer:
        for (int r = 0; r < g.rows(); r++) {
            for (int c = 0; c < g.columns(); c++) {
                if (cnt.allReached()) break outer;

                Brick cur = g.get(r, c);
                if (cur == null) continue;
                BrickType current = cur.getBrickType();
                if (current == null) continue;

                if (current == BrickType.Steel || current == BrickType.Diamond) continue;

                double x = rng.nextDouble();

                if (x < bombP) {
                    if (current != BrickType.Bomb && cnt.canPlace(BrickType.Bomb)) {
                        g.set(r, c, getNewBrick(BrickType.Bomb));
                        cnt.applyReplaceDelta(current, BrickType.Bomb);
                    }
                    continue;
                }

                if (x < bombP + rockP) {
                    if (current != BrickType.Rock && cnt.canPlace(BrickType.Rock)) {
                        g.set(r, c, getNewBrick(BrickType.Rock));
                        cnt.applyReplaceDelta(current, BrickType.Rock);
                    }
                    continue;
                }

                if (x < bombP + rockP + rebornP) {
                    if (current != BrickType.Reborn && cnt.canPlace(BrickType.Reborn)) {
                        g.set(r, c, getNewBrick(BrickType.Reborn));
                        cnt.applyReplaceDelta(current, BrickType.Reborn);
                    }
                    continue;
                }

                if (x < bombP + rockP + rebornP + giftP) {
                    if (current != BrickType.Gift && cnt.canPlace(BrickType.Gift)) {
                        g.set(r, c, getNewBrick(BrickType.Gift));
                        cnt.applyReplaceDelta(current, BrickType.Gift);
                    }
                    continue;
                }

                if (x < bombP + rockP + rebornP + giftP + otherP) {
                    BrickType[] pool = { BrickType.Rocket, BrickType.Angel, BrickType.Ball };
                    int start = rng.nextInt(pool.length);
                    for (int i = 0; i < pool.length; i++) {
                        BrickType pick = pool[(start + i) % pool.length];
                        if (current == pick) break;
                        if (!cnt.canPlace(pick)) continue;
                        g.set(r, c, getNewBrick(pick));
                        cnt.applyReplaceDelta(current, pick);
                        break;
                    }
                }
            }
        }

        openDimondGate(g, rng);
    }
}
