package game.Player.PlayerSkills.Skills;

import game.GameObject.Shield;
import game.Player.Player;
import org.GameObject.GameObject;
import utils.Time;
import utils.Vector2;

public class Invincible extends Skill{

    private static final int SPEED_BOOSTED_MULTIPLIER = 2;
    private static final double INVINCIBLE_TIME = 5.0;
    private Time.CoroutineID invincibleCoroutineID;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public Invincible(GameObject owner) {
        super(owner);
    }

    @Override
    public void invoke() {
        Player.getInstance().getPlayerPaddle().setCanBeDamaged(false);
        Player.getInstance().getPlayerPaddle().getTransform().setGlobalScale(new Vector2(1.5, 1.5));
        Player.getInstance().setCurrentSpeed(Player.getInstance().getCurrentSpeed() * SPEED_BOOSTED_MULTIPLIER);
        Shield.getInstance().turnOn();
        invincibleCoroutineID = Time.addCoroutine(this :: turnOff, Time.getTime() + INVINCIBLE_TIME);
    }

    private void turnOff() {
        Player.getInstance().getPlayerPaddle().setCanBeDamaged(true);
        Player.getInstance().getPlayerPaddle().getTransform().setGlobalScale(new Vector2(1.25, 1.25));
        Player.getInstance().setCurrentSpeed(Player.getInstance().getBaseSpeed());
        Shield.getInstance().turnOff();
        Time.removeCoroutine(invincibleCoroutineID);
    }
}
