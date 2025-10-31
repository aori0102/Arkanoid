package game.BrickObj.BrickGenMap;

import static game.BrickObj.BrickGenMap.Mathx.*;

import game.Brick.Brick;
import game.Brick.BrickType;
import game.BrickObj.Init.Matrix;
import game.BrickObj.BrickGenMap.style.*;

import java.util.*;

public final class GenMap {
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

    /**
     * Register all defaults style.
     */
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

    public List<List<BrickType>> generate() {
        List<List<BrickType>> result = new ArrayList<>();
        for (int row = 0; row < rows; row++) {
            List<BrickType> rowList = new ArrayList<>();
            for (int col = 0; col < cols; col++) {
                rowList.add(BrickType.Steel);
            }
            result.add(rowList);
        }
        return result;
    }

    /**
     * Return a new BrickMatrix for the chosen style.
     * Strategy pattern design for genMap structure.
     */
    public Matrix generate(MapStyle style, double difficulty) {
        StyleGenerator gen = strategies.get(style);
        return gen.generate(rows, cols, keep01(difficulty), rng);
    }


    /**
     * Allow opening system for another style which will be added.
     * Overide this function.
     */
    public void register(MapStyle style, StyleGenerator generator) {
        strategies.put(style, generator);
    }
}
