package game.Brick.BrickGenMap.balanceRule.ConcreteHandlers;

import game.Brick.BrickGenMap.balanceRule.BalanceHandler;
import game.Brick.BrickType;
import game.Brick.Init;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static game.Brick.BrickGenMap.TransTypeNumBer.transNumberToType;
import static game.Brick.BrickGenMap.TransTypeNumBer.transTypeToNumber;

/**
 * A Balance Handler that enforces maximum counts for specific special bricks.
 *
 * <p>This rule scans the entire map and counts the occurrences of
 * {@link BrickType#Bomb}, {@link BrickType#Rocket}, {@link BrickType#Wheel},
 * and {@link BrickType#Rock}.
 *
 * <p>If any type exceeds its predefined maximum (e.g., {@code MAX_BOMB}),
 * the handler will randomly select the "excess" bricks of that type and
 * replace them with {@link BrickType#Normal}.
 * {@code new Random()}, which will be non-deterministic and will not
 * respect the main generator's seed.
 */
public class SpecialBrickLimitRule extends BalanceHandler {
    @Override
    protected void applyRule(Init.Matrix g) {
        final int rows = g.rows();
        final int cols = g.columns();

        List<Init.IntPair> listBomb = new ArrayList<>();
        List<Init.IntPair> listRocket = new ArrayList<>();
        List<Init.IntPair> listWheel = new ArrayList<>();
        List<Init.IntPair> listRock = new ArrayList<>();


        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                BrickType type = transNumberToType(g.get(row, col));

                switch (type) {
                    case Bomb -> listBomb.add(new Init.IntPair(row, col));
                    case Rocket -> listRocket.add(new Init.IntPair(row, col));
                    case Wheel -> listWheel.add(new Init.IntPair(row, col));
                    case Rock -> listRock.add(new Init.IntPair(row, col));
                    default -> {}
                }
            }
        }

        // 2. Call the helper function to limit each type
        // The excess bricks are replaced with BrickType.Normal
        limitBrickType(g, listBomb, MAX_BOMB, BrickType.Normal);
        limitBrickType(g, listRocket, MAX_ROCKET, BrickType.Normal);
        limitBrickType(g, listWheel, MAX_WHEEL, BrickType.Normal);
        limitBrickType(g, listRock, MAX_ROCK, BrickType.Normal);
    }

    /**
     * A private helper method to limit the count of a single brick type.
     * It checks if the number of bricks in {@code positions} exceeds
     * {@code maxAllowed}. If it does, it shuffles the list, takes the
     * excess amount from the start of the shuffled list, and replaces
     * them in the matrix {@code g} with {@code replaceType}.
     *
     * @param g           The game map Matrix to modify.
     * @param positions   A list of {@link Init.IntPair} coordinates for all bricks of a specific type.
     * @param maxAllowed  The maximum number of this brick type allowed to remain.
     * @param replaceType The {@link BrickType} to use as a replacement (e.g., Normal).
     */
    private static void limitBrickType(Init.Matrix g, List<Init.IntPair> positions, int maxAllowed, BrickType replaceType) {
        // Only act if the current count exceeds the maximum allowed
        if (positions.size() > maxAllowed) {

            // Shuffle the list to randomize which bricks are removed
            Collections.shuffle(positions);

            // Calculate how many bricks we need to remove
            int excessCount = positions.size() - maxAllowed;

            // Loop 'excessCount' times, removing the "excess" bricks
            // from the beginning of the shuffled list.
            for (int i = 0; i < excessCount; i++) {
                Init.IntPair cell = positions.get(i);
                int r = cell.fi();
                int c = cell.se();

                // Set the excess brick to the replacement type
                g.set(r, c, transTypeToNumber(replaceType));
            }
        }
    }
}
