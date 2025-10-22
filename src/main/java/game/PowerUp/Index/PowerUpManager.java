package game.PowerUp.Index;

import game.Player.PlayerPowerUpHandler;
import game.PowerUp.StatusEffect;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

import java.util.HashSet;

/**
 * Manage all the power up of the game
 */
public class PowerUpManager extends MonoBehaviour {

    public static PowerUpManager instance = null;

    /**
     * Upon called, all {@link game.GameObject.Ball} duplicate itself
     * by the multiple provided within the event argument.
     */
    public EventHandler<Integer> onDuplicateBall = new EventHandler<Integer>(PowerUpManager.class);

    /**
     * Upon called, all {@link game.GameObject.Ball} triplicate itself
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

    public EventHandler<Void> onLaserBeam = new EventHandler<>(PowerUpManager.class);

    private HashSet<PowerUp> powerUpsSet = new HashSet<>();
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

    @Override
    public void awake() {
        assignPowerUpEvent();
    }

    /**
     * Link each power up with its corresponding event
     * These events are from the {@link game.Player.PlayerPowerUpHandler}
     */
    public void assignPowerUpEvent() {
        for (var x : powerUpsSet) {
            switch (x.getPowerUpIndex()) {
                // Duplicate event
                case DuplicateBall -> {
                    playerPowerUpHandler.onDuplicateBallRequested.addListener((sender, multipleNumber) -> {
                        onDuplicateBall.invoke(this, multipleNumber);
                    });
                }
                // Triplicate event
                case TriplicateBall -> {
                    playerPowerUpHandler.onTriplicateBallRequested.addListener((sender, multipleNumber) -> {
                        onTriplicateBall.invoke(this, multipleNumber);
                    });
                }

                case FireBall -> {
                     playerPowerUpHandler.onFireBallRequested.addListener((sender, powerEffect) -> {
                         onFireBall.invoke(this, powerEffect);
                     });
                }

                case Blizzard -> {
                    playerPowerUpHandler.onBlizzardBallRequested.addListener((sender, powerEffect) -> {
                        onBlizzardBall.invoke(this, powerEffect);
                    });
                }

                case  LaserBeam -> {

                }
            }
        }
    }

    /**
     * Adds the power up to the powerupSet
     *
     * @param powerUp : the added power up
     */
    public void addPowerUp(PowerUp powerUp) {
        powerUpsSet.add(powerUp);
    }

    /**
     * Removes the power up to the powerupSet
     *
     * @param powerUp : the removed power up
     */
    public void removePowerUp(PowerUp powerUp) {
        powerUpsSet.remove(powerUp);
    }

    /**
     * Link the playerPowerUpHandler
     *
     * @param playerPowerUpHandler : the linked playerPowerUpHandler
     */
    public void linkPlayerPowerUp(PlayerPowerUpHandler playerPowerUpHandler) {
        this.playerPowerUpHandler = playerPowerUpHandler;
        assignPowerUpEvent();
    }

    @Override
    protected void destroyComponent() {

    }

    public PowerUp getPowerUp(PowerUpIndex powerUpIndex) {
        // spawn
        return null;
    }

}
