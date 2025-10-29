package game.Obstacle;

public interface ICanDamagePlayer {
    /**
     * Return the amount of damage by this object.
     *
     * @return This object's damage.
     */
    int getDamage();

    /**
     * Handle whatever happens to the object after damaging the player.
     */
    void onDamagedPlayer();
}