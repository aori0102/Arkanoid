package game.Brick;

import game.object.Ball;
import org.*;
import utils.Vector2;

public final class Brick extends MonoBehaviour {

    private int health;
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

    /*
    - Remove this constructor
    - Add new constructor corresponding with MonoBehaviour (GameObject as argument)
    - Add new method to change brick type.
     */

    public Brick(GameObject owner){
        super(owner);
        var collider = addComponent(BoxCollider.class);
        collider.setLocalSize(new Vector2(width, height));
        collider.setOnCollisionEnterCallback(this::onCollisionEnter);

        addComponent(SpriteRenderer.class);

        owner.setLayer(Layer.Brick);
    }

    public void awake() {
        setType(BrickType.Normal);
    }

    public void update() {
    }

    private void onCollisionEnter(CollisionData data) {
        if(data.collided && data.otherCollider.getComponent(Ball.class ) != null) {
            onBrickCollision.invoke(this, null);
        }
    }

    public void setType(BrickType _brickType){
        this.maxHealth = _brickType.getMaxHealth();
        this.health = _brickType.getHealth();
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

    public void decreaseHealth(int decreaseAmount) {
        this.health -= decreaseAmount;

        if (health <= 0) {
                GameObjectManager.destroy(gameObject);
        }
    }

    public void beDestroy() {
        this.health = 0;
        this.isNewDeath = true;
    }

    public boolean isDestroyed() {
        return this.health <= 0;
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
