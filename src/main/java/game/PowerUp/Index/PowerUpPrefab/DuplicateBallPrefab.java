package game.PowerUp.Index.PowerUpPrefab;

import game.Player.Player;
import game.Player.PlayerPaddle;
import game.PowerUp.DuplicateBall;
import game.PowerUp.Index.PowerUp;
import game.PowerUp.Index.PowerUpManager;
import org.GameObject.GameObjectManager;
import utils.Vector2;

public class DuplicateBallPrefab extends PowerUpPrefab {
    @Override
    public PowerUp generatePowerUp(Vector2 position, PlayerPaddle playerPaddle) {

        var duplicateBall = GameObjectManager.instantiate("duplicateBall" + PowerUpManager.count++).
                addComponent(DuplicateBall.class);
        duplicateBall.getTransform().setGlobalPosition(position);
        duplicateBall.getTransform().setGlobalScale(new Vector2(0.5, 0.5));
        duplicateBall.linkPaddle(playerPaddle);
        duplicateBall.linkPlayerPowerUp(Player.getInstance().getPlayerPowerUpHandler());

        return duplicateBall;
    }
}
