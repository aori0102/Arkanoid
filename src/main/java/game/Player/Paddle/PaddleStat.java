package game.Player.Paddle;

import game.Entity.EntityStat;
import game.Player.PlayerData;
import org.GameObject.GameObject;

public final class PaddleStat extends EntityStat {
    private int attack = 20;
    private int defence = 20;
    private double attackMultiplier = 1;
    private double defenceMultiplier = 1;
    private double damageTakenMultiplier = 1;
    private double regenerationMultiplier = 1;
    private double criticalChance = 0.1;
    private double criticalDamage = 1.5;
    private int maxHealth = 100;
    private double movementSpeed = 800;
    private double movementSpeedMultiplier = 1;
    private double laserBeamCooldown = 5;
    private double dashCooldown = 0.2;
    private double updraftCooldown = 5;
    private double invincibleCooldown = 5;


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
        return attack;
    }

    @Override
    public int getDefence() {
        return defence;
    }

    @Override
    public double getAttackMultiplier() {
        return attackMultiplier;
    }

    @Override
    public double getDefenceMultiplier() {
        return defenceMultiplier;
    }

    @Override
    public double getDamageTakenMultiplier() {
        return damageTakenMultiplier;
    }

    @Override
    public double getRegenerationMultiplier() {
        return regenerationMultiplier;
    }

    @Override
    public double getCriticalChance() {
        return criticalChance;
    }

    @Override
    public double getCriticalDamage() {
        return criticalDamage;
    }

    @Override
    public int getMaxHealth() {
        return maxHealth;
    }

    @Override
    public double getMovementSpeed() {
        return movementSpeed;
    }

    @Override
    public double getMovementSpeedMultiplier() {
        return movementSpeedMultiplier;
    }

    public void setAttack(int value) {
        attack = value;
    }

    public void setDefence(int value) {
        defence = value;
    }

    public void setDamageTakenMultiplier(double value) {
        damageTakenMultiplier = value;
    }

    public void setRegenerationMultiplier(double value) {
        regenerationMultiplier = value;
    }

    public void setCriticalChance(double value) {
        criticalChance = value;
    }

    public void setCriticalDamage(double value) {
        criticalDamage = value;
    }

    public void setMaxHealth(int value) {
        maxHealth = value;
    }

    public void setMovementSpeed(double value) {
        movementSpeed = value;
    }

    public void setMovementSpeedMultiplier(double value) {
        movementSpeedMultiplier = value;
    }

    public void setAttackMultiplier(double value) {
        attackMultiplier = value;
    }

    public void setDefenceMultiplier(double value) {
        defenceMultiplier = value;
    }

    public double getLaserBeamCooldown() {
        return laserBeamCooldown;
    }

    public void setLaserBeamCooldown(double laserBeamCooldown) {
        this.laserBeamCooldown = laserBeamCooldown;
    }

    public double getDashCooldown() {
        return dashCooldown;
    }

    public void setDashCooldown(double dashCooldown) {
        this.dashCooldown = dashCooldown;
    }

    public double getUpdraftCooldown() {
        return updraftCooldown;
    }

    public void setUpdraftCooldown(double updraftCooldown) {
        this.updraftCooldown = updraftCooldown;
    }

    public double getInvincibleCooldown() {
        return invincibleCooldown;
    }

    public void setInvincibleCooldown(double invincibleCooldown) {
        this.invincibleCooldown = invincibleCooldown;
    }
}
