package org;

import java.util.HashSet;
import java.util.Set;

import ecs.GameObject;
import ecs.MonoBehaviour;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class PlayerInput extends MonoBehaviour {

    private Set<KeyCode> pressedKey = new HashSet<KeyCode>();
    private ActionMap actionMap;
    private Scene scene;

    public PlayerInput(GameObject owner) {
        super(owner);
    }

    public void awake() {
        actionMap = gameObject.getComponent(ActionMap.class);
        scene = Main.scene;
    }

    public void update() {
        HandlePlayerInput();
    }


    public void HandlePlayerInput() {
        scene.setOnKeyPressed(e -> {
            pressedKey.add(e.getCode());
            System.out.println("Press " + pressedKey.toString());});
        scene.setOnKeyReleased(e -> {pressedKey.remove(e.getCode());});
    }


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
