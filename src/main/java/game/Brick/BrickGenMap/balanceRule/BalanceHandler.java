package game.Brick.BrickGenMap.balanceRule;
import game.Brick.Init.Matrix;

/**
 * Abstract Handler class for the Chain of Responsibility pattern design.
 * Manages the next "link" (handler) in the chain.
 */
public abstract class BalanceHandler {
    protected final static int AT_LEAST_NUM_GATE = 4;
    protected final static int MAX_DIAMOND = 15;
    protected final static double RATIO_NORMAL = 0.6;
    protected final static double RATIO_STEEL = 0.3;
    protected final static int MAX_ROCK = 3;
    protected final static int MAX_ROCKET = 5;
    protected final static int MAX_BOMB = 5;
    protected final static int MAX_WHEEL = 2;
    protected BalanceHandler nextHandler;

    public BalanceHandler setNext(BalanceHandler handler) {
        this.nextHandler = handler;
        return handler;
    }

    public void handle(Matrix g) {
        applyRule(g);

        if (nextHandler != null) {
            nextHandler.handle(g);
        }
    }

    protected abstract void applyRule(Matrix g);

}
