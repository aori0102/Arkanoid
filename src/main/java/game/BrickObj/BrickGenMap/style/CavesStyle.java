package game.BrickObj.BrickGenMap.style;

import static game.BrickObj.BrickGenMap.TransTypeNumBer.transTypeToNumber;
import static game.BrickObj.BrickGenMap.Mathx.*;

import game.BrickObj.BrickType;
import game.BrickObj.Init.Matrix;
import game.BrickObj.BrickGenMap.SpecialsSprinkler;
import game.BrickObj.BrickGenMap.StyleGenerator;
import game.BrickObj.BrickGenMap.TypePickers;
import java.util.Random;

/** CAVES: cellular automata smoothing of random walls. */
public final class CavesStyle implements StyleGenerator {
    @Override
    public Matrix generate(int rows, int cols, double difficulty, Random rng) {
        difficulty = keep01(difficulty);

        boolean[][] wall = new boolean[rows][cols];
        double initP = lerp(0.44, 0.62, difficulty);
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                wall[r][c] = rng.nextDouble() < initP;

        int steps = 3 + (int)Math.round(lerp(1, 3, difficulty));
        for (int s = 0; s < steps; s++) {
            boolean[][] nw = new boolean[rows][cols];
            for (int r = 0; r < rows; r++)
                for (int c = 0; c < cols; c++) {
                    int n = neighbors(wall, r, c);
                    nw[r][c] = (wall[r][c] && n >= 4) || (!wall[r][c] && n >= 5);
                }
            wall = nw;
        }

        Matrix g = new Matrix(rows, cols);
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (wall[r][c]) g.set(r, c, transTypeToNumber(TypePickers.pickFromTopHard(rng, 0.5 + 0.45 * difficulty)));
                else if (rng.nextDouble() < 0.15) g.set(r, c, transTypeToNumber(BrickType.Normal));

        SpecialsSprinkler.sprinkle(g, rng, difficulty);
        return g;
    }

    private static int neighbors(boolean[][] w, int r, int c) {
        int rows = w.length, cols = w[0].length, cnt = 0;
        for (int dr = -1; dr <= 1; dr++)
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                int nr = r + dr, nc = c + dc;
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) cnt++;
                else if (w[nr][nc]) cnt++;
            }
        return cnt;
    }
}
