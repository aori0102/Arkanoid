package game.PowerUp;

import game.Player.PlayerPowerUpHandler;
import org.EventHandler;
import org.GameObject;
import org.MonoBehaviour;

import java.util.HashSet;

/**
 * Manage all the power up of the game
 */
public class PowerUpManager extends MonoBehaviour {

    public static PowerUpManager instance;

    private PlayerPowerUpHandler playerPowerUpHandler;

    /**
     * Upon called, all {@link game.object.Ball} duplicate itself
     * by the multiple provided within the event argument.
     */
    public EventHandler<Integer> onDuplicateBall = new EventHandler<Integer>(this);

    /**
     * Upon called, all {@link game.object.Ball} triplicate itself
     * by the multiple provided within the event argument.
     */
    public EventHandler<Integer> onTriplicateBall = new EventHandler<>(this);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */

    private HashSet<PowerUp> powerUpsSet = new HashSet<>();

    public PowerUpManager(GameObject owner) {
        super(owner);
        instance = this;
    }

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
                case DuplicateBall-> {
                    playerPowerUpHandler.onDuplicateBallRequested.addListener((sender, multipleNumber) -> {
                        onDuplicateBall.invoke(this, multipleNumber);
                    });
                }
                // Triplicate event
                case TriplicateBall-> {
                    playerPowerUpHandler.onTriplicateBallRequested.addListener((sender, multipleNumber) -> {
                        onTriplicateBall.invoke(this, multipleNumber);
                    });
                }
            }
        }
    }

    /**
     * Adds the power up to the powerupSet
     * @param powerUp : the added power up
     */
    public void addPowerUp(PowerUp powerUp) {
        powerUpsSet.add(powerUp);
    }


    /**
     * Removes the power up to the powerupSet
     * @param powerUp : the removed power up
     */
    public void removePowerUp(PowerUp powerUp) {
        powerUpsSet.remove(powerUp);
    }

    /**
     * Link the playerPowerUpHandler
     * @param playerPowerUpHandler : the linked playerPowerUpHandler
     */
    public void linkPlayerPowerUp(PlayerPowerUpHandler playerPowerUpHandler) {
        this.playerPowerUpHandler = playerPowerUpHandler;
        assignPowerUpEvent();
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
    }

    @Override
    protected void destroyComponent() {

    }
}
