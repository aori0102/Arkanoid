package game.Brick.BrickGenMap;
import game.Brick.BrickObj;
import game.Brick.InitBrick;

import java.util.Random;
import java.util.Stack;
import java.util.Vector;

public final class GenMap extends InitBrick {

    private int ROWS;
    private int COLS;

    private GenMap() {}

    public void getData(int rows, int cols) {
        ROWS = rows;
        COLS = cols;
    }
    
    public enum Style {
        RANDOM, GRADIENT, STRIPES, CHECKER, SYMMETRIC, ROOMS, HEART, DUNGEON,
        RINGS, SPIRAL, SINE, CROSS, MAZE, CAVES, DIAGONAL, STAR
    }


    private Random rng;

    // ------------------------------------------------------------------ //
    // PUBLIC
    // ------------------------------------------------------------------ //
    public BrickMatrix generate(Style style, double difficulty) {
        difficulty = clamp01(difficulty);
        return switch (style) {
            case RANDOM    -> generateRandom(lerp(0.55, 0.85, 1.0 - difficulty), difficulty);
            case GRADIENT  -> generateGradientVertical(difficulty);
            case STRIPES   -> generateStripes(
                    new BrickObj.BrickType[]{
                            BrickObj.BrickType.Normal, BrickObj.BrickType.Rock,
                            BrickObj.BrickType.Steel,  BrickObj.BrickType.Diamond},
                    3, difficulty);
            case CHECKER   -> generateChecker(BrickObj.BrickType.Normal, BrickObj.BrickType.Rock, 1, 1, difficulty);
            case SYMMETRIC -> generateSymmetricRandom(lerp(0.55, 0.85, 1.0 - difficulty), difficulty);
            case ROOMS     -> generateRooms(difficulty);
            case HEART     -> generateHeart(difficulty);
            case DUNGEON   -> generateDungeon(difficulty);
            case RINGS     -> generateRings(difficulty);
            case SPIRAL    -> generateSpiral(difficulty);
            case SINE      -> generateSine(difficulty);
            case CROSS     -> generateCross(difficulty);
            case MAZE      -> generateMaze(difficulty);
            case CAVES     -> generateCaves(difficulty);
            case DIAGONAL  -> generateDiagonalStripes(difficulty);
            case STAR      -> generateStar(difficulty);
        };
    }

