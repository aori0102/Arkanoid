package game.UI.MainMenu;

import game.GameManager.GameManager;
import game.UI.Buttons.*;
import game.UI.Options.OptionsManager;
import game.UI.RecordMenu.RecordMenuManager;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

import javafx.scene.input.MouseEvent;

/**
 * Controller component responsible for handling all user interactions (clicks)
 * on the buttons within the main menu.
 * <p>
 * It acts as the intermediary, translating button clicks into actions managed
 * by global managers like {@link GameManager}, {@link RecordMenuManager}, and {@link OptionsManager}.
 */
public class MainMenuController extends MonoBehaviour {
    private StartButton startButton;
    private ContinueButton continueButton;
    private RecordButton recordButton;
    private OptionsButton optionsButton;
    private QuitButton quitButton;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public MainMenuController(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        linkButtons();
        startButton.getButtonUI().onPointerClick.addListener(this::onStartButtonClick);
        continueButton.getButtonUI().onPointerClick.addListener(this::onContinueButtonClick);
        recordButton.getButtonUI().onPointerClick.addListener(this::onRecordButtonClick);
        optionsButton.getButtonUI().onPointerClick.addListener(this::onOptionsButtonClick);
        quitButton.getButtonUI().onPointerClick.addListener(this::onQuitButtonClick);

    }

    /**
     * Handles the click event for the Start button.
     * Requests the {@link GameManager} to start a new game session.
     *
     * @param sender The object that triggered the event.
     * @param e      The mouse event data.
     */
    private void onStartButtonClick(Object sender, MouseEvent e) {
        GameManager.getInstance().startNewGame();
    }

    /**
     * Handles the click event for the Continue button.
     * Requests the {@link GameManager} to load and continue a saved game.
     *
     * @param sender The object that triggered the event.
     * @param e      The mouse event data.
     */
    private void onContinueButtonClick(Object sender, MouseEvent e) {
        GameManager.getInstance().continueGame();
    }

    /**
     * Handles the click event for the Continue button.
     * Requests the {@link GameManager} to load and continue a saved game.
     *
     * @param sender The object that triggered the event.
     * @param e      The mouse event data.
     */
    private void onRecordButtonClick(Object sender, MouseEvent e) {
        MainMenuManager.getInstance().hideUI();
        RecordMenuManager.getInstance().showUI();
    }

    /**
     * Handles the click event for the Options button.
     * Hides the main menu UI and shows the options menu UI via their respective managers.
     *
     * @param sender The object that triggered the event.
     * @param e      The mouse event data.
     */
    private void onOptionsButtonClick(Object sender, MouseEvent e) {
        MainMenuManager.getInstance().hideUI();
        OptionsManager.getInstance().showUI();
    }

    /**
     * Handles the click event for the Quit button.
     * Requests the {@link GameManager} to shut down the application.
     *
     * @param sender The object that triggered the event.
     * @param e      The mouse event data.
     */
    private void onQuitButtonClick(Object sender, MouseEvent e) {
        GameManager.getInstance().quitGame();
    }

    /**
     * Retrieves the necessary button components from the {@link MainMenuManager}
     * and links them to the local fields.
     */
    private void linkButtons() {
        try {
            startButton = MainMenuManager.getInstance().getStartButton();
            continueButton = MainMenuManager.getInstance().getContinueButton();
            recordButton = MainMenuManager.getInstance().getRecordButton();
            optionsButton = MainMenuManager.getInstance().getOptionsButton();
            quitButton = MainMenuManager.getInstance().getQuitButton();
        } catch (Exception e) {
            System.err.println("[MainMenuController] Failed to link buttons: " + e.getMessage());
        }

        System.out.println("[MainMenuController] All buttons linked successfully.");
    }

}
