package game.UI.PauseMenu;

import game.GameManager.GameManager;
import game.UI.Buttons.*;
import javafx.scene.input.MouseEvent;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

/**
 * Controller component responsible for handling user input (clicks) on the Pause Menu buttons
 * and translating those actions into game state changes via the {@link GameManager}.
 * <p>
 * This class links to the specific button components managed by the {@link PauseMenuManager}.
 */
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

    /**
     * Handles the click event for the Pause button (used to re-enter the game from the main canvas).
     * This pauses the game and shows the full pause menu UI.
     *
     * @param sender The object that triggered the event.
     * @param e The mouse event data.
     */
    private void onPauseButtonClicked(Object sender, MouseEvent e) {
        GameManager.getInstance().pauseGame();
        PauseMenuManager.getInstance().showPauseMenu();
    }

    /**
     * Handles the click event for the Resume button.
     * This resumes the game time and hides the pause menu UI.
     *
     * @param sender The object that triggered the event.
     * @param e The mouse event data.
     */
    private void onResumeButtonClicked(Object sender, MouseEvent e) {
        GameManager.getInstance().resumeGame();
        PauseMenuManager.getInstance().hidePauseMenu();
    }

    /**
     * Handles the click event for the Menu button.
     * This triggers the process to quit the current game session and return to the main menu.
     *
     * @param sender The object that triggered the event.
     * @param e The mouse event data.
     */
    private void onMenuButtonClicked(Object sender, MouseEvent e) {
        GameManager.getInstance().quitToMainMenu();
    }

    /**
     * Retrieves the necessary button components from the {@link PauseMenuManager}
     * and links them to the local fields.
     */
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
