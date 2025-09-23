package ecs;

import utils.Vector2;

import java.util.function.Consumer;

public class BoxCollider extends MonoBehaviour {

    private Vector2 center = new Vector2();
    private Vector2 size = new Vector2();
    public Consumer<BoxCollider> onCollisionEnter;

    public BoxCollider(GameObject owner) {
        super(owner);
        PhysicsManager.RegisterCollider(this);
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        BoxCollider newBoxCollider = new BoxCollider(newOwner);
        newBoxCollider.center = this.center;
        newBoxCollider.size = this.size;
        return newBoxCollider;
    }

    @Override
    protected void clear() {
        center = null;
        size = null;
    }

    public Vector2 getLocalCenter() {
        return new Vector2(center);
    }

    public Vector2 getCenter() {
        return transform().getPosition().add(center);
    }

    public Vector2 getLocalSize() {
        return new Vector2(size);
    }

    public Vector2 getSize() {
        return size.scale(transform().getScale());
    }

    public void setLocalCenter(Vector2 center) {
        this.center = center;
    }

    public void setLocalSize(Vector2 size) {
        this.size = size;
    }

    public Vector2 getMinBound() {
        return getCenter().subtract(getSize().divide(2.0));
    }

    public Vector2 getMaxBound() {
        return getCenter().add(getSize().divide(2.0));
    }

    public Vector2 getExtents() {
        return getSize().divide(2.0);
    }

}
