package game.Brick;

import game.Voltraxis.Interface.IBossTarget;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;

public class Brick extends MonoBehaviour implements IBossTarget {

    private int health = 0;
    private BrickType brickType = BrickType.Normal;

    public EventHandler<Void> onBrickDestroyed = new EventHandler<>(this);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public Brick(GameObject owner) {
        super(owner);
        setBrickType(BrickType.Normal);
    }

    @Override
    protected void destroyComponent() {
        onBrickDestroyed.invoke(this, null);
    }

    @Override
    public void takeDamage(int amount) {
        health -= amount;
        if (health <= 0) {
            GameObjectManager.destroy(gameObject);
        }
    }

    public void setBrickType(BrickType brickType) {
        this.brickType = brickType;
        this.health = brickType.maxHealth;
    }
}