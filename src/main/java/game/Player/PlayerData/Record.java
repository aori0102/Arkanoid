package game.Player.PlayerData;

public final class Record {

    private final int levelCleared;
    private final int brickDestroyed;
    private final int score;
    private final int rank;

    public Record(ProgressData progressData) {
        levelCleared = progressData.getLevel();
        brickDestroyed = progressData.getBrickDestroyed();
        score = progressData.getScore();
        rank = progressData.getRank();
    }

    public int getLevelCleared() {
        return levelCleared;
    }

    public int getBrickDestroyed() {
        return brickDestroyed;
    }

    public int getScore() {
        return score;
    }

    public int getRank() {
        return rank;
    }

    @Override
    public String toString() {
        return "- Level Cleared : [" + levelCleared + "]\n"
                + "- Brick Destroyed : [" + brickDestroyed + "]\n"
                + "- Score : [" + score + "]\n"
                + "- Rank : [" + rank + "]";
    }

}