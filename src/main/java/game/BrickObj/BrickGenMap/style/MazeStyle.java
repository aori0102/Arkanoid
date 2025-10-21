package game.BrickObj.BrickGenMap.style;

import static game.BrickObj.InitMatrix.*;
import static game.BrickObj.BrickGenMap.Mathx.*;
import static game.BrickObj.BrickGenMap.GridUtils.*;

import game.BrickObj.BrickType;
import game.BrickObj.InitMatrix.BrickMatrix;
import game.BrickObj.BrickGenMap.SpecialsSprinkler;
import game.BrickObj.BrickGenMap.StyleGenerator;
import game.BrickObj.BrickGenMap.TypePickers;
import java.util.Random;
import java.util.Stack;

/** MAZE: DFS-carved corridors on a grid of hard walls. */
public final class MazeStyle implements StyleGenerator {
    @Override
    public BrickMatrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);
        BrickMatrix g = new BrickMatrix(rows, cols);
        // start with hard walls
        BrickType wall = TypePickers.pickFromTopHard(rng, 0.55 + 0.4 * difficulty);
        fillAll(g, wall);

        boolean[][] visited = new boolean[rows][cols];
        int sr = ((rng.nextInt(Math.max(1, rows / 2)) * 2) | 1);
        int sc = ((rng.nextInt(Math.max(1, cols / 2)) * 2) | 1);
        Stack<int[]> st = new Stack<>();
        st.push(new int[]{sr, sc});
        visited[sr][sc] = true;
        g.set(sr, sc, getNewBrick(TypePickers.pickFromWeak(rng, difficulty)));

        int[][] DIR = {{2,0},{-2,0},{0,2},{0,-2}};
        while (!st.isEmpty()) {
            int[] cur = st.peek();
            int r = cur[0], c = cur[1];
            // shuffle directions
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
                g.set(mr, mc, getNewBrick(TypePickers.pickFromWeak(rng, difficulty)));
                g.set(nr, nc, getNewBrick(TypePickers.pickFromWeak(rng, difficulty)));
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
