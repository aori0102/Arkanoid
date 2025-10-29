package game.Brick;

public enum  BrickType {

    Normal(90),
    Steel(36),
    Diamond(999999);

    public final int maxHealth;
    private BrickType(int maxHealth) {
        this.maxHealth = maxHealth;
    }

}
