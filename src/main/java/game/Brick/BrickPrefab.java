package game.Brick;

import org.GameObject.GameObjectManager;
import org.Physics.BoxCollider;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

public final class BrickPrefab {

    public static final Vector2 BRICK_SIZE = new Vector2(64.0, 32.0);

    public static Brick instantiateBrick(){

        // Core object
        var brickObject = GameObjectManager.instantiate("Brick")
                .addComponent(BrickDamageAcceptor.class)
                .addComponent(Brick.class)
                .getGameObject();

        // Damage visualizer

        // Renderer
        var brickRenderer = brickObject.addComponent(SpriteRenderer.class);
        brickRenderer.setImage(ImageAsset.ImageIndex.GreenBrick.getImage());
        brickRenderer.setSize(BRICK_SIZE);
        brickRenderer.setPivot(new Vector2(0.5, 0.5));
        brickObject.addComponent(BoxCollider.class).setLocalSize(BRICK_SIZE);

        return brickObject.getComponent(Brick.class);

    }

}