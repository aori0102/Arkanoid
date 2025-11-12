package game.Brick.BrickGenMap.balanceRule.ConcreteHandlers;

import game.Brick.BrickGenMap.balanceRule.BalanceHandler;
import game.Brick.BrickType;
import game.Brick.Init.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static game.Brick.BrickGenMap.TransTypeNumBer.transTypeToNumber;

/**
 * A Balance Handler that ensures the playfield's bottom row is not
 * completely blocked by {@link BrickType#Diamond} bricks.
 *
 * <p>This rule guarantees a minimum number of "gates" or "openings"
 * (non-diamond bricks) on the last row, defined by
 * {@code AT_LEAST_NUM_GATE}.
 *
 * <p>If the count of these openings is too low, this handler will
 * randomly select columns on the bottom row and replace whatever
 * brick is there (presumably a Diamond) with a {@link BrickType#Normal}
 * brick until the minimum number of gates is met.
 * {@code new Random()}, which will be non-deterministic and will not
 * respect the main generator's seed.
 */
public class BottomNotFullDiamondRule extends BalanceHandler {

    /**
     * Applies the "bottom gate" rule to the matrix.
     *
     * @param g The game matrix ({@link Matrix}) to be modified in-place.
     */
    @Override
    protected void applyRule(Matrix g) {
        int rows = g.rows();
        int cols = g.columns();

        // 1. Count existing non-diamond "gates" on the bottom row
        int num = 0;
        for (int col = 0; col < cols; col++) {
            if (g.get(rows - 1, col) != transTypeToNumber(BrickType.Diamond)) {
                num++;
            }
        }
        // 2. If the number of gates is sufficient, do nothing.
        if (num < AT_LEAST_NUM_GATE) {
            // 3. If gates are insufficient, create them
            List<Integer> rngList = new ArrayList<>();
            for (int col = 0; col < cols; col++) {
                rngList.add(col);
            }

            Collections.shuffle(rngList);

            for (int i = 0; i < AT_LEAST_NUM_GATE - num; i++) {
                int row = rows - 1;
                int col = rngList.get(i);

                g.set(row, col, transTypeToNumber(BrickType.Normal));
            }
        }
    }
}
