package org;

import java.util.HashMap;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public class ActionMap extends MonoBehaviour {

    /**
     * Action state of the paddle. Each state corresponds with each button input from keyboard.
     */
    public enum Action {
        GoLeft,
        GoRight,
        MousePressed,
        None
    }

    private PlayerInput playerInput;
    private HashMap<KeyCode, Action> keyActionMap = new HashMap<>();
    private HashMap<MouseButton, Action> mouseActionMap = new HashMap<>();
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

        keyActionMap.put(KeyCode.A, Action.GoLeft);
        keyActionMap.put(KeyCode.D, Action.GoRight);
        mouseActionMap.put(MouseButton.PRIMARY, Action.MousePressed);

    }

    /**
     * Adjust the current action state.
     */
    public void update() {
        currentAction = Action.None;
        for (KeyCode keyCode : keyActionMap.keySet()) {
            if (playerInput.isKeyPressed(keyCode)) {
                currentAction = keyActionMap.get(keyCode);
            }
        }
        for (MouseButton mouseButton : mouseActionMap.keySet()) {
            if (playerInput.isMousePressed(mouseButton)) {
                currentAction = mouseActionMap.get(mouseButton);
            }
        }
    }

    @Override
    protected void destroyMono() {

    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
    }

    @Override
    protected void clear() {

    }
}
