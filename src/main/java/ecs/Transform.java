package ecs;

import utils.Vector2;

public class Transform extends MonoBehaviour {

    private Vector2 position = new Vector2();
    private Vector2 scale = new Vector2(1f, 1f);

    public Vector2 GetPosition() {
        return new Vector2(position);
    }

    public Vector2 GetScale() {
        return new Vector2(scale);
    }

    public void SetPosition(Vector2 position) {
        this.position = new Vector2(position);
    }

    public void SetScale(Vector2 scale) {
        this.scale = new Vector2(scale);
    }

    @Override
    protected MonoBehaviour Clone(MonoBehaviour source) {

        Transform newTransform = new Transform();
        newTransform.position = this.position;
        newTransform.scale = this.scale;
        return newTransform;

    }

    @Override
    protected void Clear() {
        position = null;
        scale = null;
    }

}
