package org;

import java.util.HashSet;
import java.util.Set;

import ecs.GameObject;
import ecs.MonoBehaviour;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class PlayerInput extends MonoBehaviour {

    private Set<KeyCode> pressedKey = new HashSet<KeyCode>();
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
    }


    /**
     * Check if the key is pressed.
     * @param keyCode the wanted check key
     * @return true if the key is pressed.
     */
    public boolean isKeyPressed(KeyCode keyCode) {
        return pressedKey.contains(keyCode);
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
    }

    @Override
    protected void clear() {

    }
}
