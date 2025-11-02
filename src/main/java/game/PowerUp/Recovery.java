package game.PowerUp;

import game.Player.Player;
import game.PowerUp.Index.PowerUp;
import game.PowerUp.Index.PowerUpIndex;
import game.PowerUp.Index.PowerUpManager;
import org.Event.EventActionID;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;

public class Recovery extends PowerUp{
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
    }

    private void handleOnRecoveryRequested() {
        Player.getInstance().getPlayerHealth().heal();
    }

    @Override
    protected void onDestroy() {
        handleOnRecoveryRequested();
    }
}
