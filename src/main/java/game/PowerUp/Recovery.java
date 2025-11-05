package game.PowerUp;

import game.Entity.EntityHealthAlterType;
import game.Player.Player;
import game.PowerUp.Index.PowerUp;
import game.PowerUp.Index.PowerUpIndex;
import game.PowerUp.Index.PowerUpManager;
import org.Event.EventActionID;
import org.GameObject.GameObject;

public class Recovery extends PowerUp {

    private static final int HEAL_AMOUNT = 20;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public Recovery(GameObject owner) {
        super(owner);
        setPowerUpIndex(PowerUpIndex.Recovery);
    }

    private void handleOnRecoveryRequested() {
        Player.getInstance().getPlayerPaddle().getPaddleHealth()
                .alterHealth(EntityHealthAlterType.Regeneration, HEAL_AMOUNT);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handleOnRecoveryRequested();
    }

}