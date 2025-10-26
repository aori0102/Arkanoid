package game.Player.Prefab;

import game.Player.PlayerHealthLossVignette;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Main;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

/**
 * Prefab of the vignette visual effect upon player taking damage. Attached
 * with {@link PlayerHealthLossVignette} controlling the visual effect.
 */
public class PlayerHealthLossVignettePrefab implements IPlayerPrefab {

    @Override
    public GameObject instantiatePrefab() {

        var vignette = GameObjectManager.instantiate("HealthLossVignette")
                .addComponent(PlayerHealthLossVignette.class);

        var vignetteRenderer = GameObjectManager.instantiate("VisualEffect")
                .addComponent(SpriteRenderer.class);
        vignetteRenderer.setImage(ImageAsset.ImageIndex.HealthLossVignette.getImage());
        vignetteRenderer.setPivot(new Vector2(0.5, 0.5));
        vignetteRenderer.getTransform().setGlobalPosition(
                new Vector2(Main.STAGE_WIDTH / 2, Main.STAGE_HEIGHT / 2)
        );
        vignetteRenderer.getGameObject().setParent(vignette.getGameObject());

        vignette.linkVignetteRenderer(vignetteRenderer);

        return vignette.getGameObject();

    }

}