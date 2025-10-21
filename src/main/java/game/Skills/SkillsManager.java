package game.Skills;

import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.InputAction.ActionMap;
import org.InputAction.PlayerInput;

public class SkillsManager extends MonoBehaviour {
    
    private ActionMap actionMap;
    
    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public SkillsManager(GameObject owner) {
        super(owner);
    }

    private void handleOnSkillButtonPressed(GameObject sender) {
    }
    
    /**
     * <br><br>
     * <b><i><u>NOTE</u> : Only use within {@link }
     * as part of component linking process.</i></b>
     *
     * @param actionMap .
     */
    public void linkActionMap(ActionMap actionMap) {
        this.actionMap = actionMap;
    }

    @Override
    protected void destroyComponent() {
        
    }
}
