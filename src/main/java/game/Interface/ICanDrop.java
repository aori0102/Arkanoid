package game.Interface;

/**
 * An interface for all types of drop in game.
 */
public interface ICanDrop {
    /**
     * Handle the dropping movement
     */
    public void handleDroppingMovement(double droppingSpeed);
}
