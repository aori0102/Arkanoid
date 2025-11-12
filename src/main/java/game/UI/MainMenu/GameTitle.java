package game.UI.MainMenu;

import game.UI.Title;
import org.GameObject.GameObject;
import org.Layer.RenderLayer;
import org.Main;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

/**
 * Represents the main title screen graphic for the game.
 * <p>
 * This class inherits animation and idle behavior from {@link Title} and specializes
 * its appearance and initial positioning.
 */
public class GameTitle extends Title {


    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public GameTitle(GameObject owner) {
        super(owner);

        SpriteRenderer spriteRenderer = owner.addComponent(SpriteRenderer.class);
        spriteRenderer.setImage(ImageAsset.ImageIndex.GameTitle.getImage());
        spriteRenderer.getTransform().setGlobalScale(new Vector2(0.5, 0.5));
        spriteRenderer.setPivot(new Vector2(0.5, 0.5));
        spriteRenderer.setRenderLayer(RenderLayer.UI_3);

        getTransform().setGlobalPosition(new Vector2(Main.STAGE_WIDTH / 2, -TWEEN_DISTANCE / 2));
    }


}
