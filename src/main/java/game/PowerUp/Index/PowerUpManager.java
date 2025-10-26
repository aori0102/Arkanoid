package game.PowerUp.Index;

import game.GameObject.Ball;
import game.Player.Player;
import game.Player.PlayerPaddle;
import game.Player.PlayerPowerUpHandler;
import game.PowerUp.*;
import org.Event.EventHandler;
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
     * Upon called, all {@link Ball} duplicate itself
     * by the multiple provided within the event argument.
     */
    public EventHandler<Integer> onDuplicateBall = new EventHandler<>(PowerUpManager.class);

    /**
     * Upon called, all {@link Ball} triplicate itself
     * by the multiple provided within the event argument.
     */
    public EventHandler<Integer> onTriplicateBall = new EventHandler<>(PowerUpManager.class);

    /**
     * Upon called, the {@code Opponent} will be attached the
     * {@code Burn} effect, which will deal damage each time in
     * a specific amount of time.
     */
    public EventHandler<StatusEffect> onFireBall = new EventHandler<>(PowerUpManager.class);

    /**
     * Upon called, the {@code Opponent} will be attached the
     * {@code FrostBite} effect, which will make the enemy takes more damage
     * when hit by ball or other damaged resources.
     */
    public EventHandler<StatusEffect> onBlizzardBall = new EventHandler<>(PowerUpManager.class);

    private PlayerPowerUpHandler playerPowerUpHandler;

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

    /**
     * Link each power up with its corresponding event
     * These events are from the {@link PlayerPowerUpHandler}
     */
    public void assignPowerUpEvent(PowerUp powerUp) {
        switch (powerUp) {
            case DuplicateBall ignored -> {
                playerPowerUpHandler.onDuplicateBallRequested.removeAllListeners();
                playerPowerUpHandler.onDuplicateBallRequested.addListener((_, number) ->
                        onDuplicateBall.invoke(this, number));
            }
            case TriplicateBall ignored -> {
                playerPowerUpHandler.onTriplicateBallRequested.removeAllListeners();
                playerPowerUpHandler.onTriplicateBallRequested.addListener((_, number) ->
                        onTriplicateBall.invoke(this, number));
            }
            case FireBall ignored -> {
                playerPowerUpHandler.onFireBallRequested.removeAllListeners();
                playerPowerUpHandler.onFireBallRequested.addListener((_, effect) ->
                        onFireBall.invoke(this, effect));
            }
            case BlizzardBall ignored -> {
                playerPowerUpHandler.onBlizzardBallRequested.removeAllListeners();
                playerPowerUpHandler.onBlizzardBallRequested.addListener((_, effect) ->
                        onBlizzardBall.invoke(this, effect));
            }
            default -> {
                throw new IllegalArgumentException("You have to add listener to a power up event");
            }
        }
    }

    public void spawnPowerUp(Vector2 position) {


        int target = 1;
        //if (Random.range(0, 1) == target) {

            var chosenKey = PowerUpPrefabGenerator.registeredPowerUps.get(
                    Random.range(0, PowerUpPrefabGenerator.registeredPowerUps.size() - 1)
            );

            PowerUp chosen = PowerUpPrefabGenerator.powerUpPrefabHashMap.get(chosenKey)
                    .generatePowerUp(position, playerPaddle);


            assignPowerUpEvent(chosen);

        //}
    }


    /**
     * Link the playerPowerUpHandler
     *
     * @param playerPowerUpHandler : the linked playerPowerUpHandler
     */
    public void linkPlayerPowerUp(PlayerPowerUpHandler playerPowerUpHandler) {
        this.playerPowerUpHandler = playerPowerUpHandler;
    }

    /**
     * <br><br>
     * <b><i><u>NOTE</u> : Only use within {@link }
     * as part of component linking process.</i></b>
     *
     * @param playerPaddle .
     */
    public void linkPlayerPaddle(PlayerPaddle playerPaddle) {
        this.playerPaddle = playerPaddle;
    }

    public static PowerUpManager getInstance() {
        return instance;
    }

    @Override
    protected void destroyComponent() {

    }

}
