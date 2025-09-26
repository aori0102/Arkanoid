package ecs.UI;


import ecs.GameObject;
import ecs.MonoBehaviour;

public abstract class UI extends MonoBehaviour {
    private double height;
    private double width;

    /**
     * Get the game object this MonoBehaviour is attached to.
     *
     * @param owner The owner of this component.
     */
    public UI(GameObject owner) {
        super(owner);
    }

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
