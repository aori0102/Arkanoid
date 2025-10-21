package game.Brick;

public enum BrickType {

    Normal(18),
    Steel(36),
    Diamond(999999);

    public final int maxHealth;
    private BrickType(int maxHealth) {
        this.maxHealth = maxHealth;
    }

}
