package org.Rendering;

import javafx.scene.Node;
import org.GameObject.GameObject;

public class Slider extends Renderable {
    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public Slider(GameObject owner) {
        super(owner);
    }

    @Override
    public Node getNode() {
        return null;
    }
}
