package game.Brick;

import game.PowerUp.Index.PowerUpManager;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Layer.Layer;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

public class Brick extends MonoBehaviour {

    private int health = 0;
    private BrickType brickType = BrickType.Normal;
    private SpriteRenderer spriteRenderer = null;

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
        spriteRenderer = addComponent(SpriteRenderer.class);
        owner.setLayer(Layer.Brick);
    }

    public BrickType getBrickType() {
        return brickType;
    }

    @Override
    protected void onDestroy() {
        var onBrickDestroyedEventArgs = new OnBrickDestroyedEventArgs();
        onBrickDestroyedEventArgs.brickPosition = getTransform().getGlobalPosition();
        onBrickDestroyed.invoke(this, onBrickDestroyedEventArgs);
    }

    public void damage(int amount) {
        health -= amount;
        if (health <= 0) {
            PowerUpManager.getInstance().spawnPowerUp(getTransform().getGlobalPosition());
            GameObjectManager.destroy(gameObject);
        }
    }

    public void setBrickType(BrickType brickType) {
        this.brickType = brickType;
        this.health = brickType.maxHealth;
        spriteRenderer.setImage(brickType.imageIndex.getImage());
        spriteRenderer.setSize(BrickPrefab.BRICK_SIZE);
    }
}