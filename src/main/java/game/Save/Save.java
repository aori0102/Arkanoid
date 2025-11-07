package game.Save;

public final class Save {

    private int level = 0;
    private int score = 0;
    private int combo = 0;
    private int health = 0;
    private int lives = 0;
    private int rank = 0;
    private int exp = 0;
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

}
