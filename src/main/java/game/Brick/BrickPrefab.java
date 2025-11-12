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
        var brick = brickObject.getComponent(Brick.class);

        // Visual
        instantiateVisual(brick)
                .setParent(brickObject);

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

    private GameObject instantiateVisual(Brick brick) {

        // Main object
        var brickVisualObject = GameObjectManager.instantiate("BrickVisual")
                .addComponent(BrickVisual.class)
                .addComponent(SpriteRenderer.class)
                .getGameObject();
        var brickVisual = brickVisualObject.getComponent(BrickVisual.class);

        // Renderer
        var brickRenderer = brickVisualObject.getComponent(SpriteRenderer.class);
        brickRenderer.setPivot(new Vector2(0.5, 0.5));

        // Link component
        brick.linkBrickVisual(brickVisual);
        brickVisual.linkBrick(brick);

        return brickVisualObject;

    }

}