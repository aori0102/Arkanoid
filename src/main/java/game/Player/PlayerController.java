package game.Player;

import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.InputAction.ActionMap;
import org.InputAction.PlayerInput;

import java.util.HashSet;

/**
 * Control all the input from player
 */
public class PlayerController extends MonoBehaviour {

    public EventHandler<ActionMap.Action> onSkillsInputRequested = new  EventHandler<ActionMap.Action>(PlayerController.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PlayerController(GameObject owner) {
        super(owner);
        addComponent(PlayerInput.class);
        addComponent(ActionMap.class);
    }

    public void update() {
        handleOnSkillInputRequest();
    }

    public PlayerInput getPlayerInput() {
        return getComponent(PlayerInput.class);
    }

    public ActionMap getActionMap() {
        return getComponent(ActionMap.class);
    }

    /**
     * Get all the current action input from user.
     * @return the currentActions set
     */
    public HashSet<ActionMap.Action> getActionsList() {
        return getComponent(ActionMap.class).currentAction;
    }

    public void handleOnSkillInputRequest() {
        for (ActionMap.Action action : getActionsList()) {
            if (action == ActionMap.Action.Skill1
            || action == ActionMap.Action.Skill2
            || action == ActionMap.Action.Skill3) {
                onSkillsInputRequested.invoke(this, action);
            }
        }
    }

    @Override
    protected void destroyComponent() {

    }
}
