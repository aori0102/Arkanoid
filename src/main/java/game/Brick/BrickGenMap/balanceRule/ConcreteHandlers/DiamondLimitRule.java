package game.Brick.BrickGenMap.balanceRule.ConcreteHandlers;

import game.Brick.BrickGenMap.balanceRule.BalanceHandler;
import game.Brick.BrickType;
import game.Brick.Init.*;

import java.util.*;

import static game.Brick.BrickGenMap.TransTypeNumBer.transNumberToType;
import static game.Brick.BrickGenMap.TransTypeNumBer.transTypeToNumber;
import static java.lang.Math.abs;


/**
 * A Balance Handler that enforces a maximum limit on the number of
 * {@link BrickType#Diamond} bricks allowed on the map.
 *
 * <p>This rule operates as follows:
 * <ol>
 * <li>It scans the grid to find all diamond bricks.</li>
 * <li>If the total count exceeds {@code MAX_DIAMOND}, it randomly selects
 * a subset of diamonds to "keep" (up to the limit).</li>
 * <li>All other "excess" diamonds are collected.</li>
 * <li>These excess diamonds are then randomly replaced with:
 * <ul>
 * <li>{@link BrickType#Normal} (based on {@code RATIO_NORMAL})</li>
 * <li>{@link BrickType#Steel} (based on {@code RATIO_STEEL})</li>
 * <li>A random special brick (the remainder).</li>
 * </ul>
 * </li>
 * </ol>
 *
 * <p><b>Warning:</b> This handler creates its own {@code new Random()} instance,
 * which means its replacements will be non-deterministic and will not
 * respect the generator's main seed.
 */
public class DiamondLimitRule extends BalanceHandler {

    /**
     * Applies the diamond limiting rule to the matrix.
     *
     * @param g The game matrix ({@link Matrix}) to be modified in-place.
     */

    @Override
    protected void applyRule(Matrix g) {
        final int rows = g.rows();
        final int cols = g.columns();

        Random rng = new Random();

        // 1. Find all diamond positions
        List<IntPair> diamondsPos = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                if (transNumberToType(g.get(r, c)) == BrickType.Diamond) {
                    diamondsPos.add(new IntPair(r, c));
                }

            }
        }

        // No diamonds to limit, exit early.
        if (diamondsPos.isEmpty()) return;

        // 2. Shuffle and determine which diamonds to "keep"
        Collections.shuffle(diamondsPos, rng);
        Set<IntPair> keep = new HashSet<>(diamondsPos.subList(0, Math.min(MAX_DIAMOND, diamondsPos.size())));
        List<IntPair> needToChange = new ArrayList<>();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                BrickType cur = transNumberToType(g.get(r, c));
                if (cur != BrickType.Diamond) continue;

                IntPair cell = new IntPair(r, c);
                if (!keep.contains(cell)) {
                    needToChange.add(new  IntPair(r, c));
                }
            }
        }

        // 3. Collect all "excess" diamonds that are not in the 'keep' set
        Collections.shuffle(needToChange, rng);
        int lengthForNormal = (int) (needToChange.size() * RATIO_NORMAL);
        int lengthForSteel =  (int) (needToChange.size() * RATIO_STEEL);

        for (int i = 0; i < lengthForNormal; i++) {
            IntPair cell = needToChange.get(i);
            int r = cell.fi();
            int c = cell.se();
            g.set(r, c, transTypeToNumber(BrickType.Normal));
        }

        for (int i = lengthForNormal; i < lengthForSteel + lengthForNormal; i++) {
            IntPair cell = needToChange.get(i);
            int r = cell.fi();
            int c = cell.se();
            g.set(r, c, transTypeToNumber(BrickType.Steel));
        }

        for (int i = lengthForNormal + lengthForSteel; i < needToChange.size(); i++) {
            IntPair cell = needToChange.get(i);
            int r = cell.fi();
            int c = cell.se();

            int randType = abs(rng.nextInt() % 6) + 3;
            g.set(r, c, randType);
        }
    }
}
