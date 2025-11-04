package game.PowerUp.Index;

import game.Ball.BallsManager;
import game.Player.Player;
import game.Player.Paddle.PlayerPaddle;
import game.Player.PlayerPowerUpHandler;
import game.PowerUp.*;
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
            if (chosenKey.equals(DuplicateBall.class) || chosenKey.equals(TriplicateBall.class)) {
                if (BallsManager.getInstance().getBallSet().size()
                        > BallsManager.getInstance().getMaxBallExisted()) {
                    return;
                }
            }

            PowerUpPrefabGenerator.powerUpPrefabHashMap.get(chosenKey)
                    .generatePowerUp(position, playerPaddle);

        }
    }

    public static PowerUpManager getInstance() {
        return instance;
    }

}
