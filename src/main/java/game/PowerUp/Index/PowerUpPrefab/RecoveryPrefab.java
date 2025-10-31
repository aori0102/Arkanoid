package game.PowerUp.Index.PowerUpPrefab;

import game.Player.PlayerPaddle;
import game.PowerUp.Index.PowerUp;
import game.PowerUp.Recovery;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import utils.Vector2;

public class RecoveryPrefab extends PowerUpPrefab{
    @Override
    public PowerUp generatePowerUp(Vector2 position, PlayerPaddle playerPaddle) {
        var recovery = GameObjectManager.instantiate("Recovery").addComponent(Recovery.class);
        recovery.getTransform().setGlobalPosition(position);

        return recovery;
    }
}
