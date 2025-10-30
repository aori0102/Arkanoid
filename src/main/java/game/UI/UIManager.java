package game.UI;

import game.UI.Buttons.BaseButton;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

import java.util.ArrayList;
import java.util.List;

public class UIManager extends MonoBehaviour {
    private static UIManager instance;

    private List<BaseButton> pauseMenuButtons =  new ArrayList<>();

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public UIManager(GameObject owner) {
        super(owner);

        if (instance == null) {
            instance = this;
        }
    }

    public static UIManager getInstance(){
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
        pauseMenuButtons.add(button);
    }


}
