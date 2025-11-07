package game.Brick;

import game.Damagable.HealthChangeVisualizer;
import game.Rank.ExperienceHolder;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Layer.Layer;
import org.Physics.BoxCollider;
import org.Prefab.Prefab;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

public final class BrickPrefab extends Prefab {

    public static final Vector2 BRICK_SIZE = new Vector2(64.0, 32.0);

    @Override
    public GameObject instantiatePrefab() {

        // Setup core object
        var brickObject = GameObjectManager.instantiate("Brick")
                .addComponent(BrickHealth.class)
                .addComponent(Brick.class)
                .addComponent(ExperienceHolder.class)
                .addComponent(BrickEffectController.class)
                .addComponent(BrickStat.class)
                .getGameObject();
        brickObject.setLayer(Layer.Brick);

        // Brick component
        var brick = brickObject.getComponent(Brick.class);

        // Setup visual and ready for rendering object
        var brickRenderer = brickObject.addComponent(SpriteRenderer.class);
        brickRenderer.setImage(ImageAsset.ImageIndex.BrickNormal.getImage());
        brickRenderer.setSize(BRICK_SIZE);
        brickRenderer.setPivot(new Vector2(0.5, 0.5));

        // Health component
        brickObject.getComponent(BrickHealth.class)
                .linkBrick(brickObject.getComponent(Brick.class));

        // Collider
        brickObject.addComponent(BoxCollider.class).setLocalSize(BRICK_SIZE);

        // Health visualizer
        var healthVisualizer = PrefabManager.instantiatePrefab(PrefabIndex.HealthChange_VisualizeHandler)
                .getComponent(HealthChangeVisualizer.class);
        healthVisualizer.linkEntityHealth(brickObject.getComponent(BrickHealth.class));
        healthVisualizer.getGameObject().setParent(brickObject);

        // Link component
        brickObject.getComponent(BrickStat.class).linkBrick(brick);

        return brickObject;

    }

}