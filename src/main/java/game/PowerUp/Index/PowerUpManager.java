package game.PowerUp.Index;

import game.Ball.BallsManager;
import game.PowerUp.*;
import org.Event.EventActionID;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Prefab.PrefabManager;
import utils.Random;
import utils.Vector2;


/**
 * Manage all the power up of the game
 */
public class PowerUpManager extends MonoBehaviour {

    private static PowerUpManager instance = null;

    private final Integer[] assignedPowerUp;

    private EventActionID powerUp_onAnyPowerUpDestroyed_ID = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PowerUpManager(GameObject owner) {
        super(owner);
        if (instance != null) {
            throw new ReinitializedSingletonException("PowerUpManager is a singleton!");
        }
        instance = this;

        var powerUpCount = PowerUpIndex.values().length;
        assignedPowerUp = new Integer[powerUpCount];
        for (int i = 0; i < powerUpCount; i++) {
            assignedPowerUp[i] = 0;
        }

    }

    @Override
    public void awake() {
        powerUp_onAnyPowerUpDestroyed_ID = PowerUp.onAnyPowerUpDestroyed
                .addListener(this::powerUp_onAnyPowerUpDestroyed);
    }

    @Override
    public void onDestroy() {
        instance = null;
        PowerUp.onAnyPowerUpDestroyed.removeListener(powerUp_onAnyPowerUpDestroyed_ID);
    }

    /**
     * Called when {@link PowerUp#onAnyPowerUpDestroyed} is invoked.<br><br>
     * This function removes a power up as it's destroyed.
     *
     * @param sender Event caller {@link PowerUp}.
     * @param e      Empty event argument.
     */
    private void powerUp_onAnyPowerUpDestroyed(Object sender, Void e) {
        if (sender instanceof PowerUp powerUp) {
            assignedPowerUp[powerUp.getPowerUpIndex().ordinal()]--;
        }
    }

    public void spawnPowerUp(Vector2 position) {

        var powerUpIndexArray = PowerUpIndex.values();
        int target = 0;
        if (Random.range(0, 1) == target) {

            var chosenKey = powerUpIndexArray[Random.range(0, powerUpIndexArray.length)];
            if (chosenKey == PowerUpIndex.DuplicateBall || chosenKey == PowerUpIndex.TriplicateBall) {
                if (!BallsManager.getInstance().canSpawnBallMultiplication()) {
                    return;
                }
            }
            if (assignedPowerUp[chosenKey.ordinal()] < chosenKey.maxCocurrent) {
                PrefabManager.instantiatePrefab(chosenKey.prefabIndex)
                        .getTransform().setGlobalPosition(position);
                assignedPowerUp[chosenKey.ordinal()]++;
            }

        }
    }

    public static PowerUpManager getInstance() {
        return instance;
    }

}