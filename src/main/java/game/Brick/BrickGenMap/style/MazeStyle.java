package game.Brick.BrickGenMap.style;

import static game.Brick.BrickGenMap.TransTypeNumBer.transTypeToNumber;
import static game.Brick.Init.*;
import static game.Brick.BrickGenMap.Mathx.*;

import game.Brick.BrickType;
import game.Brick.BrickGenMap.SpecialsSprinkler;
import game.Brick.BrickGenMap.StyleGenerator;
import game.Brick.BrickGenMap.TypePickers;
import java.util.Random;
import java.util.Stack;

/**
 * Generates a maze-like structure using a randomized Depth-First Search (DFS)
 * algorithm, also known as "recursive backtracking."
 * <p>
 * This generator works by "carving" corridors out of a solid grid of hard bricks.
 * <ol>
 * <li>The entire grid is first filled with a 'hard' wall brick type.</li>
 * <li>A random starting cell (at an odd-numbered coordinate) is chosen
 * and its wall is replaced with a weak, breakable brick.</li>
 * <li>The algorithm then performs a randomized DFS, moving two steps at a time
 * (e.g., from (1,1) to (1,3)).</li>
 * <li>It carves a path by replacing the walls at both the destination cell
 * (1,3) and the cell in between (1,2) with weak bricks.</li>
 * <li>The process continues, backtracking with a {@link Stack} when a dead end
 * is reached, until all reachable cells have been carved out.</li>
 * <li>Finally, {@link SpecialsSprinkler} is used to add special bricks
 * onto the newly created paths.</li>
 * </ol>
 *
 * @see StyleGenerator
 * @see SpecialsSprinkler
 * @see TypePickers
 */
public final class MazeStyle implements StyleGenerator {

    /**
     * Generates a maze-like brick layout using a randomized DFS algorithm.
     *
     * @param rows       The number of rows for the grid.
     * @param cols       The number of columns for the grid.
     * @param difficulty The difficulty level (0.0 to 1.0), which influences the
     * hardness of the walls and the density of special bricks.
     * @param rng        The {@link Random} instance to use for all randomized
     * operations (maze path, brick types).
     * @return A {@link Matrix} where 'hard' bricks form the maze walls and
     * 'weak' or 'special' bricks form the carved-out paths.
     */
    @Override
    public Matrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        BrickType wall = TypePickers.pickFromTopHard(rng, 0.55 + 0.4 * difficulty);
        Matrix g = new Matrix(rows, cols, transTypeToNumber(wall));

        boolean[][] visited = new boolean[rows][cols];
        int sr = ((rng.nextInt(Math.max(1, rows / 2)) * 2) | 1);
        int sc = ((rng.nextInt(Math.max(1, cols / 2)) * 2) | 1);
        Stack<int[]> st = new Stack<>();
        st.push(new int[]{sr, sc});
        visited[sr][sc] = true;
        g.set(sr, sc, transTypeToNumber(TypePickers.pickFromWeak(rng, difficulty)));

        int[][] DIR = {{2,0},{-2,0},{0,2},{0,-2}};
        while (!st.isEmpty()) {
            int[] cur = st.peek();
            int r = cur[0], c = cur[1];
            for (int i = 0; i < 4; i++) {
                int j = rng.nextInt(4);
                int[] t = DIR[i]; DIR[i] = DIR[j]; DIR[j] = t;
            }
            boolean moved = false;
            for (int[] d : DIR) {
                int nr = r + d[0], nc = c + d[1];
                if (!inBounds(nr, nc, rows, cols) || visited[nr][nc]) continue;
                visited[nr][nc] = true;
                int mr = r + d[0] / 2, mc = c + d[1] / 2;
                g.set(mr, mc, transTypeToNumber(TypePickers.pickFromWeak(rng, difficulty)));
                g.set(nr, nc, transTypeToNumber(TypePickers.pickFromWeak(rng, difficulty)));
                st.push(new int[]{nr, nc});
                moved = true;
                break;
            }
            if (!moved) st.pop();
        }

        SpecialsSprinkler.sprinkle(g, rng, difficulty);
        return g;
    }
}
