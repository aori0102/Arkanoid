package game.Brick;

import game.Voltraxis.Interface.ITakeBallDamage;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import utils.Vector2;

public class Brick extends MonoBehaviour implements ITakeBallDamage {

    private int health = 0;
    private BrickType brickType = BrickType.Normal;

    public EventHandler<OnBrickDestroyedEventArgs> onBrickDestroyed = new EventHandler<>(this);

    public static class OnBrickDestroyedEventArgs {
        public Vector2 brickPosition;
    }

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
        var onBrickDestroyedEventArgs = new OnBrickDestroyedEventArgs();
        onBrickDestroyedEventArgs.brickPosition = getTransform().getGlobalPosition();
        onBrickDestroyed.invoke(this, onBrickDestroyedEventArgs);
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