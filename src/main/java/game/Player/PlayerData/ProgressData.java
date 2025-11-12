package game.Player.PlayerData;

import game.Player.Paddle.PlayerStat;
import game.Player.PlayerAttributes;
import game.Player.PlayerSkills.SkillIndex;

import java.util.Arrays;

public final class ProgressData {

    private int level = 0;
    private int score = 0;
    private int combo = 0;
    private int health = 0;
    private int lives = 0;
    private int rank = 0;
    private int exp = 0;
    private int brickDestroyed = 0;
    private double[] statMultiplierArray = new double[PlayerStat.PlayerStatIndex.values().length];
    private double[] skillCooldownMultiplierArray = new double[SkillIndex.values().length];

    public ProgressData() {
        toDefault();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCombo() {
        return combo;
    }

    public void setCombo(int combo) {
        this.combo = combo;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getBrickDestroyed() {
        return brickDestroyed;
    }

    public void setBrickDestroyed(int brickDestroyed) {
        this.brickDestroyed = brickDestroyed;
    }

    public double[] getStatMultiplierArray() {
        return statMultiplierArray;
    }

    public void setStatMultiplierArray(double[] statMultiplierArray) {
        this.statMultiplierArray = statMultiplierArray;
    }

    public double[] getSkillCooldownMultiplierArray() {
        return skillCooldownMultiplierArray;
    }

    public void setSkillCooldownMultiplierArray(double[] skillCooldownMultiplierArray) {
        this.skillCooldownMultiplierArray = skillCooldownMultiplierArray;
    }

    public void overrideSave(ProgressData progressData) {
        this.brickDestroyed = progressData.getBrickDestroyed();
        this.level = progressData.getLevel();
        this.score = progressData.getScore();
        this.combo = progressData.getCombo();
        this.health = progressData.getHealth();
        this.lives = progressData.getLives();
        this.rank = progressData.getRank();
        this.exp = progressData.getExp();
        this.skillCooldownMultiplierArray = progressData.getSkillCooldownMultiplierArray();
        this.statMultiplierArray = progressData.getStatMultiplierArray();
    }

    public void toDefault() {
        this.level = 0;
        this.score = 0;
        this.combo = 0;
        this.health = PlayerAttributes.MAX_HEALTH;
        this.lives = PlayerAttributes.MAX_LIVES;
        this.rank = 0;
        this.exp = 0;
        this.brickDestroyed = 0;
        Arrays.fill(skillCooldownMultiplierArray, 1.0);
        Arrays.fill(statMultiplierArray, 1.0);
    }

    @Override
    public String toString() {
        StringBuilder info = new StringBuilder("Player Current Progress:\n"
                + "- Level : [" + level + "]\n"
                + "- Score : [" + score + "]\n"
                + "- Combo : [" + combo + "]\n"
                + "- Health : [" + health + "]\n"
                + "- Lives : [" + lives + "]\n"
                + "- Rank : [" + rank + "]\n"
                + "- Exp : [" + exp + "]\n"
                + "- Brick Destroyed : [" + brickDestroyed + "]\n"
                + "- Stat Multiplier : \n");
        for (var index : PlayerStat.PlayerStatIndex.values()) {
            info.append("\t- ")
                    .append(index)
                    .append(" : [")
                    .append(statMultiplierArray[index.ordinal()])
                    .append("]\n");
        }
        info.append("- Skill Cooldown Multiplier : \n");
        for (var index : SkillIndex.values()) {
            info.append("\t- ")
                    .append(index)
                    .append(" : [")
                    .append(skillCooldownMultiplierArray[index.ordinal()])
                    .append("]\n");
        }

        return info.toString();
    }

}