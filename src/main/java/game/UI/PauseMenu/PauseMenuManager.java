package game.UI.PauseMenu;

import game.UI.Buttons.BaseButton;
import game.UI.Buttons.MenuButton;
import game.UI.Buttons.ResumeButton;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

import java.util.ArrayList;
import java.util.List;

public class PauseMenuManager extends MonoBehaviour {
    private static PauseMenuManager instance;
    private final List<BaseButton> buttons =  new ArrayList<>();
    private final List<BaseButton> pauseMenuButtons =new ArrayList<>();


    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PauseMenuManager(GameObject owner) {
        super(owner);

        if (instance == null) {
            instance = this;
        }
    }

    public static PauseMenuManager getInstance(){
        return instance;
    }

    public void showPauseMenu(){
        for (BaseButton button : pauseMenuButtons) {
            button.getGameObject().setActive(true);
        }
    }

    public void hidePauseMenu(){
        for (BaseButton button : pauseMenuButtons) {
            button.getGameObject().setActive(false);
        }
    }

    public void addPauseMenuButton(BaseButton button){
        if(button instanceof ResumeButton || button instanceof MenuButton){
            pauseMenuButtons.add(button);
        }
        buttons.add(button);
    }

    public List<BaseButton> getButtons(){
        return buttons;
    }
}
