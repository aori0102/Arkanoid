package game.BrickObj;

public enum BrickType {

    Normal(50, 10),
    Steel(100, 50),
    Diamond(1000000000, 1000000000),
    Rocket(50, 10),
    Rock(50, 30),
    Bomb(50, 30),
    Gift(10, 10),
    Reborn(10, 10),
    Angel(10, 10);


    private final int maxHealth;
    private final int health;

    BrickType(int _maxHealth, int _health) {
        this.maxHealth = _maxHealth;
        this.health = _health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getHealth() {
        return health;
    }
}
