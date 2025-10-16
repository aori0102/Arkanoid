package org.InputAction;

import java.util.HashMap;
import java.util.HashSet;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Scene.SceneManager;

public class PlayerInput extends MonoBehaviour {

    private final HashSet<KeyCode> pressedKey = new HashSet<>();
    private final HashMap<MouseButton, MouseEvent> pressedMouse = new HashMap<>();

    public boolean isMouseReleased = false;

    public PlayerInput(GameObject owner) {
        super(owner);
    }

    public void awake() {
        handlePlayerInput();
    }

    /**
     * Add the input from keyboard to pressedKey set and remove it when releasing.
     */
    public void handlePlayerInput() {

        var sceneMap = SceneManager.getSceneMap();
        var sceneCollection = sceneMap.values();
        for (var scene : sceneCollection) {

            scene.setOnKeyPressed(e -> {
                pressedKey.add(e.getCode());
            });
            scene.setOnKeyReleased(e -> {
                pressedKey.remove(e.getCode());
            });
            scene.setOnMouseDragged(e -> {
                pressedMouse.put(e.getButton(), e);
            });
            scene.setOnMouseReleased(e -> {
                pressedMouse.remove(e.getButton());
                isMouseReleased = true;
            });

        }

    }


    /**
     * Check if the key is pressed.
     *
     * @param keyCode is the checked key button
     * @return true if the key is pressed.
     */
    public boolean isKeyPressed(KeyCode keyCode) {
        return pressedKey.contains(keyCode);
    }

    /**
     * Check if the mouse button is pressed
     *
     * @param button is the checked button
     * @return true if the button is pressed
     */
    public boolean isMousePressed(MouseButton button) {
        return pressedMouse.containsKey(button);
    }

    public MouseEvent getMouseEvent(MouseButton button) {
        if (isMousePressed(button)) {
            return pressedMouse.get(button);
        }
        return null;
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
    }

    @Override
    protected void destroyComponent() {

    }
}
