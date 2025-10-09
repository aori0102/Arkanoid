package game.Brick;

import org.CollisionData;
import org.EventHandler;
import org.GameObject;
import org.MonoBehaviour;

public final class BrickObj extends MonoBehaviour {

    private int health;
    private final BrickType objBrickType;

    private final int maxHealth;
    private boolean isNewDeath = false;
    private boolean isDamaged = false;

    // Create a
    public EventHandler<Void> onBrickDestroyed = new EventHandler<>(this);

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
    }

    @Override
    protected void destroyComponent() {
        onBrickDestroyed.invoke(this, null);
    }

    public enum BrickType {

        Normal(50, 10, 32, 64),
        Steel(1000, 1000, 32, 64),
        Diamond(1000000000, 1000000000, 32, 64),
        Rocket(50, 10, 32, 64),
        Rock(50, 30, 32, 64),
        Bomb(50, 30, 32, 64),
        Gift(10, 10, 32, 64),
        Ball(10, 10, 32, 64),
        Reborn(10, 10, 32, 64),
        Angel(10, 10, 32, 64);


        private final int maxHealth;
        private final double width;
        private final double height;
        private final int health;

        BrickType(int _maxHealth, int _health, double _width, double _height) {
            this.maxHealth = _maxHealth;
            this.health = _health;
            this.width = _width;
            this.height = _height;
        }

        public int getMaxHealth() {
            return maxHealth;
        }

        public int getHealth() {
            return health;
        }

        public double getWidth() {
            return width;
        }

        public double getHeight() {
            return height;
        }

        public static BrickType getType(int num) {
            BrickType[] arr = BrickType.values();
            if (num < 0 || num > arr.length) num = 0;
            return arr[num];
        }
    }

    /*
    - Remove this constructor
    - Add new constructor corresponding with MonoBehaviour (GameObject as argument)
    - Add new method to change brick type.
     */
    public BrickObj(BrickType brick) {
        super();
        this.health = brick.getHealth();
        this.maxHealth = brick.getMaxHealth();
        this.objBrickType = brick;
    }

    public BrickObj(GameObject owner){
        super(owner);

        //...
    }

    public int getHealth() {
        return this.health;
    }

    public void hitDamage(int damge) {
        if (this.health < 0) {
            return;
        }

        this.health -= damge;
        this.isDamaged = true;

        if (this.health < 0) {
            this.health = 0;
            this.isNewDeath = true;
        }
    }

    public void incHealth(int inc) {
        if (this.health <= 0) {
            return;
        }

        this.health += inc;
        if (this.health > this.maxHealth) {
            this.health = this.maxHealth;
        }
    }

    public void decHealth(int inc) {
        if (this.health <= 0) {
            return;
        }

        this.health -= inc;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    public void beDestroy() {
        this.health = 0;
        this.isNewDeath = true;
    }

    public boolean isDestroyed() {
        return this.health <= 0;
    }

    public BrickType getObjType() {
        return this.objBrickType;
    }

    public boolean isObjNewDeath() {
        return this.isNewDeath;
    }

    public void setObjDeathState() {
        if (this.isNewDeath && this.isDestroyed()) {
            this.isNewDeath = false;
            return;
        }
        this.isNewDeath = true;
    }

    public boolean isJustDamaged() {
        return this.isDamaged;
    }

    public void resetIsDamaged() {
        this.isDamaged = false;
    }

    public void resetNewDeath() {
        this.isNewDeath = false;
    }

}
