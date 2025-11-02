package game.PowerUp.Index.PowerUpPrefab;

import game.Player.Player;
import game.Player.Paddle.PlayerPaddle;
import game.PowerUp.Index.PowerUp;
import game.PowerUp.Index.PowerUpManager;
import game.PowerUp.TriplicateBall;
import org.GameObject.GameObjectManager;
import utils.Vector2;

public class TriplicateBallPrefab extends PowerUpPrefab {
    @Override
    public PowerUp generatePowerUp(Vector2 position, PlayerPaddle playerPaddle) {

        var triplicateBall = GameObjectManager.instantiate("triplicateBall" + PowerUpManager.count++).
                addComponent(TriplicateBall.class);
        triplicateBall.getTransform().setGlobalPosition(position);
        triplicateBall.getTransform().setGlobalScale(new Vector2(0.5, 0.5));
        triplicateBall.linkPaddle(playerPaddle);
        triplicateBall.linkPlayerPowerUp(Player.getInstance().getPlayerPowerUpHandler());

        return triplicateBall;
    }
}
