package org;

import java.util.HashMap;

import ecs.GameObject;
import ecs.MonoBehaviour;
import javafx.scene.input.KeyCode;

public class ActionMap extends MonoBehaviour {

    /**
     * Action state of the paddle. Each state corresponds with each button input from keyboard.
     */
    public enum Action {
        GoLeft,
        GoRight,
        None
    }

    private PlayerInput playerInput;
    private HashMap<KeyCode, Action> actionMap = new HashMap<>();
    public Action currentAction = Action.None;

    public ActionMap(GameObject owner) {
        super(owner);
    }

    public void awake() {
        playerInput = gameObject.getComponent(PlayerInput.class);
        assignActionMap();
    }

    /**
     * Assign the input with corresponding action state.
     */
    public void assignActionMap() {

        actionMap.put(KeyCode.A, Action.GoLeft);
        actionMap.put(KeyCode.D, Action.GoRight);

    }

    /**
     * Adjust the current action state.
     */
    public void update() {
        currentAction = Action.None;
        for (KeyCode keyCode : actionMap.keySet()) {
            if (playerInput.isKeyPressed(keyCode)) {
                currentAction = actionMap.get(keyCode);
            }
        }
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
    }

    @Override
    protected void clear() {

    }
}
