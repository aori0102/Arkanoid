package game.PowerUp.Index.PowerUpPrefab;

import game.Player.PlayerPaddle;
import game.PowerUp.Index.PowerUp;
import game.PowerUp.ShieldPowerUp;
import org.GameObject.GameObjectManager;
import utils.Vector2;

public class ShieldPrefab extends PowerUpPrefab {
    @Override
    public PowerUp generatePowerUp(Vector2 position, PlayerPaddle playerPaddle) {
        var shield = GameObjectManager.instantiate("Shield").addComponent(ShieldPowerUp.class);
        shield.getTransform().setGlobalPosition(position);
        return shield;
    }
}
