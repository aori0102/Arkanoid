package game.Brick.BrickGenMap.balanceRule.ConcreteHandlers;

import game.Brick.BrickGenMap.balanceRule.BalanceHandler;
import game.Brick.BrickType;
import game.Brick.Init;

import java.util.*;

import static game.Brick.BrickGenMap.TransTypeNumBer.transNumberToType;
import static game.Brick.BrickGenMap.TransTypeNumBer.transTypeToNumber;

/**
 * A Balance Handler that ensures the map is playable by guaranteeing that all
 * non-diamond brick regions are path-connected.
 *
 * <p>This rule works by:
 * <ol>
 * <li>Scanning the map to find all isolated "connected components" (regions) of non-diamond bricks.</li>
 * <li>If more than one region is found, it iteratively connects adjacent regions (region {@code i} to {@code i+1}).</li>
 * <li>It finds a path (using BFS) from a random cell in one region to a random cell in the next.</li>
 * <li>It then "carves" this path through any {@link BrickType#Diamond} bricks that are in the way.</li>
 * <li>Carved-out diamonds are replaced by Normal, Steel, or other special bricks based on a probability.</li>
 * </ol>
 *
 * Note: This handler creates its own {@code new Random()} instance, meaning its
 * behavior will be different every time, even if the generator uses a fixed seed.
 */
public class AlwayHavePathRule extends BalanceHandler {

    /**
     * Applies the path-finding and connectivity rule to the matrix.
     *
     * @param g The game matrix to be modified in-place.
     */
    @Override
    protected void applyRule(Init.Matrix g) {
        final int rows = g.rows();
        final int cols = g.columns();
        final Random rng = new Random();

        // 1. Find all Connected Components (non-Diamond bricks)
        Init.Matrix comp = new Init.Matrix(rows, cols, -1);

        int compCount = 0;
        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};

        // Use BFS to find all comp
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (comp.get(r, c) != -1) continue;
                if (transNumberToType(g.get(r, c)) == BrickType.Diamond) continue;

                Queue<Init.IntPair> q = new LinkedList<>();
                q.add(new Init.IntPair(r, c));
                comp.set(r, c, compCount);

                while (!q.isEmpty()) {
                    Init.IntPair p = q.poll();
                    for (int k = 0; k < 4; k++) {
                        int nr = p.fi() + dx[k];
                        int nc = p.se() + dy[k];
                        if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) continue;
                        if (comp.get(nr, nc) != -1) continue;
                        if (transNumberToType(g.get(nr, nc)) == BrickType.Diamond) continue;
                        comp.set(nr, nc, compCount);
                        q.add(new Init.IntPair(nr, nc));
                    }
                }
                compCount++;
            }
        }

        // If 0 or 1 component, map is already connected.
        if (compCount <= 1) return;

        // 2. Store all cells belonging to each component
        List<List<Init.IntPair>> regions = new ArrayList<>();
        for (int i = 0; i < compCount; i++) regions.add(new ArrayList<>());

        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (comp.get(r, c) != -1)
                    regions.get(comp.get(r, c)).add(new Init.IntPair(r, c));

        // 3. Connect adjacent components (region i -> region i+1)
        for (int i = 0; i < compCount - 1; i++) {
            List<Init.IntPair> regionA = regions.get(i);
            List<Init.IntPair> regionB = regions.get(i + 1);

            // Choose 2 position randomly to find a path for connect 2 components
            Init.IntPair start = regionA.get(rng.nextInt(regionA.size()));
            Init.IntPair end = regionB.get(rng.nextInt(regionB.size()));

            // 4. Find path from start to end
            Map<Init.IntPair, Init.IntPair> parent = new HashMap<>();
            Queue<Init.IntPair> q = new LinkedList<>();
            q.add(start);
            parent.put(start, null);

            boolean found = false;
            while (!q.isEmpty()) {
                Init.IntPair cur = q.poll();
                if (cur.equals(end)) {
                    found = true;
                    break;
                }
                for (int k = 0; k < 4; k++) {
                    int nr = cur.fi() + dx[k];
                    int nc = cur.se() + dy[k];
                    if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) continue;
                    Init.IntPair nxt = new Init.IntPair(nr, nc);
                    if (parent.containsKey(nxt)) continue;
                    parent.put(nxt, cur);
                    q.add(nxt);
                }
            }

            // 5. If path found, reconstruct it and "carve" a tunnel
            if (!found) continue;
            List<Init.IntPair> path = new ArrayList<>();
            for (Init.IntPair p = end; p != null; p = parent.get(p)) path.add(p);

            // Carve the path by random bricks
            for (Init.IntPair p : path) {
                int r = p.fi(), c = p.se();
                BrickType cur = transNumberToType(g.get(r, c));
                if (cur == BrickType.Diamond) {
                    double x = rng.nextDouble();
                    if (x < RATIO_NORMAL) {
                        g.set(r, c, transTypeToNumber(BrickType.Normal));
                    } else if (x < RATIO_STEEL + RATIO_NORMAL) {
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
}
