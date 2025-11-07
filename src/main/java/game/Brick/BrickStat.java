package game.Brick;

import game.Entity.EntityStat;
import org.Annotation.LinkViaPrefab;
import org.GameObject.GameObject;

public final class BrickStat extends EntityStat {

    private double damageTakenMultiplier = 1.0;

    @LinkViaPrefab
    private Brick brick = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public BrickStat(GameObject owner) {
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
        return 1;
    }

    @Override
    public double getDefenceMultiplier() {
        return 1;
    }

    @Override
    public double getDamageTakenMultiplier() {
        return damageTakenMultiplier;
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
        return brick.getBrickType().maxHealth;
    }

    @Override
    public double getMovementSpeed() {
        return 0;
    }

    @Override
    public double getMovementSpeedMultiplier() {
        return 0;
    }

    public void setDamageTakenMultiplier(double damageTakenMultiplier) {
        this.damageTakenMultiplier = damageTakenMultiplier;
    }

    /**
     * Link the central brick class.<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link BrickPrefab}
     * as part of component linking process.</i></b>
     *
     * @param brick The central brick class.
     */
    public void linkBrick(Brick brick) {
        this.brick = brick;
    }

}