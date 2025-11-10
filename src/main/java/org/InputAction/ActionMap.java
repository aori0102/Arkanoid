package org.InputAction;

import java.util.HashMap;
import java.util.HashSet;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import org.Event.EventHandler;
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
        Dash,
        None
    }

    private PlayerInput playerInput;
    private final HashMap<KeyCode, Action> keyActionMap = new HashMap<>();
    private final HashMap<MouseButton, Action> mouseActionMap = new HashMap<>();

    private final HashSet<KeyCode> currentKey = new HashSet<>();
    private final HashSet<KeyCode> previousKey = new HashSet<>();

    private final HashSet<MouseButton> previousMouseButton = new HashSet<>();
    private final HashSet<MouseButton> currentMouseButton = new HashSet<>();

    public EventHandler<Action> onKeyPressed = new EventHandler<>(ActionMap.class);
    public EventHandler<Action> onKeyHeld = new EventHandler<>(ActionMap.class);
    public EventHandler<Action> onKeyReleased = new EventHandler<>(ActionMap.class);

    public EventHandler<Action> onMouseHeld = new EventHandler<>(ActionMap.class);
    public EventHandler<Action> onMouseReleased = new EventHandler<>(ActionMap.class);
    public EventHandler<Action> onMouseClicked = new EventHandler<>(ActionMap.class);

    public ActionMap(GameObject owner) {
        super(owner);
        addComponent(PlayerInput.class);
    }

    public void awake() {
        playerInput = getComponent(PlayerInput.class);
        assignActionMap();
    }

    /**
     * Assign the input with corresponding action state.
     */
    public void assignActionMap() {

        keyActionMap.put(KeyCode.A, Action.GoLeft);
        keyActionMap.put(KeyCode.D, Action.GoRight);
        keyActionMap.put(KeyCode.Q, Action.Skill1);
        keyActionMap.put(KeyCode.E, Action.Skill2);
        keyActionMap.put(KeyCode.X, Action.Skill3);
        keyActionMap.put(KeyCode.SHIFT, Action.Dash);
        mouseActionMap.put(MouseButton.PRIMARY, Action.MousePressed);

    }

    /**
     * Adjust the current action state.
     */
    public void update() {
        handlePlayerInput();
    }

    public void handlePlayerInput() {
        currentKey.clear();
        currentMouseButton.clear();

        for (KeyCode keyCode : keyActionMap.keySet()) {
            if (playerInput.isKeyPressed(keyCode)) {
                currentKey.add(keyCode);
            }
        }

        for (MouseButton mouseButton : mouseActionMap.keySet()) {
            if (playerInput.isMousePressed(mouseButton)) {
                currentMouseButton.add(mouseButton);
            }
        }

        HashSet<KeyCode> allKey = new HashSet<>(currentKey);
        allKey.addAll(previousKey);
        for (KeyCode keyCode : allKey) {
            Action action = keyActionMap.get(keyCode);
            boolean wasPressed = previousKey.contains(keyCode);
            boolean isPressed = currentKey.contains(keyCode);

            if (!wasPressed && isPressed) {
                onKeyPressed.invoke(this, action);
            } else if (wasPressed && isPressed) {
                onKeyHeld.invoke(this, action);
            } else {
                onKeyReleased.invoke(this, action);
            }
        }

        HashSet<MouseButton> allMouseButton = new HashSet<>(currentMouseButton);
        allMouseButton.addAll(previousMouseButton);
        for  (MouseButton mouseButton : allMouseButton) {
            Action action = mouseActionMap.get(mouseButton);
            boolean wasPressed = previousMouseButton.contains(mouseButton);
            boolean isPressed = currentMouseButton.contains(mouseButton);

            if (!wasPressed && isPressed) {
                onMouseClicked.invoke(this, action);
            } else if (wasPressed && isPressed) {
                onMouseHeld.invoke(this, action);
            } else {
                onMouseReleased.invoke(this, action);
            }
        }

        previousKey.clear();
        previousKey.addAll(currentKey);

        previousMouseButton.clear();
        previousMouseButton.addAll(currentMouseButton);
    }

}
