package game.UI.PauseMenu;

import game.GameManager.GameManager;
import game.UI.Buttons.*;
import javafx.scene.input.MouseEvent;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

public class PauseMenuController extends MonoBehaviour {
    private PauseButton pauseButton;
    private ResumeButton resumeButton;
    private MenuButton menuButton;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PauseMenuController(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        linkButtons();
        pauseButton.getButtonUI().onPointerClick.addListener(this::onPauseButtonClicked);
        resumeButton.getButtonUI().onPointerClick.addListener(this::onResumeButtonClicked);
        menuButton.getButtonUI().onPointerClick.addListener(this::onMenuButtonClicked);

        PauseMenuManager.getInstance().hidePauseMenu();
    }

    private void onPauseButtonClicked(Object sender, MouseEvent e) {
        GameManager.getInstance().pauseGame();
        PauseMenuManager.getInstance().showPauseMenu();
    }

    private void onResumeButtonClicked(Object sender, MouseEvent e) {
        GameManager.getInstance().resumeGame();
        PauseMenuManager.getInstance().hidePauseMenu();
    }

    private void onMenuButtonClicked(Object sender, MouseEvent e) {
        GameManager.getInstance().quitToMainMenu();
    }

    private void linkButtons() {
        try {
            pauseButton = PauseMenuManager.getInstance().getPauseButton();
            resumeButton = PauseMenuManager.getInstance().getResumeButton();
            menuButton = PauseMenuManager.getInstance().getMenuButton();

            if (pauseButton == null || resumeButton == null || menuButton == null) {
                throw new NullPointerException("PauseMenuButton(s) is null!");
            }
        } catch (Exception e) {
            System.err.println("[PauseMenuController] Failed to link buttons: " + e.getMessage());
            return;
        }

        System.out.println("[PauseMenuController] All buttons linked successfully.");
    }
}
