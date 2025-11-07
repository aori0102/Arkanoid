package game.UI.MainMenu;

import game.GameManager.GameManager;
import game.UI.Buttons.*;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

import javafx.scene.input.MouseEvent;

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


    private void onStartButtonClick(Object sender, MouseEvent e) {
        GameManager.getInstance().startNewGame();
    }

    private void onContinueButtonClick(Object sender, MouseEvent e) {
        GameManager.getInstance().continueGame();
    }

    private void onRecordButtonClick(Object sender, MouseEvent e) {
        GameManager.getInstance().showRecord();
    }

    private void onOptionsButtonClick(Object sender, MouseEvent e) {
        GameManager.getInstance().showOptions();
    }

    private void onQuitButtonClick(Object sender, MouseEvent e) {
        GameManager.getInstance().quitGame();
    }

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
