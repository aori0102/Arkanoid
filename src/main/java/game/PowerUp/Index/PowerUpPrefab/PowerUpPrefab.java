package game.PowerUp.Index.PowerUpPrefab;

import game.Player.PlayerPaddle;
import game.PowerUp.Index.PowerUp;
import utils.Vector2;

public abstract class PowerUpPrefab {
    public abstract PowerUp generatePowerUp(Vector2 position, PlayerPaddle playerPaddle);
}
