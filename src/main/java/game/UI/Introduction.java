package game.UI;

import org.Audio.AudioManager;
import org.Audio.SFXAsset;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Layer.RenderLayer;
import org.Main;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import org.Scene.SceneKey;
import org.Scene.SceneManager;
import utils.UITween.Tween;
import utils.Vector2;

public class Introduction extends MonoBehaviour {
    private SpriteRenderer logo;
    private SpriteRenderer blackBackground;

    private double TWEEN_DURATION = 0.6;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public Introduction(GameObject owner) {
        super(owner);

        logo = GameObjectManager.instantiate("Logo")
                .addComponent(SpriteRenderer.class);

        blackBackground = GameObjectManager.instantiate("BlackBackground")
                .addComponent(SpriteRenderer.class);

        logo.setPivot(new Vector2(0.5, 0.5));
        logo.getTransform().setGlobalPosition(new Vector2(Main.STAGE_WIDTH/2, Main.STAGE_HEIGHT/2));

        logo.setRenderLayer(RenderLayer.UI_4);
        blackBackground.setRenderLayer(RenderLayer.UI_2);

        logo.setImage(ImageAsset.ImageIndex.Nidemoka.getImage());
        blackBackground.setImage(ImageAsset.ImageIndex.BlackBackground.getImage());

        Tween.to(logo.getGameObject())
                .fadeTo(0, 0.001)
                .play();
    }

    @Override
    public void start() {
        startAnimation();
    }

    public void exitAnimation() {
        Tween.to(logo.getGameObject())
                .fadeTo(0, TWEEN_DURATION)
                .setDelay(1.5)
                .onComplete(()->{
                    SceneManager.loadScene(SceneKey.Menu);
                })
                .play();
    }

    public void startAnimation(){
        Tween.to(logo.getGameObject())
                .fadeTo(1, TWEEN_DURATION)
                .setDelay(0.5)
                .onComplete(()->{
                    exitAnimation();
                    AudioManager.playSFX(SFXAsset.SFXIndex.IntroductionVoice);
                })
                .play();
    }



}
