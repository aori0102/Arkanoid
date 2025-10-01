package org.UI;

import org.*;

public abstract class UI extends MonoBehaviour {
    private double height;
    private double width;
    protected Transform transform;

    /**
     * Get the game object this MonoBehaviour is attached to.
     *
     * @param owner The owner of this component.
     */
    public UI(GameObject owner) {
        super(owner);
        transform = owner.addComponent(Transform.class);

    }

    public Transform getTransform() {
        return transform;
    }
    public void setTransform(Transform transform) {}
    public double getHeight() {
        return height;
    }
    public void setHeight(double height) {
        this.height = height;
    }
    public double getWidth() {
        return width;
    }
    public void setWidth(double width) {
        this.width = width;
    }
}
