package game.Brick;

import game.PowerUp.Index.PowerUpManager;
import game.Voltraxis.Interface.ITakePlayerDamage;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Layer.Layer;
import utils.Vector2;

public class Brick extends MonoBehaviour implements ITakePlayerDamage {

    private int health = 0;
    private BrickType brickType = BrickType.Normal;

    public EventHandler<OnBrickDestroyedEventArgs> onBrickDestroyed = new EventHandler<>(Brick.class);

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
        owner.setLayer(Layer.Brick);
    }

    @Override
    protected void onDestroy() {
        var onBrickDestroyedEventArgs = new OnBrickDestroyedEventArgs();
        onBrickDestroyedEventArgs.brickPosition = getTransform().getGlobalPosition();
        onBrickDestroyed.invoke(this, onBrickDestroyedEventArgs);
    }

    @Override
    public void takeDamage(int amount) {
        health -= amount;
        if (health <= 0) {
            PowerUpManager.getInstance().spawnPowerUp(getTransform().getGlobalPosition());
            GameObjectManager.destroy(gameObject);
        }
    }

    public void setBrickType(BrickType brickType) {
        this.brickType = brickType;
        this.health = brickType.maxHealth;
    }
}