package ecs;

import utils.Vector2;

import java.util.function.Function;

public class Transform extends MonoBehaviour {

    private Vector2 position = new Vector2();
    private Vector2 scale = new Vector2(1f, 1f);

    protected Function<Void, Void> onPositionChanged;

    public Transform(GameObject owner) {
        super(owner);
    }

    /**
     * Get the transform position.
     *
     * @return The transform position.
     */
    public Vector2 getPosition() {
        return new Vector2(position);
    }

    /**
     * Get the transform scale.
     *
     * @return The transform scale.
     */
    public Vector2 getScale() {
        return new Vector2(scale);
    }

    /**
     * Set the transform position.
     */
    public void setPosition(Vector2 position) {
        this.position = new Vector2(position);
    }

    /**
     * Set the transform scale.
     */
    public void setScale(Vector2 scale) {
        this.scale = new Vector2(scale);
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {

        Transform newTransform = new Transform(newOwner);
        newTransform.position = this.position;
        newTransform.scale = this.scale;
        return newTransform;

    }

    @Override
    protected void clear() {
        position = null;
        scale = null;
    }

}
