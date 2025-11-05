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

/** MAZE: DFS-carved corridors on a grid of hard walls. */
public final class MazeStyle implements StyleGenerator {
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
