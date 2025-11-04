package game.UI.Prefab;

import game.UI.Buttons.PauseButton;
import game.UI.Buttons.ResumeButton;
import game.UI.PauseMenu.PauseMenuManager;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public class ResumeButtonPrefab extends Prefab {

    @Override
    public GameObject instantiatePrefab() {
        ResumeButton resumeButton = GameObjectManager.instantiate("ResumeButton")
                .addComponent(ResumeButton.class);
        PauseMenuManager.getInstance().linkResumeButton(resumeButton);

        return resumeButton.getGameObject();
    }
}
