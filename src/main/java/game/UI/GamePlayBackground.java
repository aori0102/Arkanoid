package game.UI;

import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Layer.RenderLayer;
import org.Main;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

public class GamePlayBackground extends MonoBehaviour {

    private SpriteRenderer spriteRenderer;
    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public GamePlayBackground(GameObject owner) {
        super(owner);

        spriteRenderer = owner.addComponent(SpriteRenderer.class);
        spriteRenderer.setImage(ImageAsset.ImageIndex.GamePlayBackground_Normal.getImage());
        spriteRenderer.setPivot(new Vector2(0.5,0.5));
        spriteRenderer.setRenderLayer(RenderLayer._2);
        owner.getTransform().setGlobalPosition(new Vector2((Main.STAGE_WIDTH - 250) / 2, Main.STAGE_HEIGHT /2));
    }
}
