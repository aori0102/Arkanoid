package ecs;

import utils.Vector2;

public class Transform extends MonoBehaviour {

    private Vector2 position = new Vector2();
    private Vector2 scale = new Vector2(1f, 1f);

    public Vector2 getPosition() {
        return new Vector2(position);
    }

    public Vector2 getScale() {
        return new Vector2(scale);
    }

    public void setPosition(Vector2 position) {
        this.position = new Vector2(position);
    }

    public void setScale(Vector2 scale) {
        this.scale = new Vector2(scale);
    }

    @Override
    protected MonoBehaviour clone(MonoBehaviour source) {

        Transform newTransform = new Transform();
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
