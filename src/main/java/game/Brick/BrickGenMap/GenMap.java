package game.Brick.BrickGenMap;

import static game.Brick.BrickGenMap.BalanceCondition.balanceCondition;
import static game.Brick.BrickGenMap.MapStyle.RANDOM;
import static game.Brick.BrickGenMap.Mathx.*;
import static game.Brick.BrickGenMap.TransTypeNumBer.transNumberToType;
import static java.lang.Math.min;

import game.Brick.BrickType;
import game.Brick.Init.Matrix;
import game.Brick.BrickGenMap.style.*;

import java.util.*;

/**
 * The main class responsible for generating game maps (levels).
 *
 * <p>This class acts as an orchestrator, combining several key components:
 * <ul>
 * <li>A <b>Strategy Pattern</b> (via {@link StyleGenerator}) to create maps
 * with different visual layouts (e.g., RANDOM, HEART, STRIPES).</li>
 * <li>A difficulty system based on an internal {@code level} counter.</li>
 * <li>A seeded {@link Random} generator for reproducible maps.</li>
 * <li>A post-generation balancing step ({@link BalanceCondition#balanceCondition})
 * to ensure maps are playable.</li>
 * </ul>
 *
 * It is the primary entry point for the game engine to get a new map.
 */
public final class GenMap {

    private int level = 0;
    private final int rows;
    private final int cols;
    private final Random rng;
    private final Map<MapStyle, StyleGenerator> strategies = new EnumMap<>(MapStyle.class);

    /**
     * Creates a new map generator with the given dimensions.
     * Uses a non-deterministic seed based on {@link System#nanoTime()}.
     *
     * @param rows The number of rows for the map.
     * @param cols The number of columns for the map.
     */
    public GenMap(int rows, int cols) {
        this(rows, cols, System.nanoTime());
    }

    /**
     * Creates a new map generator with the given dimensions and a specific seed.
     * Using a seed allows for generating reproducible maps for testing or sharing.
     *
     * @param rows The number of rows for the map.
     * @param cols The number of columns for the map.
     * @param seed The seed to initialize the {@link Random} generator.
     */
    public GenMap(int rows, int cols, long seed) {
        this.rows = Math.max(1, rows);
        this.cols = Math.max(1, cols);
        this.rng = new Random(seed);
        registerDefaults();
    }

    /**
     * Populates the {@code strategies} map with all default
     * {@link StyleGenerator} implementations.
     */
    private void registerDefaults() {
        strategies.put(RANDOM, new RandomStyle());
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

    /**
     * Selects a random {@link MapStyle} from the list of registered strategies.
     * @return A randomly chosen MapStyle.
     * @throws IllegalStateException if no styles are registered.
     */
    private MapStyle getRandomMapStyle() {
        if (strategies.isEmpty()) {
            throw new IllegalStateException("No registered map styles!");
        }

        return MapStyle.SINE;

//        List<MapStyle> styles = new ArrayList<>(strategies.keySet());
//        return styles.get(rng.nextInt(styles.size()));
    }

    /**
     * Calculates a random value, seemingly for difficulty calculation.
     * @return A double value clamped between 0.0 and 1.0.
     */
    private double getRandomDouble() {
        double base = min(0.8, level * 0.02);
        double add = min(rng.nextDouble() * 0.002, 0.02);
        return keep01(base + add);
    }

    /**
     * Gets the difficulty level for the current map generation.
     * @return A difficulty value, typically between 0.0 and 1.0.
     */
    private double getDifficultLevel() {
        return getRandomDouble();
    }

    /**
     * Generates a new, complete, and balanced map for the next level.
     * <p>
     * This is the primary method called by the game. It performs all steps:
     * <ol>
     * <li>Increments the internal level counter.</li>
     * <li>Picks a random style and calculates difficulty.</li>
     * <li>Generates the raw map {@link Matrix} using the style strategy.</li>
     * <li>Applies all post-generation balancing rules (e.g., {@code balanceCondition}).</li>
     * <li>Converts the internal number-based Matrix into a user-friendly {@code List<List<BrickType>>}.</li>
     * </ol>
     *
     * @return A 2D List of {@link BrickType} representing the balanced, playable map.
     */
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
                rowList.add(transNumberToType(mapType.get(row, col)));
            }
            result.add(rowList);
        }

        return result;
    }

    /**
     * Generates a raw, *unbalanced* map matrix for a specific style and difficulty.
     * <p>
     * This method retrieves the corresponding {@link StyleGenerator} (Strategy)
     * and delegates the map creation to it. The returned map is "raw" and
     * has not had any balancing rules applied.
     *
     * @param style The {@link MapStyle} to generate.
     * @param difficulty A value (clamped 0.0-1.0) controlling the generation.
     * @return The raw, unbalanced {@link Matrix} (internal representation).
     * @throws IllegalArgumentException if the specified style is not registered.
     */
    public Matrix generate(MapStyle style, double difficulty) {
        StyleGenerator gen = strategies.get(style);
        if (gen == null)
            throw new IllegalArgumentException("MapStyle not registered: " + style);
        return gen.generate(rows, cols, keep01(difficulty), rng);
    }


    /**
     * Registers a new {@link StyleGenerator} strategy or overwrites an existing one.
     * This allows for adding new map styles at runtime.
     *
     * @param style The {@link MapStyle} enum to use as the key.
     * @param generator The {@link StyleGenerator} implementation (the strategy).
     */
    public void register(MapStyle style, StyleGenerator generator) {
        strategies.put(style, generator);
    }
}
