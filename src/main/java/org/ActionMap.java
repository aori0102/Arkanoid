package org;

import java.util.HashMap;

import ecs.GameObject;
import ecs.MonoBehaviour;
import javafx.scene.input.KeyCode;

public class ActionMap extends MonoBehaviour {

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

    public void assignActionMap() {

        actionMap.put(KeyCode.A, Action.GoLeft);
        actionMap.put(KeyCode.D, Action.GoRight);

    }

    public void update() {
        for (KeyCode keyCode : actionMap.keySet()) {
            if (playerInput.isKeyPressed(keyCode)) {
                currentAction = actionMap.get(keyCode);
            } else {
                currentAction = Action.None;
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
