package game.PowerUp.Index.PowerUpPrefab;

import game.Player.Paddle.PlayerPaddle;
import game.PowerUp.FireBall;
import game.PowerUp.Index.PowerUp;
import org.GameObject.GameObjectManager;
import utils.Vector2;

public class FireBallPrefab extends PowerUpPrefab {

    @Override
    public PowerUp generatePowerUp(Vector2 position, PlayerPaddle playerPaddle) {

        var fireBall = GameObjectManager.instantiate("fireBall").addComponent(FireBall.class);
        fireBall.getTransform().setGlobalPosition(position);
        fireBall.getTransform().setGlobalScale(new Vector2(0.5, 0.5));

        return fireBall;

    }
}
