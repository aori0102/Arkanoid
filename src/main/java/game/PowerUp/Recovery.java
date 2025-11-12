package game.PowerUp;

import game.Entity.EntityHealthAlterType;
import game.Player.Player;
import game.PowerUp.Index.PowerUp;
import game.PowerUp.Index.PowerUpIndex;
import game.PowerUp.Index.PowerUpManager;
import org.Event.EventActionID;
import org.GameObject.GameObject;

/**
 * Recovery power-up.
 *
 * When applied, this power-up restores a fixed amount of health to the
 * player's paddle. The healing amount is defined as a constant.
 */
public class Recovery extends PowerUp {

    /** The amount of health to restore when this power-up is applied. */
    private static final int HEAL_AMOUNT = 20;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The GameObject that owns this component.
     */
    public Recovery(GameObject owner) {
        super(owner);
        setPowerUpIndex(PowerUpIndex.Recovery);
    }

    /**
     * Apply the recovery effect to the player's paddle.
     * <p>
     * This method checks if the player instance and paddle exist, then
     * restores a fixed amount of health to the paddle.
     */
    private void handleOnRecoveryRequested() {
        var player = Player.getInstance();
        if (player != null) {
            var paddle = player.getPlayerPaddle();
            if (paddle != null && paddle.getPaddleHealth() != null)
                paddle.getPaddleHealth()
                        .alterHealth(EntityHealthAlterType.Regeneration, null, HEAL_AMOUNT);
        }
    }

    /**
     * Called when this power-up is destroyed.
     * <p>
     * Automatically triggers the recovery effect when the power-up is removed
     * from the game.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        handleOnRecoveryRequested();
    }

}
