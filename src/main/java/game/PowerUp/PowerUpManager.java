package game.PowerUp;

import game.Player.PlayerPowerUpHandler;
import org.EventHandler;
import org.GameObject;
import org.MonoBehaviour;

import java.util.HashSet;

public class PowerUpManager extends MonoBehaviour {

    public static PowerUpManager instance;

    private PlayerPowerUpHandler playerPowerUpHandler;


    public EventHandler<Integer> onDuplicateBall = new EventHandler<Integer>(this);
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


    public void assignPowerUpEvent() {
        for (var x : powerUpsSet) {
            switch (x.getPowerUpIndex()) {
                case DuplicateBall-> {
                    playerPowerUpHandler.onDuplicateBallRequested.addListener((sender, multipleNumber) -> {
                        onDuplicateBall.invoke(this, multipleNumber);
                    });
                }
                case TriplicateBall-> {
                    playerPowerUpHandler.onTriplicateBallRequested.addListener((sender, multipleNumber) -> {
                        onTriplicateBall.invoke(this, multipleNumber);
                    });
                }
            }
        }
    }


    public void addPowerUp(PowerUp powerUp) {
        powerUpsSet.add(powerUp);
    }

    public void removePowerUp(PowerUp powerUp) {
        powerUpsSet.remove(powerUp);
    }

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
