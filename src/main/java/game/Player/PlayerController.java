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

    public PlayerInput getPlayerInput() {
        return getComponent(PlayerInput.class);
    }

    public ActionMap getActionMap() {
        return getComponent(ActionMap.class);
    }

}
