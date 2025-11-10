package game.Brick.BrickGenMap;

import game.Brick.BrickType;
import game.Brick.Init.*;

import java.util.*;

import static game.Brick.BrickGenMap.TransTypeNumBer.transNumberToType;
import static game.Brick.BrickGenMap.TransTypeNumBer.transTypeToNumber;
import static java.lang.Math.abs;

public class BalanceCondition {

    private final static int atLeastNumGate = 4;
    private final static int Max_Diamond = 15;

    public static void balanceCondition(Matrix g) {
        bottomNotFullDiamond(g);
        alwayHavePath(g);
        diamondBalance(g);
    }

    private static void diamondBalance(Matrix g) {
        final int rows = g.rows();
        final int cols = g.columns();

        Random rng = new Random();

        List<IntPair> diamondsPos = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                if (transNumberToType(g.get(r, c)) == BrickType.Diamond) {
                    diamondsPos.add(new IntPair(r, c));
                }

            }
        }

        if (diamondsPos.isEmpty()) return;

        int maxDiamond = Max_Diamond;
        Collections.shuffle(diamondsPos, rng);
        Set<IntPair> keep = new HashSet<>(diamondsPos.subList(0, Math.min(maxDiamond, diamondsPos.size())));
        List<IntPair> needToChange = new ArrayList<>();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                BrickType cur = transNumberToType(g.get(r, c));
                if (cur != BrickType.Diamond) continue;

                IntPair cell = new IntPair(r, c);
                if (!keep.contains(cell)) {
                    needToChange.add(new  IntPair(r, c));
                }
            }
        }

        Collections.shuffle(needToChange, rng);
        int lengthForNormal = (int) (needToChange.size() * 0.6);
        int lengthForSteel =  (int) (needToChange.size() * 0.3);

        for (int i = 0; i < lengthForNormal; i++) {
            IntPair cell = needToChange.get(i);
            int r = cell.fi();
            int c = cell.se();
            g.set(r, c, transTypeToNumber(BrickType.Normal));
        }

        for (int i = lengthForNormal; i < lengthForSteel + lengthForNormal; i++) {
            IntPair cell = needToChange.get(i);
            int r = cell.fi();
            int c = cell.se();
            g.set(r, c, transTypeToNumber(BrickType.Steel));
        }

        for (int i = lengthForNormal + lengthForSteel; i < needToChange.size(); i++) {
            IntPair cell = needToChange.get(i);
            int r = cell.fi();
            int c = cell.se();

            int randType = abs(rng.nextInt() % 6) + 3;
            g.set(r, c, randType);
        }
    }

    public static void alwayHavePath(Matrix g) {
        final int rows = g.rows();
        final int cols = g.columns();
        final Random rng = new Random();

        int[][] comp = new int[rows][cols];
        for (int[] row : comp) Arrays.fill(row, -1);

        int compCount = 0;
        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (comp[r][c] != -1) continue;
                if (transNumberToType(g.get(r, c)) == BrickType.Diamond) continue;

                Queue<IntPair> q = new LinkedList<>();
                q.add(new IntPair(r, c));
                comp[r][c] = compCount;

                while (!q.isEmpty()) {
                    IntPair p = q.poll();
                    for (int k = 0; k < 4; k++) {
                        int nr = p.fi() + dx[k];
                        int nc = p.se() + dy[k];
                        if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) continue;
                        if (comp[nr][nc] != -1) continue;
                        if (transNumberToType(g.get(nr, nc)) == BrickType.Diamond) continue;
                        comp[nr][nc] = compCount;
                        q.add(new IntPair(nr, nc));
                    }
                }
                compCount++;
            }
        }

        if (compCount <= 1) return;

        List<List<IntPair>> regions = new ArrayList<>();
        for (int i = 0; i < compCount; i++) regions.add(new ArrayList<>());

        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (comp[r][c] != -1)
                    regions.get(comp[r][c]).add(new IntPair(r, c));

        for (int i = 0; i < compCount - 1; i++) {
            List<IntPair> regionA = regions.get(i);
            List<IntPair> regionB = regions.get(i + 1);

            IntPair start = regionA.get(rng.nextInt(regionA.size()));
            IntPair end = regionB.get(rng.nextInt(regionB.size()));

            Map<IntPair, IntPair> parent = new HashMap<>();
            Queue<IntPair> q = new LinkedList<>();
            q.add(start);
            parent.put(start, null);

            boolean found = false;
            while (!q.isEmpty()) {
                IntPair cur = q.poll();
                if (cur.equals(end)) {
                    found = true;
                    break;
                }
                for (int k = 0; k < 4; k++) {
                    int nr = cur.fi() + dx[k];
                    int nc = cur.se() + dy[k];
                    if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) continue;
                    IntPair nxt = new IntPair(nr, nc);
                    if (parent.containsKey(nxt)) continue;
                    parent.put(nxt, cur);
                    q.add(nxt);
                }
            }

            if (!found) continue;
            List<IntPair> path = new ArrayList<>();
            for (IntPair p = end; p != null; p = parent.get(p)) path.add(p);

            for (IntPair p : path) {
                int r = p.fi(), c = p.se();
                BrickType cur = transNumberToType(g.get(r, c));
                if (cur == BrickType.Diamond) {
                    double x = rng.nextDouble();
                    if (x < 0.6) {
                        g.set(r, c, transTypeToNumber(BrickType.Normal));
                    } else if (x < 0.9) {
                        g.set(r, c, transTypeToNumber(BrickType.Steel));
                    } else {
                        BrickType[] pool = {
                                BrickType.Rock, BrickType.Bomb,
                                BrickType.Gift, BrickType.Wheel,
                                BrickType.Rocket, BrickType.Angel
                        };
                        BrickType pick = pool[rng.nextInt(pool.length)];
                        g.set(r, c, transTypeToNumber(pick));
                    }
                }
            }
        }
    }

    public static void bottomNotFullDiamond(Matrix g) {
        int rows = g.rows();
        int cols = g.columns();

        int num = 0;
        for (int col = 0; col < cols; col++) {
            if (g.get(rows - 1, col) != transTypeToNumber(BrickType.Diamond)) {
                num++;
            }
        }

        if (num < atLeastNumGate) {
            List<Integer> rngList = new ArrayList<>();
            for (int col = 0; col < cols; col++) {
                rngList.add(col);
            }

            Collections.shuffle(rngList);

            for (int i = 0; i < atLeastNumGate - num; i++) {
                int row = rows - 1;
                int col = rngList.get(i);

                g.set(row, col, transTypeToNumber(BrickType.Normal));
            }
        }
    }
}
