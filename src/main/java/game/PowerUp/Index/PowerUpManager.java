package game.PowerUp.Index;

import game.Ball.BallsManager;
import game.Level.LevelManager;
import game.Level.LevelType;
import org.Event.EventActionID;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Prefab.PrefabManager;
import utils.Random;
import utils.Vector2;

/**
 * PowerUpManager is responsible for managing all PowerUps in the game.
 * - Spawns new power-ups.
 * - Keeps track of the number of active power-ups for each type.
 * - Handles events when power-ups are destroyed.
 * This class is implemented as a singleton.
 */
public class PowerUpManager extends MonoBehaviour {

    private static final double NORMAL_SPAWN_RATE = 0.36;
    private static final double FRENZY_SPAWN_RATE = 0.7;

    // Singleton instance
    private static PowerUpManager instance = null;

    // Keeps track of how many instances of each power-up type are active
    private final Integer[] assignedPowerUp;

    // Event listener ID for global power-up destruction event
    private EventActionID powerUp_onAnyPowerUpDestroyed_ID = null;

    /**
     * Constructor for PowerUpManager.
     *
     * @param owner The GameObject that owns this component.
     * @throws ReinitializedSingletonException If a PowerUpManager instance already exists.
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

    /**
     * Called when the component is awakened.
     * Registers a listener for when any power-up is destroyed.
     */
    @Override
    public void awake() {
        powerUp_onAnyPowerUpDestroyed_ID = PowerUp.onAnyPowerUpDestroyed
                .addListener(this::powerUp_onAnyPowerUpDestroyed);
    }

    /**
     * Called when the component is destroyed.
     * - Removes the singleton instance.
     * - Unregisters the global power-up destroyed listener.
     */
    @Override
    public void onDestroy() {
        instance = null;
        PowerUp.onAnyPowerUpDestroyed.removeListener(powerUp_onAnyPowerUpDestroyed_ID);
    }

    /**
     * Event handler for when any power-up is destroyed.
     * Decrements the count of the destroyed power-up type.
     *
     * @param sender The PowerUp that was destroyed.
     * @param e      Empty event argument.
     */
    private void powerUp_onAnyPowerUpDestroyed(Object sender, Void e) {
        if (sender instanceof PowerUp powerUp) {
            assignedPowerUp[powerUp.getPowerUpIndex().ordinal()]--;
        }
    }

    /**
     * Spawns a new power-up at the specified position.
     * - Randomly selects a PowerUpIndex.
     * - Checks for maximum concurrent limit.
     * - Prevents spawning DuplicateBall/TriplicateBall if not allowed by BallsManager.
     *
     * @param position The world position to spawn the power-up at.
     */
    public void spawnPowerUp(Vector2 position) {
        var powerUpIndexArray = PowerUpIndex.values();
        if (Random.range(0.0, 1.0) < getSpawnRate()) {  // Random chance to spawn

            var chosenKey = powerUpIndexArray[Random.range(0, powerUpIndexArray.length)];

            // Check if ball multiplication power-ups can spawn
            if (chosenKey == PowerUpIndex.DuplicateBall || chosenKey == PowerUpIndex.TriplicateBall) {
                if (!BallsManager.getInstance().canSpawnBallMultiplication()) {
                    return;
                }
            }

            // Spawn if below max concurrent limit
            if (assignedPowerUp[chosenKey.ordinal()] < chosenKey.maxCocurrent) {
                PrefabManager.instantiatePrefab(chosenKey.prefabIndex)
                        .getTransform().setGlobalPosition(position);
                assignedPowerUp[chosenKey.ordinal()]++;
            }

        }
    }

    /**
     * Returns the singleton instance of PowerUpManager.
     *
     * @return PowerUpManager instance.
     */
    public static PowerUpManager getInstance() {
        return instance;
    }

    private double getSpawnRate() {
        return LevelManager.getInstance().getCurrentLevelType() == LevelType.Frenzy
                ? FRENZY_SPAWN_RATE
                : NORMAL_SPAWN_RATE;
    }

}
