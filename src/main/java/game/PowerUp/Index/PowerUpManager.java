package game.PowerUp.Index;

import game.Effect.StatusEffect;
import game.Ball.Ball;
import game.GameObject.BallsManager;
import game.Player.Player;
import game.Player.Paddle.PlayerPaddle;
import game.Player.PlayerPowerUpHandler;
import game.PowerUp.*;
import org.Event.EventHandler;
import game.Player.PlayerPaddle;
import game.PowerUp.DuplicateBall;
import game.PowerUp.TriplicateBall;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import utils.Random;
import utils.Vector2;


/**
 * Manage all the power up of the game
 */
public class PowerUpManager extends MonoBehaviour {

    private static PowerUpManager instance = null;
    private PlayerPaddle playerPaddle;
    public static int count = 0;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PowerUpManager(GameObject owner) {
        super(owner);
        if (instance != null) {
            throw new IllegalStateException("PowerUpManager is a singleton!");
        }
        instance = this;
    }

    @Override
    public void awake() {
        playerPaddle = Player.getInstance().getPlayerPaddle();
    }

    @Override
    public void onDestroy() {
        instance = null;
    }


    public void spawnPowerUp(Vector2 position) {


        int target = 1;
        if (Random.range(0, 10) == target) {

            var chosenKey = PowerUpPrefabGenerator.registeredPowerUps.get(
                    Random.range(0, PowerUpPrefabGenerator.registeredPowerUps.size())
            );

            PowerUp chosen = PowerUpPrefabGenerator.powerUpPrefabHashMap.get(chosenKey)
                    .generatePowerUp(position, playerPaddle);
            if (chosenKey.equals(DuplicateBall.class) || chosenKey.equals(TriplicateBall.class)) {
                if (BallsManager.getInstance().getBallSet().size()
                        < BallsManager.getInstance().getMaxBallExisted()) {
                    return;
                }
            }

            PowerUp chosen = PowerUpPrefabGenerator.powerUpPrefabHashMap.get(chosenKey)
                    .generatePowerUp(position, playerPaddle);

        }
    }

    /**
     * Link the playerPowerUpHandler
     *
     * @param playerPowerUpHandler : the linked playerPowerUpHandler
     */
    public void linkPlayerPowerUp(PlayerPowerUpHandler playerPowerUpHandler) {
        this.playerPowerUpHandler = playerPowerUpHandler;
    }

    public static PowerUpManager getInstance() {
        return instance;
    }

}
