package game.UI.MainMenu;

import game.UI.Title;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Layer.RenderLayer;
import org.Main;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.Random;
import utils.Time;
import utils.UITween.Ease;
import utils.UITween.Tween;
import utils.Vector2;

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
        spriteRenderer.getTransform().setGlobalScale(new Vector2(0.5,0.5));
        spriteRenderer.setPivot(new Vector2(0.5, 0.5));
        spriteRenderer.setRenderLayer(RenderLayer.UI_Middle);

        getTransform().setGlobalPosition(new Vector2(Main.STAGE_WIDTH/2, - TWEEN_DISTANCE/2 ));
    }


}
