package game.Brick.BrickGenMap;

import game.Brick.BrickGenMap.balanceRule.BalanceHandler;
import game.Brick.BrickGenMap.balanceRule.ConcreteHandlers.*;
import game.Brick.Init.Matrix;

/**
 * A static "Facade" class for the map balancing system.
 *
 * <p>This class preserves the original static {@code balanceCondition(Matrix g)}
 * method signature, allowing {@code GenMap} to call it without any changes.
 *
 * <p>Internally, it secretly uses the Chain of Responsibility pattern
 * to execute the various balancing rules.
 */
public final class BalanceCondition {

    /**
     * The first handler (head) in our Chain of Responsibility.
     * It is static and final, initialized once.
     * And with private constructor, we will make sure that it will not have any instance.
     */
    private BalanceCondition() {}
    private static final BalanceHandler balanceChainHead;

    /*
     * This is a "static initializer block".
     * The code inside this block is executed automatically ONE TIME
     * when the BalanceCondition class is first loaded by the JVM.
     *
     * <p>This is where we build the chain.
     */
    static {
        BalanceHandler rule1 = new BottomNotFullDiamondRule();
        BalanceHandler rule2 = new AlwayHavePathRule();
        BalanceHandler rule3 = new DiamondLimitRule();
        BalanceHandler rule4 = new SpecialBrickLimitRule();

        rule1.setNext(rule2).setNext(rule3).setNext(rule4);

        balanceChainHead = rule1;
    }

    /**
     * This is the public method called by GenMap.
     * It's the one you wanted to keep unchanged.
     *
     * @param g The game Matrix to be balanced.
     */
    public static void balanceCondition(Matrix g) {
        if (balanceChainHead != null) {
            balanceChainHead.handle(g);
        }
    }
}