package game.Interface;

/**
 * An interface for all types of drop in game.
 */
public interface ICanDropPowerUp {
    /**
     * Get the chance to spawn power up upon this
     * object is destroyed.
     * @return The chance to spawn a power up.
     */
    double getPowerUpSpawnChange();
}