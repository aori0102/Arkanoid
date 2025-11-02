package game.PowerUp.Index;

import game.Player.Player;
import game.Player.PlayerPaddle;
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


    public void spawnPowerUp(Vector2 position) {


        int target = 1;
       if (Random.range(0, 10) == target) {

            var chosenKey = PowerUpPrefabGenerator.registeredPowerUps.get(
                    Random.range(0, PowerUpPrefabGenerator.registeredPowerUps.size())
            );

            PowerUp chosen = PowerUpPrefabGenerator.powerUpPrefabHashMap.get(chosenKey)
                    .generatePowerUp(position, playerPaddle);

        }
    }

    public static PowerUpManager getInstance() {
        return instance;
    }

}