    // ------------------------------------------------------------------ //
    // EXISTING STYLES (rút gọn – giống bản trước)
    // ------------------------------------------------------------------ //
    public BrickMatrix generateRandom(double density, double hardnessBias) {
        BrickMatrix g = emptyGrid();
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++)
                if (rng.nextDouble() < density)
                    g.set(r, c, new BrickObj(pickByBias(hardnessBias)));
        sprinkleSpecialsByDifficulty(g, hardnessBias);
        return g;
    }

    public BrickMatrix generateGradientVertical(double difficulty) {
        BrickMatrix g = emptyGrid();
        for (int r = 0; r < ROWS; r++) {
            double rowBias = (double) r / Math.max(1, ROWS - 1);
            double bias = clamp01(0.15 + 0.85 * Math.pow(rowBias, 0.75 + 0.5 * difficulty));
            BrickObj.BrickType ty = pickByBias(bias);
            for (int c = 0; c < COLS; c++) g.set(r, c, new BrickObj(ty));
        }
        sprinkleSpecialsByDifficulty(g, difficulty);
        return g;
    }

    public BrickMatrix generateStripes(BrickObj.BrickType[] palette, int stripeH, double difficulty) {
        if (palette == null || palette.length == 0) palette = new BrickObj.BrickType[]{BrickObj.BrickType.Normal};
        if (stripeH <= 0) stripeH = 2;
        BrickMatrix g = emptyGrid();
        for (int r = 0; r < ROWS; r++) {
            BrickObj.BrickType ty = palette[(r / stripeH) % palette.length];
            for (int c = 0; c < COLS; c++) g.set(r, c, new BrickObj(ty));
        }
        sprinkleSpecialsByDifficulty(g, difficulty);
        return g;
    }

    public BrickMatrix generateChecker(BrickObj.BrickType a, BrickObj.BrickType b,
                                       int cellH, int cellW, double difficulty) {
        if (cellH <= 0) cellH = 1;
        if (cellW <= 0) cellW = 1;
        BrickMatrix g = emptyGrid();
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++)
                g.set(r, c, new BrickObj((((r / cellH) + (c / cellW)) & 1) == 0 ? a : b));
        sprinkleSpecialsByDifficulty(g, difficulty);
        return g;
    }

    public BrickMatrix generateSymmetricRandom(double density, double hardnessBias) {
        BrickMatrix g = emptyGrid();
        int half = (COLS + 1) / 2;
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < half; c++)
                if (rng.nextDouble() < density) {
                    BrickObj.BrickType t = pickByBias(hardnessBias);
                    g.set(r, c, new BrickObj(t));
                    int m = COLS - 1 - c;
                    if (m != c) g.set(r, m, new BrickObj(t));
                }
        sprinkleSpecialsByDifficulty(g, hardnessBias);
        return g;
    }

    public BrickMatrix generateRooms(double difficulty) {
        BrickMatrix g = emptyGrid();
        fillAll(g, BrickObj.BrickType.Normal);
        int rooms = 3 + (int) Math.round(lerp(2, 5, difficulty));
        for (int k = 0; k < rooms; k++) {
            int h = 3 + rng.nextInt(5), w = 3 + rng.nextInt(6);
            int r0 = rng.nextInt(Math.max(1, ROWS - h));
            int c0 = rng.nextInt(Math.max(1, COLS - w));
            BrickObj.BrickType wall = pickFromTopHard(lerpIndexTop(0.5 + 0.5 * difficulty));
            BrickObj.BrickType fill = pickByBias(0.25 + 0.5 * difficulty);
            drawRect(g, r0, c0, h, w, wall, true);
            fillRect(g, r0 + 1, c0 + 1, Math.max(0, h - 2), Math.max(0, w - 2), fill);
        }
        sprinkleSpecialsByDifficulty(g, difficulty);
        return g;
    }

    public BrickMatrix generateHeart(double difficulty) {
        BrickMatrix g = emptyGrid();
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++) {
                double x = map(c, 0, COLS - 1, -1.3, 1.3);
                double y = map(r, 0, ROWS - 1,  1.3, -1.1);
                double f = Math.pow(x * x + y * y - 1.0, 3) - x * x * Math.pow(y, 3);
                if (f <= 0.0) {
                    double edge = clamp01(1.0 - Math.min(1.0, Math.abs(f) * 40));
                    double bias = clamp01(0.25 + 0.6 * difficulty + 0.15 * edge);
                    g.set(r, c, new BrickObj(pickByBias(bias)));
                } else if (rng.nextDouble() < 0.06) {
                    g.set(r, c, new BrickObj(BrickObj.BrickType.Normal));
                }
            }
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++) {
                if (g.get(r, c) == null) continue;
                boolean edge = false;
                for (int dr = -1; dr <= 1 && !edge; dr++)
                    for (int dc = -1; dc <= 1 && !edge; dc++) {
                        if (dr == 0 && dc == 0) continue;
                        int nr = r + dr, nc = c + dc;
                        if (!inBounds(nr, nc) || g.get(nr, nc) == null) edge = true;
                    }
                if (edge && rng.nextDouble() < 0.75)
                    g.set(r, c, new BrickObj(pickFromTopHard(lerpIndexTop(0.6 + 0.35 * difficulty))));
            }
        sprinkleSpecialsByDifficulty(g, difficulty);
        return g;
    }

    public BrickMatrix generateDungeon(double difficulty) {
        BrickMatrix g = emptyGrid();
        int rooms = 4 + (int) Math.round(lerp(1, 4, difficulty));
        Vector<Room> rs = new Vector<>();
        for (int k = 0; k < rooms; k++) {
            int h = 3 + rng.nextInt(6), w = 3 + rng.nextInt(7);
            int r0 = rng.nextInt(Math.max(1, ROWS - h));
            int c0 = rng.nextInt(Math.max(1, COLS - w));
            BrickObj.BrickType wall = pickFromTopHard(lerpIndexTop(0.55 + 0.45 * difficulty));
            drawRect(g, r0, c0, h, w, wall, true);
            rs.add(new Room(r0, c0, h, w));
            if (rng.nextDouble() < 0.6) {
                BrickObj.BrickType fill = pickByBias(0.25 + 0.5 * difficulty);
                fillRectSparse(g, r0 + 1, c0 + 1, Math.max(0, h - 2), Math.max(0, w - 2), fill, 0.35);
            }
        }
        int maxDoorsPerRoom = (int) Math.max(1, Math.round(lerp(3, 1, difficulty)));
        int doorWidth       = (int) Math.max(1, Math.round(lerp(2, 1, difficulty)));
        for (Room a : rs) {
            int created = 0;
            for (Room b : rs) {
                if (a == b || created >= maxDoorsPerRoom) break;
                if (!roomsNearby(a, b)) continue;
                if (rng.nextBoolean()) {
                    int row = clampI(a.r0 + a.h / 2, 0, ROWS - 1);
                    int cs = Math.min(a.c0 + a.w, b.c0 + b.w);
                    int ce = Math.max(a.c0, b.c0);
                    if (cs > ce) { int t = cs; cs = ce; ce = t; }
                    carveDoorLine(g, row, cs, ce, doorWidth, difficulty);
                } else {
                    int col = clampI(a.c0 + a.w / 2, 0, COLS - 1);
                    int rs0 = Math.min(a.r0 + a.h, b.r0 + b.h);
                    int re0 = Math.max(a.r0, b.r0);
                    if (rs0 > re0) { int t = rs0; rs0 = re0; re0 = t; }
                    carveDoorCol(g, col, rs0, re0, doorWidth, difficulty);
                }
                created++;
            }
        }
        sprinkleSpecialsByDifficulty(g, difficulty);
        return g;
    }

    // ------------------------------------------------------------------ //
    // NEW STYLES
    // ------------------------------------------------------------------ //

    // Vòng đồng tâm từ ngoài vào trong; gần tâm cứng hơn theo difficulty
    public BrickMatrix generateRings(double difficulty) {
        BrickMatrix g = emptyGrid();
        fillAll(g, BrickObj.BrickType.Normal);
        int layers = Math.min(ROWS, COLS) / 2;
        for (int d = 0; d < layers; d += 2) {
            int r0 = d, c0 = d, h = ROWS - 2 * d, w = COLS - 2 * d;
            if (h <= 1 || w <= 1) break;
            double t = (double) d / Math.max(1, layers - 1);
            double bias = clamp01(0.35 + (1.0 - t) * (0.55 * difficulty + 0.15));
            BrickObj.BrickType wall = pickByBias(bias);
            drawRect(g, r0, c0, h, w, wall, true);
            // mở một lối nhỏ mỗi vòng (khó cao thì lối hiếm)
            if (rng.nextDouble() < lerp(0.8, 0.3, difficulty)) {
                int side = rng.nextInt(4);
                switch (side) {
                    case 0 -> g.set(r0, c0 + rng.nextInt(w), new BrickObj(pickFromWeak(difficulty)));
                    case 1 -> g.set((r0 + h - 1), c0 + rng.nextInt(w), new BrickObj(pickFromWeak(difficulty)));
                    case 2 -> g.set((r0 + rng.nextInt(h)), c0, new BrickObj(pickFromWeak(difficulty)));
                    default-> g.set((r0 + rng.nextInt(h)), c0 + w - 1, new BrickObj(pickFromWeak(difficulty)));
                }
            }
        }
        sprinkleSpecialsByDifficulty(g, difficulty);
        return g;
    }

    // Xoắn ốc hình chữ nhật (tường cứng), có khe ngắt theo độ khó
    public BrickMatrix generateSpiral(double difficulty) {
        BrickMatrix g = emptyGrid();
        fillAll(g, BrickObj.BrickType.Normal);
        int top = 0, bottom = ROWS - 1, left = 0, right = COLS - 1;
        int gapEvery = (int) Math.max(3, Math.round(lerp(2, 6, 1 - difficulty)));
        int step = 0;
        while (top <= bottom && left <= right) {
            BrickObj.BrickType wall = pickFromTopHard(lerpIndexTop(0.55 + 0.45 * difficulty));
            for (int c = left; c <= right; c++) setSpiralCell(g, top, c, wall, step++, gapEvery, difficulty);
            for (int r = top + 1; r <= bottom; r++) setSpiralCell(g, r, right, wall, step++, gapEvery, difficulty);
            if (top < bottom) for (int c = right - 1; c >= left; c--) setSpiralCell(g, bottom, c, wall, step++, gapEvery, difficulty);
            if (left < right) for (int r = bottom - 1; r > top; r--) setSpiralCell(g, r, left, wall, step++, gapEvery, difficulty);
            top++; left++; bottom--; right--;
        }
        sprinkleSpecialsByDifficulty(g, difficulty);
        return g;
    }
    private void setSpiralCell(BrickMatrix g, int r, int c, BrickObj.BrickType wall,
                               int step, int gapEvery, double difficulty) {
        if (!inBounds(r, c)) return;
        if (step % gapEvery == 0 && rng.nextDouble() < lerp(0.8, 0.25, difficulty)) {
            g.set(r, c, new BrickObj(pickFromWeak(difficulty)));
        } else {
            g.set(r, c, new BrickObj(wall));
        }
    }

    // Dải sin chạy ngang – băng cứng theo đường sin; độ dày tăng theo độ khó
    public BrickMatrix generateSine(double difficulty) {
        BrickMatrix g = emptyGrid();
        fillAll(g, BrickObj.BrickType.Normal);
        double amp = lerp(3, 6, difficulty);
        double freq = lerp(0.8, 1.6, difficulty);
        int thickness = (int) Math.max(1, Math.round(lerp(1, 3, difficulty)));
        BrickObj.BrickType wall = pickFromTopHard(lerpIndexTop(0.55 + 0.35 * difficulty));
        for (int c = 0; c < COLS; c++) {
            double x = (2 * Math.PI * c / COLS) * freq;
            int r0 = (int) Math.round(ROWS / 2.0 + amp * Math.sin(x));
            for (int t = -thickness; t <= thickness; t++)
                if (inBounds(r0 + t, c)) g.set((r0 + t), c, new BrickObj(wall));
        }
        sprinkleSpecialsByDifficulty(g, difficulty);
        return g;
    }

    // Dấu cộng + chéo X
    public BrickMatrix generateCross(double difficulty) {
        BrickMatrix g = emptyGrid();
        fillAll(g, BrickObj.BrickType.Normal);
        BrickObj.BrickType hard = pickFromTopHard(lerpIndexTop(0.55 + 0.4 * difficulty));
        int midR = ROWS / 2, midC = COLS / 2;
        int thick = (int) Math.max(1, Math.round(lerp(1, 2, difficulty)));

        // dọc + ngang
        for (int r = 0; r < ROWS; r++)
            for (int t = -thick; t <= thick; t++)
                if (inBounds(r, midC + t)) g.set(r, midC + t, new BrickObj(hard));
        for (int c = 0; c < COLS; c++)
            for (int t = -thick; t <= thick; t++)
                if (inBounds(midR + t, c)) g.set((midR + t), c, new BrickObj(hard));

        // chéo
        for (int r = 0; r < ROWS; r++) {
            int c1 = (int) Math.round((long) r * COLS / (double) ROWS);
            int c2 = COLS - 1 - c1;
            for (int t = -thick; t <= thick; t++) {
                if (inBounds(r, c1 + t)) g.set(r, c1 + t, new BrickObj(hard));
                if (inBounds(r, c2 + t)) g.set(r, c2 + t, new BrickObj(hard));
            }
        }
        sprinkleSpecialsByDifficulty(g, difficulty);
        return g;
    }

    // MAZE: khắc mê cung bằng DFS; tường cứng, hành lang yếu
    public BrickMatrix generateMaze(double difficulty) {
        BrickMatrix g = emptyGrid();
        // start with walls
        BrickObj.BrickType wall = pickFromTopHard(lerpIndexTop(0.55 + 0.4 * difficulty));
        fillAll(g, wall);
        boolean[][] visited = new boolean[ROWS][COLS];

        // cells on odd grid
        int sr = (rng.nextInt(ROWS / 2) * 2) | 1;
        int sc = (rng.nextInt(COLS / 2) * 2) | 1;
        Stack<int[]> st = new Stack<>();
        st.push(new int[]{sr, sc});
        visited[sr][sc] = true;
        g.set(sr, sc, new BrickObj(pickFromWeak(difficulty))); // corridor

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
                if (!inBounds(nr, nc)) continue;
                if (visited[nr][nc]) continue;
                visited[nr][nc] = true;
                // knock down wall between
                int mr = r + d[0] / 2, mc = c + d[1] / 2;
                g.set(mr, mc, new BrickObj(pickFromWeak(difficulty)));
                g.set(nr, nc, new BrickObj(pickFromWeak(difficulty)));
                st.push(new int[]{nr, nc});
                moved = true;
                break;
            }
            if (!moved) st.pop();
        }

        sprinkleSpecialsByDifficulty(g, difficulty);
        return g;
    }

    // CAVES: cellular automata
    public BrickMatrix generateCaves(double difficulty) {
        boolean[][] wall = new boolean[ROWS][COLS];
        double initP = lerp(0.44, 0.62, difficulty);
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++)
                wall[r][c] = rng.nextDouble() < initP;

        int steps = 3 + (int) Math.round(lerp(1, 3, difficulty));
        for (int s = 0; s < steps; s++) {
            boolean[][] nw = new boolean[ROWS][COLS];
            for (int r = 0; r < ROWS; r++)
                for (int c = 0; c < COLS; c++) {
                    int n = countWallNeighbors(wall, r, c);
                    nw[r][c] = (wall[r][c] && n >= 4) || (!wall[r][c] && n >= 5);
                }
            wall = nw;
        }

        BrickMatrix g = emptyGrid();
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++)
                if (wall[r][c]) {
                    BrickObj.BrickType t = pickFromTopHard(lerpIndexTop(0.5 + 0.45 * difficulty));
                    g.set(r, c, new BrickObj(t));
                } else if (rng.nextDouble() < 0.15) {
                    g.set(r, c, new BrickObj(BrickObj.BrickType.Normal));
                }
        sprinkleSpecialsByDifficulty(g, difficulty);
        return g;
    }
    private int countWallNeighbors(boolean[][] w, int r, int c) {
        int cnt = 0;
        for (int dr = -1; dr <= 1; dr++)
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                int nr = r + dr, nc = c + dc;
                if (nr < 0 || nr >= ROWS || nc < 0 || nc >= COLS) cnt++;
                else if (w[nr][nc]) cnt++;
            }
        return cnt;
    }

    // Đường chéo song song (dải chéo)
    public BrickMatrix generateDiagonalStripes(double difficulty) {
        BrickMatrix g = emptyGrid();
        fillAll(g, BrickObj.BrickType.Normal);
        int period = (int) Math.max(2, Math.round(lerp(6, 3, difficulty)));
        int thick  = (int) Math.max(1, Math.round(lerp(1, 2, difficulty)));
        BrickObj.BrickType wall = pickFromTopHard(lerpIndexTop(0.55 + 0.3 * difficulty));
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++) {
                int k = Math.floorMod(r - c, period);
                if (k < thick) g.set(r, c, new BrickObj(wall));
            }
        sprinkleSpecialsByDifficulty(g, difficulty);
        return g;
    }

    // Ngôi sao: nhiều tia (rays) từ tâm
    public BrickMatrix generateStar(double difficulty) {
        BrickMatrix g = emptyGrid();
        fillAll(g, BrickObj.BrickType.Normal);
        int rays = 5 + (int) Math.round(lerp(0, 3, difficulty)); // 5..8 tia
        int cx = ROWS / 2, cy = COLS / 2;
        BrickObj.BrickType wall = pickFromTopHard(lerpIndexTop(0.6 + 0.35 * difficulty));
        int len = Math.max(ROWS, COLS);
        for (int i = 0; i < rays; i++) {
            double ang = (2 * Math.PI * i) / rays + rng.nextDouble() * 0.2;
            double dx = Math.cos(ang), dy = Math.sin(ang);
            for (int t = 0; t < len; t++) {
                int r = (int) Math.round(cx + dx * t);
                int c = (int) Math.round(cy + dy * t);
                if (!inBounds(r, c)) break;
                g.set(r, c, new BrickObj(wall));
                // độ dày tia theo độ khó
                if (difficulty > 0.5) {
                    setIfIn(g, r + 1, c, wall);
                    setIfIn(g, r, c + 1, wall);
                }
            }
        }
        sprinkleSpecialsByDifficulty(g, difficulty);
        return g;
    }

    // ------------------------------------------------------------------ //
    // HELPERS
    // ------------------------------------------------------------------ //
    private static final class Room { final int r0, c0, h, w; Room(int r0,int c0,int h,int w){this.r0=r0;this.c0=c0;this.h=h;this.w=w;} }

    private BrickMatrix emptyGrid() {
        return new BrickMatrix(ROWS, COLS);
    }

    private void fillAll(BrickMatrix g, BrickObj.BrickType t) {
        for (int r = 0; r < ROWS; r++) for (int c = 0; c < COLS; c++) g.set(r, c, new BrickObj(t));
    }

    private void fillRect(BrickMatrix g, int r0, int c0, int h, int w, BrickObj.BrickType t) {
        for (int r = 0; r < h; r++) {
            int rr = r0 + r; if (rr < 0 || rr >= ROWS) continue;
            for (int c = 0; c < w; c++) { int cc = c0 + c; if (cc < 0 || cc >= COLS) continue; g.set(rr, cc, new BrickObj(t)); }
        }
    }
    private void fillRectSparse(BrickMatrix g, int r0, int c0, int h, int w, BrickObj.BrickType t, double p) {
        p = clamp01(p);
        for (int r = 0; r < h; r++) {
            int rr = r0 + r; if (rr < 0 || rr >= ROWS) continue;
            for (int c = 0; c < w; c++) {
                int cc = c0 + c; if (cc < 0 || cc >= COLS) continue;
                if (rng.nextDouble() < p) g.set(rr, cc, new BrickObj(t));
            }
        }
    }
    private void drawRect(BrickMatrix g, int r0, int c0, int h, int w, BrickObj.BrickType t, boolean hollow) {
        if (!hollow) { fillRect(g, r0, c0, h, w, t); return; }
        for (int c = 0; c < w; c++) { setIfIn(g, r0, c0 + c, t); setIfIn(g, r0 + h - 1, c0 + c, t); }
        for (int r = 0; r < h; r++) { setIfIn(g, r0 + r, c0, t); setIfIn(g, r0 + r, c0 + w - 1, t); }
    }
    private void setIfIn(BrickMatrix g, int r, int c, BrickObj.BrickType t) { if (inBounds(r,c)) g.set(r, c, new BrickObj(t)); }
    private boolean inBounds(int r, int c) { return 0 <= r && r < ROWS && 0 <= c && c < COLS; }
    private boolean roomsNearby(Room a, Room b) {
        return !(a.r0 + a.h < b.r0 || b.r0 + b.h < a.r0 || a.c0 + a.w < b.c0 || b.c0 + b.w < a.c0);
    }
    private void carveDoorLine(BrickMatrix g, int row, int cStart, int cEnd, int doorW, double difficulty) {
        int mid = (cStart + cEnd) / 2;
        for (int dc = -doorW / 2; dc <= doorW / 2; dc++) {
            int cc = mid + dc; if (inBounds(row, cc)) g.set(row, cc, new BrickObj(pickFromWeak(difficulty)));
        }
    }
    private void carveDoorCol(BrickMatrix g, int col, int rStart, int rEnd, int doorW, double difficulty) {
        int mid = (rStart + rEnd) / 2;
        for (int dr = -doorW / 2; dr <= doorW / 2; dr++) {
            int rr = mid + dr; if (inBounds(rr, col)) g.set(rr, col, new BrickObj(pickFromWeak(difficulty)));
        }
    }

    // pickers & specials
    private BrickObj.BrickType pickByBias(double bias) {
        BrickObj.BrickType[] arr = BrickObj.BrickType.values();
        int maxIx = (int) Math.round(clamp01(bias) * (arr.length - 1));
        int ix = rng.nextInt(maxIx + 1);
        return arr[ix];
    }
    private BrickObj.BrickType pickFromTopHard(double topFrac) {
        BrickObj.BrickType[] arr = BrickObj.BrickType.values();
        topFrac = clamp01(topFrac);
        int start = (int) Math.floor((1.0 - topFrac) * arr.length);
        start = Math.min(start, arr.length - 1);
        int ix = start + rng.nextInt(arr.length - start);
        return arr[ix];
    }
    private BrickObj.BrickType pickFromWeak(double difficulty) {
        double bias = clamp01(0.15 + 0.25 * difficulty);
        BrickObj.BrickType t = pickByBias(bias);
        if (t == BrickObj.BrickType.Steel || t == BrickObj.BrickType.Diamond) t = BrickObj.BrickType.Normal;
        return t;
    }
    private void sprinkleSpecialsByDifficulty(BrickMatrix g, double difficulty) {
        double bombP  = clamp01(0.02 + 0.06 * difficulty);
        double giftP  = clamp01(0.03 + 0.07 * difficulty);
        double otherP = clamp01(0.01 + 0.05 * difficulty);
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++) {
                BrickObj cur = g.get(r, c);
                if (cur == null) continue;
                if (cur.getObjType() == BrickObj.BrickType.Steel || cur.getObjType() == BrickObj.BrickType.Diamond) {
                    if (rng.nextDouble() < 0.04 * difficulty) g.set(r, c, new BrickObj(BrickObj.BrickType.Rock));
                    else continue;
                }
                double x = rng.nextDouble();
                if (x < bombP) g.set(r, c, new BrickObj(BrickObj.BrickType.Bomb));
                else if (x < bombP + giftP) g.set(r, c, new BrickObj(BrickObj.BrickType.Gift));
                else if (x < bombP + giftP + otherP) {
                    BrickObj.BrickType[] pool = {
                            BrickObj.BrickType.Rocket, BrickObj.BrickType.Angel,
                            BrickObj.BrickType.Reborn, BrickObj.BrickType.Ball
                    };
                    g.set(r, c, new BrickObj(pool[rng.nextInt(pool.length)]));
                }
            }
    }

    // math utils
    private double clamp01(double x) { return Math.max(0.0, Math.min(1.0, x)); }
    private int clampI(int v, int lo, int hi) { return Math.max(lo, Math.min(hi, v)); }
    private double lerp(double a, double b, double t) { return a + (b - a) * clamp01(t); }
    private double map(double v, double a0, double a1, double b0, double b1) {
        if (a1 == a0) return b0;
        double t = (v - a0) / (a1 - a0);
        return b0 + (b1 - b0) * t;
    }
    private double lerpIndexTop(double x) { return clamp01(x); }
}
