package game.Brick;

import game.object.Ball;
import org.*;
import utils.Vector2;

public final class BrickObj extends MonoBehaviour {

    private int health;
    private BrickType objBrickType;
    private int rowID = 0, colID = 0;

    private int maxHealth;
    private boolean isNewDeath = false;
    private boolean isDamaged = false;

    private final int width = 64;
    private final int height = 32;

    // Create a
    public EventHandler<Void> onBrickDestroyed = new EventHandler<>(this);
    public EventHandler<Void> onBrickCollision = new EventHandler<>(this);

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
    }

    @Override
    protected void destroyComponent() {
        onBrickDestroyed.invoke(this, null);
    }

    public enum BrickType {

        Normal(50, 10),
        Steel(1000, 1000),
        Diamond(1000000000, 1000000000),
        Rocket(50, 10),
        Rock(50, 30),
        Bomb(50, 30),
        Gift(10, 10),
        Ball(10, 10),
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

    public BrickObj(GameObject owner){
        super(owner);
        var collider = addComponent(BoxCollider.class);
        collider.setLocalSize(new Vector2(width, height));
        collider.setOnCollisionEnterCallback(this::onCollisionEnter);
    }

    private void onCollisionEnter(CollisionData data) {
        if(data.collided && data.otherCollider.getComponent(Ball.class ) != null) {
            onBrickCollision.invoke(this, null);
        }
    }

    public void setType(BrickObj.BrickType _brickType){
        this.maxHealth = _brickType.getMaxHealth();
        this.health = _brickType.getHealth();
        this.objBrickType = _brickType;
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

    public void setRowId(int id) {
        this.rowID = id;
    }

    public void setColID(int id) {
        this.colID = id;
    }

    public int getRowID() {
        return this.rowID;
    }

    public int getColID() {
        return this.colID;
    }
}
