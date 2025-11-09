package game.PlayerData;

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
    private Integer[] perkList = null;    // TODO: Set to Perk Index list

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

    public Integer[] getPerkList() {
        return perkList;
    }

    public void setPerkList(Integer[] perkList) {
        this.perkList = perkList;
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

    public void overrideSave(ProgressData progressData) {
        this.brickDestroyed = progressData.getBrickDestroyed();
        this.level = progressData.getLevel();
        this.score = progressData.getScore();
        this.combo = progressData.getCombo();
        this.health = progressData.getHealth();
        this.lives = progressData.getLives();
        this.rank = progressData.getRank();
        this.exp = progressData.getExp();
        this.perkList = progressData.getPerkList();
    }

    public void reset() {
        this.level = 0;
        this.score = 0;
        this.combo = 0;
        this.health = 0;
        this.lives = 0;
        this.rank = 0;
        this.exp = 0;
        this.perkList = null;
        this.brickDestroyed = 0;
    }

    @Override
    public String toString() {
        return "Player Current Progress:\n"
                + "- Level : [" + level + "]\n"
                + "- Score : [" + score + "]\n"
                + "- Combo : [" + combo + "]\n"
                + "- Health : [" + health + "]\n"
                + "- Lives : [" + lives + "]\n"
                + "- Rank : [" + rank + "]\n"
                + "- Exp : [" + exp + "]\n"
                + "- Brick Destroyed : [" + brickDestroyed + "]\n"
                + "- Perk : " + Arrays.toString(perkList);
    }

}