package game.Brick.BrickGenMap.style;

import static game.Brick.BrickGenMap.Mathx.*;
import static game.Brick.BrickGenMap.TransTypeNumBer.transTypeToNumber;
import static game.Brick.Init.inBounds;

import game.Brick.Init.Matrix;
import game.Brick.BrickGenMap.SpecialsSprinkler;
import game.Brick.BrickGenMap.StyleGenerator;
import game.Brick.BrickGenMap.TypePickers;
import java.util.Random;

/** DUNGEON: multiple rooms connected by doors. */
public final class DungeonStyle implements StyleGenerator {
    private static final class Room { final int r0,c0,h,w; Room(int r0,int c0,int h,int w){this.r0=r0;this.c0=c0;this.h=h;this.w=w;} }

    @Override
    public Matrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        Matrix g = new Matrix(rows, cols);

        int rooms = 4 + (int)Math.round(lerp(1, 4, difficulty));
        Room[] rs = new Room[rooms];

        for (int k = 0; k < rooms; k++) {
            int h = 3 + rng.nextInt(Math.max(2, rows / 3));
            int w = 3 + rng.nextInt(Math.max(2, cols / 2));
            h = Math.min(h, rows); w = Math.min(w, cols);
            int r0 = rng.nextInt(Math.max(1, rows - h + 1));
            int c0 = rng.nextInt(Math.max(1, cols - w + 1));
//            drawRect(g, r0, c0, h, w, TypePickers.pickFromTopHard(rng, 0.55 + 0.45 * difficulty), true);
//            if (rng.nextDouble() < 0.6) fillRect(g, r0+1, c0+1, Math.max(0,h-2), Math.max(0,w-2), TypePickers.pickByBias(rng, 0.25 + 0.5 * difficulty));
            rs[k] = new Room(r0,c0,h,w);
        }

        int maxDoorsPerRoom = (int)Math.max(1, Math.round(lerp(3, 1, difficulty)));
        int doorW = (int)Math.max(1, Math.round(lerp(2, 1, difficulty)));

        for (int i = 0; i < rs.length; i++) {
            int created = 0;
            for (int j = 0; j < rs.length && created < maxDoorsPerRoom; j++) {
                if (i == j) continue;
                Room a = rs[i], b = rs[j];
                if (!nearby(a,b)) continue;

                if (rng.nextBoolean()) {
                    int row = clampI(a.r0 + a.h / 2, 0, rows - 1);
                    int cs = Math.min(a.c0 + a.w, b.c0 + b.w);
                    int ce = Math.max(a.c0, b.c0);
                    if (cs > ce) { int t = cs; cs = ce; ce = t; }
                    carveH(g, row, cs, ce, doorW, difficulty, rng);
                } else {
                    int col = clampI(a.c0 + a.w / 2, 0, cols - 1);
                    int rs0 = Math.min(a.r0 + a.h, b.r0 + b.h);
                    int re0 = Math.max(a.r0, b.r0);
                    if (rs0 > re0) { int t = rs0; rs0 = re0; re0 = t; }
                    carveV(g, col, rs0, re0, doorW, difficulty, rng);
                }
                created++;
            }
        }

        SpecialsSprinkler.sprinkle(g, rng, difficulty);
        return g;
    }

    private static boolean nearby(Room a, Room b) {
        return !(a.r0 + a.h < b.r0 || b.r0 + b.h < a.r0 || a.c0 + a.w < b.c0 || b.c0 + b.w < a.c0);
    }

    private static void carveH(Matrix g, int row, int cStart, int cEnd, int doorW, double diff, Random rng) {
        int mid = (cStart + cEnd) / 2;
        for (int dc = -doorW / 2; dc <= doorW / 2; dc++) {
            int cc = mid + dc;
            if (inBounds(row, cc, g.rows(), g.columns()))
                g.set(row, cc, transTypeToNumber(TypePickers.pickFromWeak(rng, diff)));
        }
    }
    private static void carveV(Matrix g, int col, int rStart, int rEnd, int doorW, double diff, Random rng) {
        int mid = (rStart + rEnd) / 2;
        for (int dr = -doorW / 2; dr <= doorW / 2; dr++) {
            int rr = mid + dr;
            if (inBounds(rr, col, g.rows(), g.columns()))
                g.set(rr, col, transTypeToNumber(TypePickers.pickFromWeak(rng, diff)));
        }
    }
}
