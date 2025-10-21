package org.InputAction;

import java.util.HashMap;
import java.util.HashSet;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

public class ActionMap extends MonoBehaviour {

    /**
     * Action state of the paddle. Each state corresponds with each button input from keyboard.
     */
    public enum Action {
        GoLeft,
        GoRight,
        MousePressed,
        Skill1,
        Skill2,
        Skill3,
        None
    }

    private PlayerInput playerInput;
    private HashMap<KeyCode, Action> keyActionMap = new HashMap<>();
    private HashMap<MouseButton, Action> mouseActionMap = new HashMap<>();
    public HashSet<Action> currentAction =new HashSet<>();

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
        keyActionMap.put(KeyCode.DIGIT1, Action.Skill1);
        keyActionMap.put(KeyCode.DIGIT2, Action.Skill2);
        keyActionMap.put(KeyCode.DIGIT3, Action.Skill3);
        mouseActionMap.put(MouseButton.PRIMARY, Action.MousePressed);

    }

    /**
     * Adjust the current action state.
     */
    public void update() {
        currentAction.clear();
        for (KeyCode keyCode : keyActionMap.keySet()) {
            if (playerInput.isKeyPressed(keyCode)) {
                currentAction.add(keyActionMap.get(keyCode));
            }
        }
        for (MouseButton mouseButton : mouseActionMap.keySet()) {
            if (playerInput.isMousePressed(mouseButton)) {
                currentAction.add(mouseActionMap.get(mouseButton));
            }
        }
    }

    public boolean isActionPresented(Action action) {
        return currentAction.contains(action);
    }

    @Override
    protected void destroyComponent() {

    }
}
