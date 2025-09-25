package org;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class PlayerInput extends MonoBehaviour {

    private Set<KeyCode> pressedKey = new HashSet<KeyCode>();
    private HashMap pressedMouse = new HashMap<MouseButton, MouseEvent>();
    private Scene scene;

    public PlayerInput(GameObject owner) {
        super(owner);
    }

    public void awake() {
        scene = Main.scene;
    }

    public void update() {
        HandlePlayerInput();
    }

    @Override
    protected void destroyMono() {

    }

    /**
     * Add the input from keyboard to pressedKey set and remove it when releasing.
     */
   public void HandlePlayerInput() {
         scene.setOnKeyPressed(e -> {
            pressedKey.add(e.getCode());
        });
        scene.setOnKeyReleased(e -> {
            pressedKey.remove(e.getCode());
        });
        scene.setOnMouseDragEntered(e -> {
            pressedMouse.put(e.getButton(), e);
        });
        scene.setOnMouseDragExited(e -> {
            pressedMouse.remove(e.getButton());
        });

    }


    /**
     * Check if the key is pressed.
     * @param keyCode is the checked key button
     * @return true if the key is pressed.
     */
    public boolean isKeyPressed(KeyCode keyCode) {
        return pressedKey.contains(keyCode);
    }

    /**
     * Check if the mouse button is pressed
     * @param button is the checked button
     * @return true if the button is pressed
     */
    public boolean isMousePressed(MouseButton button) {
        return pressedMouse.containsKey(button);
    }

    public MouseEvent getMouseEvent(MouseButton button) {
        if (isMousePressed(button)) {
            return (MouseEvent) pressedMouse.get(button);
        }
        return null;
    }

    public Group getRoot() {
        return Main.root;
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
    }

    @Override
    protected void clear() {

    }
}
