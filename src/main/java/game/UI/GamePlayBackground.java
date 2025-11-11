package game.UI;

import game.Level.LevelManager;
import game.Level.LevelType;
import org.Event.EventActionID;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Layer.RenderLayer;
import org.Main;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.UITween.Tween;
import utils.Vector2;

import java.util.logging.Level;

public class GamePlayBackground extends MonoBehaviour {

    private SpriteRenderer spriteRenderer;
    private SpriteRenderer blackBackground;
    private EventActionID gamePlayBackground_onLevelLoaded = null;

    private double TWEEN_DURATION = 3;
    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public GamePlayBackground(GameObject owner) {
        super(owner);

        spriteRenderer = GameObjectManager.instantiate("GamePlayBackground")
                .addComponent(SpriteRenderer.class);
        blackBackground =  GameObjectManager.instantiate("BlackBackground")
                .addComponent(SpriteRenderer.class);
        spriteRenderer.getGameObject().setParent(owner);
        blackBackground.getGameObject().setParent(owner);
        spriteRenderer.setImage(ImageAsset.ImageIndex.GamePlayBackground_Normal.getImage());
        spriteRenderer.setPivot(new Vector2(0.5,0.5));
        blackBackground.setPivot(new Vector2(0.5,0.5));
        spriteRenderer.setRenderLayer(RenderLayer._2);
        blackBackground.setRenderLayer(RenderLayer.Overlay);
        owner.getTransform().setGlobalPosition(new Vector2((Main.STAGE_WIDTH) / 2, Main.STAGE_HEIGHT /2));

        gamePlayBackground_onLevelLoaded = LevelManager.onLevelLoaded.addListener(this::gamePlayBackground_onLevelLoaded);
    }

    private void gamePlayBackground_onLevelLoaded(Object sender, LevelManager.OnLevelLoadedEventArgs e){
        if(e.type == LevelType.Showdown){
            spriteRenderer.setImage(ImageAsset.ImageIndex.GamePlayBackground_Boss.getImage());
            blackBackground.setImage(ImageAsset.ImageIndex.BlackBackground.getImage());
            blackBackgroundStartAnimation();
        }
    }

    @Override
    protected void onDestroy() {
        LevelManager.onLevelLoaded.removeListener(gamePlayBackground_onLevelLoaded);
    }

    public void blackBackgroundStartAnimation(){
        Tween.to(blackBackground.getGameObject())
                .fadeTo(0, TWEEN_DURATION)
                .play();
    }
}
