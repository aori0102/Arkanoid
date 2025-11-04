package game.PowerUp;

import game.Entity.EntityHealthAlterType;
import game.Player.Player;
import game.PowerUp.Index.PowerUp;
import game.PowerUp.Index.PowerUpIndex;
import game.PowerUp.Index.PowerUpManager;
import org.Event.EventActionID;
import org.GameObject.GameObject;

public class Recovery extends PowerUp {
    private EventActionID recoveryEventActionID = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public Recovery(GameObject owner) {
        super(owner);
        setPowerUpIndex(PowerUpIndex.Recovery);
    }

    public void awake() {
        recoveryEventActionID = PowerUpManager.getInstance().onRecovery.addListener(this::handleOnRecoveryRequested);
    }

    private void handleOnRecoveryRequested(Object o, int healAmount) {
        Player.getInstance().getPlayerPaddle().getPaddleHealth()
                .alterHealth(EntityHealthAlterType.Regeneration, healAmount);
    }

    @Override
    protected void onDestroy() {
        handleOnRecoveryRequested();
    }
}
