package game.Player.Paddle;

import game.Entity.EntityStat;
import game.Player.PlayerData;
import org.GameObject.GameObject;

public final class PaddleStat extends EntityStat {

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PaddleStat(GameObject owner) {
        super(owner);
    }

    @Override
    public int getAttack() {
        return 0;
    }

    @Override
    public int getDefence() {
        return 0;
    }

    @Override
    public double getAttackMultiplier() {
        return 0;
    }

    @Override
    public double getDefenceMultiplier() {
        return 0;
    }

    @Override
    public double getDamageTakenMultiplier() {
        return 1;
    }

    @Override
    public double getRegenerationMultiplier() {
        return 1;
    }

    @Override
    public double getCriticalChange() {
        return 0;
    }

    @Override
    public double getCriticalDamage() {
        return 0;
    }

    @Override
    public int getMaxHealth() {
        return PlayerData.MAX_HEALTH;
    }

    @Override
    public double getMovementSpeed() {
        return 0;
    }

    @Override
    public double getMovementSpeedMultiplier() {
        return 0;
    }

}
