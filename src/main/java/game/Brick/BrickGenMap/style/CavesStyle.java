package game.Brick.BrickGenMap.style;

import static game.Brick.BrickGenMap.TransTypeNumBer.transTypeToNumber;
import static game.Brick.BrickGenMap.Mathx.*;
import static game.Brick.Init.*;

import game.Brick.BrickType;
import game.Brick.BrickGenMap.SpecialsSprinkler;
import game.Brick.BrickGenMap.StyleGenerator;
import java.util.*;

/**
 * A {@link StyleGenerator} that creates a map resembling a series of
 * natural caves or caverns.
 *
 * <p>This generator uses several procedural generation techniques:
 * <ol>
 * <li>Generates 2D pseudo-Perlin noise (using sin/cos).</li>
 * <li>Applies a threshold to the noise to create an initial "wall" map.</li>
 * <li>Uses a <b>Cellular Automata</b> "smoothing" algorithm to make the
 * wall formations more natural and "blob-like".</li>
 * <li>Applies a <b>Flood Fill</b> algorithm to find all disconnected
 * open regions (caves).</li>
 * <li>Keeps only the <b>largest</b> open region, filling in all smaller,
 * disconnected caves.</li>
 * <li>Carves several random vertical paths to improve connectivity.</li>
 * <li>Converts the final boolean wall map into a {@link Matrix} of bricks.</li>
 * <li>Finally, {@link SpecialsSprinkler} is called to add special bricks.</li>
 * </ol>
 */
public final class CavesStyle implements StyleGenerator {

    @Override
    public Matrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        double[][] noise = generatePerlinNoise(rows, cols, 0.1, rng);
        boolean[][] wall = new boolean[rows][cols];
        double threshold = lerp(0.45, 0.6, difficulty);

        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++) {
                wall[r][c] = noise[r][c] > threshold;
            }

        for (int step = 0; step < 3; step++) {
            wall = smooth(wall);
        }

        keepLargestOpenRegion(wall, rows, cols);
        addVerticalPaths(wall, rng, rows, cols);
        Matrix grid = new Matrix(rows, cols);

        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++) {
                if (r == 0 || r == rows - 1 || c == 0 || c == cols - 1) {
                    grid.set(r, c, transTypeToNumber(BrickType.Diamond));
                    continue;
                }
                if (wall[r][c]) {
                    if (r > rows * 0.7 && rng.nextDouble() < 0.3)
                        grid.set(r, c, transTypeToNumber(BrickType.Diamond));
                    else {
                        grid.set(r, c, transTypeToNumber(BrickType.Steel));
                    }
                } else {
                    grid.set(r, c, transTypeToNumber(BrickType.Normal));
                }
            }

        SpecialsSprinkler.sprinkle(grid, rng, difficulty);
        return grid;
    }

    private double[][] generatePerlinNoise(int rows, int cols, double scale, Random rng) {
        double[][] n = new double[rows][cols];
        double seedX = rng.nextDouble() * 5000, seedY = rng.nextDouble() * 5000;
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++) {
                double nx = seedX + r * scale, ny = seedY + c * scale;
                n[r][c] = 0.5 * (Math.sin(nx * 2.1) + Math.cos(ny * 1.7)) + 0.5 * Math.sin(nx + ny);
            }
        return n;
    }

    private boolean[][] smooth(boolean[][] w) {
        int rows = w.length, cols = w[0].length;
        boolean[][] next = new boolean[rows][cols];
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++) {
                int n = neighbors(w, r, c);
                next[r][c] = (w[r][c] && n >= 4) || (!w[r][c] && n >= 5);
            }
        return next;
    }

    private int neighbors(boolean[][] w, int r, int c) {
        int rows = w.length, cols = w[0].length, cnt = 0;
        for (int dr = -1; dr <= 1; dr++)
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                int nr = r + dr, nc = c + dc;
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) continue;
                if (w[nr][nc]) cnt++;
            }
        return cnt;
    }

    private void keepLargestOpenRegion(boolean[][] w, int rows, int cols) {
        boolean[][] visited = new boolean[rows][cols];
        List<List<IntPair>> regions = new ArrayList<>();
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (!w[r][c] && !visited[r][c])
                    regions.add(floodFillRegion(w, visited, r, c));
        if (regions.isEmpty()) return;
        List<IntPair> largest = regions.stream().max(Comparator.comparingInt(List::size)).orElse(List.of());
        for (List<IntPair> region : regions)
            if (region != largest)
                for (IntPair p : region) w[p.fi()][p.se()] = true;
    }

    private List<IntPair> floodFillRegion(boolean[][] w, boolean[][] visited, int r, int c) {
        List<IntPair> region = new ArrayList<>();
        int[] dx = {1, -1, 0, 0}, dy = {0, 0, 1, -1};
        Queue<IntPair> q = new LinkedList<>();
        q.add(new IntPair(r, c));
        visited[r][c] = true;
        while (!q.isEmpty()) {
            IntPair p = q.poll();
            region.add(p);
            for (int k = 0; k < 4; k++) {
                int nr = p.fi() + dx[k], nc = p.se() + dy[k];
                if (nr >= 0 && nr < w.length && nc >= 0 && nc < w[0].length && !visited[nr][nc] && !w[nr][nc]) {
                    visited[nr][nc] = true;
                    q.add(new IntPair(nr, nc));
                }
            }
        }
        return region;
    }

    private void addVerticalPaths(boolean[][] w, Random rng, int rows, int cols) {
        int numPaths = Math.max(2, cols / 8);
        Set<Integer> usedCols = new HashSet<>();
        while (usedCols.size() < numPaths) usedCols.add(rng.nextInt(cols - 2) + 1);
        for (int c : usedCols) {
            int r = rows - 2;
            while (r > 1) {
                w[r][c] = false;
                if (rng.nextDouble() < 0.3 && c + 1 < cols - 1) w[r][c + 1] = false;
                if (rng.nextDouble() < 0.3 && c - 1 > 0) w[r][c - 1] = false;
                r--;
            }
        }
    }
}
