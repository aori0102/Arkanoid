package game.PowerUp.Index.PowerUpPrefab;

import game.Player.Paddle.PlayerPaddle;
import game.PowerUp.BlizzardBall;
import game.PowerUp.Index.PowerUp;
import org.GameObject.GameObjectManager;
import utils.Vector2;

public class BlizzardBallPrefab extends PowerUpPrefab {
    @Override
    public PowerUp generatePowerUp(Vector2 position, PlayerPaddle playerPaddle) {

        var blizzardBall = GameObjectManager.instantiate("blizzardBall").addComponent(BlizzardBall.class);
        blizzardBall.getTransform().setGlobalPosition(position);
        blizzardBall.getTransform().setGlobalScale(new Vector2(0.5, 0.5));

        return blizzardBall;

    }
}
