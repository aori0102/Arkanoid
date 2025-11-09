package game.UI.PauseMenu;

import game.UI.Buttons.BaseButton;
import game.UI.Buttons.MenuButton;
import game.UI.Buttons.PauseButton;
import game.UI.Buttons.ResumeButton;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Layer.RenderLayer;
import org.Main;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.UITween.Ease;
import utils.UITween.Tween;
import utils.Vector2;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PauseMenuManager extends MonoBehaviour {
    private static PauseMenuManager instance;
    private PauseButton pauseButton;
    private ResumeButton resumeButton;
    private MenuButton menuButton;
    private final GameObject dimmedBackground;

    private final double SLIDE_DISTANCE = -800;
    private final double SLIDE_DURATION = 0.6;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PauseMenuManager(GameObject owner) {
        super(owner);

        if (instance != null) {
            throw new ReinitializedSingletonException("PauseMenuManager is a singleton!");
        }
        instance = this;

        dimmedBackground = GameObjectManager.instantiate("DimmedBackground");
        var spriteRenderer = dimmedBackground.addComponent(SpriteRenderer.class);
        spriteRenderer.setImage(ImageAsset.ImageIndex.DimmedBackground.getImage());
        spriteRenderer.setRenderLayer(RenderLayer.UI_Middle);
        PrefabManager.instantiatePrefab(PrefabIndex.PauseButton);
        PrefabManager.instantiatePrefab(PrefabIndex.ResumeButton);
        PrefabManager.instantiatePrefab(PrefabIndex.MenuButton);

        pauseButton.getTransform().setGlobalPosition(new Vector2(50, 50));
        resumeButton.getTransform().setGlobalPosition(new Vector2(Main.STAGE_WIDTH / 2, 250));
        menuButton.getTransform().setGlobalPosition(new Vector2(Main.STAGE_WIDTH / 2, 450));

        resumeButton.getGameObject().setActive(false);
        menuButton.getGameObject().setActive(false);

        hidePauseMenu();

        GameObjectManager.instantiate("PauseMenuController").addComponent(PauseMenuController.class);
    }

    @Override
    public void onDestroy() {
        instance = null;
    }

    public static PauseMenuManager getInstance() {
        return instance;
    }

    public void showPauseMenu() {
        System.out.println("showPauseMenu");
        dimmedBackground.setActive(true); // enable first
        resumeButton.getGameObject().setActive(true);
        menuButton.getGameObject().setActive(true);
        Tween.to(resumeButton.getGameObject())
                .moveX(0, SLIDE_DURATION)
                .ease(Ease.OUT_BACK)
                .setDelay(0.0)
                .ignoreTimeScale(true)
                .play();

        Tween.to(menuButton.getGameObject())
                .moveX(0, SLIDE_DURATION)
                .ease(Ease.OUT_BACK)
                .setDelay(0.1) // slight delay looks nicer
                .ignoreTimeScale(true)
                .play();
    }

    public void hidePauseMenu() {
        System.out.println("hidePauseMenu");

        Tween.to(resumeButton.getGameObject())
                .moveX(SLIDE_DISTANCE, SLIDE_DURATION)
                .ease(Ease.IN_BACK)
                .setDelay(0.0)
                .ignoreTimeScale(true)
                .play();
        Tween.to(menuButton.getGameObject())
                .moveX(SLIDE_DISTANCE, SLIDE_DURATION)
                .ease(Ease.IN_BACK)
                .setDelay(0.0)
                .ignoreTimeScale(true)
                .play();

        dimmedBackground.setActive(false);
    }


    public void linkPauseButton(PauseButton pauseButton) {
        this.pauseButton = pauseButton;
    }

    public void linkResumeButton(ResumeButton resumeButton) {
        this.resumeButton = resumeButton;
    }

    public void linkMenuButton(MenuButton menuButton) {
        this.menuButton = menuButton;
    }

    public PauseButton getPauseButton() {
        return pauseButton;
    }

    public ResumeButton getResumeButton() {
        return resumeButton;
    }

    public MenuButton getMenuButton() {
        return menuButton;
    }
}
