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
        LaserBeam,
        Updraft,
        Invincible,
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
     * <p>
     *     Each type of input will be stored in a specific HashMap. Keyboard input will be stored in
     *     {@code Key Action Map}. Mouse input will be stored in {@code Mouse Action Map}
     * </p>
     */
    public void assignActionMap() {

        keyActionMap.put(KeyCode.A, Action.GoLeft);
        keyActionMap.put(KeyCode.D, Action.GoRight);
        keyActionMap.put(KeyCode.RIGHT, Action.GoRight);
        keyActionMap.put(KeyCode.LEFT, Action.GoLeft);
        keyActionMap.put(KeyCode.Q, Action.LaserBeam);
        keyActionMap.put(KeyCode.E, Action.Updraft);
        keyActionMap.put(KeyCode.X, Action.Invincible);
        keyActionMap.put(KeyCode.SHIFT, Action.Dash);
        mouseActionMap.put(MouseButton.PRIMARY, Action.MousePressed);

    }

    /**
     * Adjust the current action state.
     */
    public void update() {
        handlePlayerInput();
    }

    /**
     * Handle input from Player
     * <p>
     *     When this receives an input, it will check whether it is from keyboard or mouse.
     *     After that, checking if it is pressed, hold, or clicked then firing an event correspond with
     *     that input.
     * </p>
     */
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
