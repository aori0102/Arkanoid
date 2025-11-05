package game.Brick.BrickGenMap;

import static game.Brick.BrickGenMap.BalanceCondition.balanceCondition;
import static game.Brick.BrickGenMap.Mathx.*;
import static game.Brick.BrickGenMap.TransTypeNumBer.transNumberToType;

import game.Brick.BrickType;
import game.Brick.Init.Matrix;
import game.Brick.BrickGenMap.style.*;

import java.util.*;

public final class GenMap {

    private int level = 0;
    private final int rows;
    private final int cols;
    private final Random rng;
    private final Map<MapStyle, StyleGenerator> strategies = new EnumMap<>(MapStyle.class);

    public GenMap(int rows, int cols) {
        this(rows, cols, System.nanoTime());
    }

    public GenMap(int rows, int cols, long seed) {
        this.rows = Math.max(1, rows);
        this.cols = Math.max(1, cols);
        this.rng = new Random(seed);
        registerDefaults();
    }

    private void registerDefaults() {
        strategies.put(MapStyle.RANDOM, new RandomStyle());
        strategies.put(MapStyle.GRADIENT, new GradientStyle());
        strategies.put(MapStyle.STRIPES, new StripesStyle());
        strategies.put(MapStyle.CHECKER, new CheckerStyle());
        strategies.put(MapStyle.SYMMETRIC, new SymmetricStyle());
        strategies.put(MapStyle.ROOMS, new RoomsStyle());
        strategies.put(MapStyle.HEART, new HeartStyle());
        strategies.put(MapStyle.DUNGEON, new DungeonStyle());
        strategies.put(MapStyle.RINGS, new RingsStyle());
        strategies.put(MapStyle.SPIRAL, new SpiralStyle());
        strategies.put(MapStyle.SINE, new SineStyle());
        strategies.put(MapStyle.CROSS, new CrossStyle());
        strategies.put(MapStyle.MAZE, new MazeStyle());
        strategies.put(MapStyle.CAVES, new CavesStyle());
        strategies.put(MapStyle.DIAGONAL, new DiagonalStyle());
        strategies.put(MapStyle.STAR, new StarStyle());
    }

    private MapStyle getRandomMapStyle() {
        if (strategies.isEmpty()) {
            throw new IllegalStateException("No registered map styles!");
        }

        List<MapStyle> styles = new ArrayList<>(strategies.keySet());
        return styles.get(rng.nextInt(styles.size()));
    }

    private double getRandomDouble() {
        double base = Math.min(0.8,  level * 0.02);
        double add = rng.nextDouble() * 0.002;
        return keep01(base + add);
    }

    private double getDifficultLevel() {
        return getRandomDouble();
    }

    public List<List<BrickType>> generate() {
        level++;
        if (level * 0.02 == 0.8) {
            level--;
        }

        Matrix mapType = generate(getRandomMapStyle(), getDifficultLevel());
        balanceCondition(mapType);

        List<List<BrickType>> result = new ArrayList<>();
        for (int row = 0; row < rows; row++) {
            List<BrickType> rowList = new ArrayList<>();
            for (int col = 0; col < cols; col++) {
                rowList.add(BrickType.Normal);
            }
            result.add(rowList);
        }

        return result;
    }

    public Matrix generate(MapStyle style, double difficulty) {
        StyleGenerator gen = strategies.get(style);
        if (gen == null)
            throw new IllegalArgumentException("MapStyle not registered: " + style);
        return gen.generate(rows, cols, keep01(difficulty), rng);
    }

    public void register(MapStyle style, StyleGenerator generator) {
        strategies.put(style, generator);
    }
}
