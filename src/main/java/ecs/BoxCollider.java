package ecs;

import utils.Vector2;

import java.util.function.Consumer;

public class BoxCollider extends MonoBehaviour {

    private Vector2 localCenter;
    private Vector2 localSize;
    public Consumer<CollisionData> onCollisionEnter;

    public BoxCollider(GameObject owner) {
        super(owner);
        localCenter = new Vector2(0.0, 0.0);
        localSize = new Vector2(1.0, 1.0);
        PhysicsManager.RegisterCollider(this);
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        BoxCollider newBoxCollider = new BoxCollider(newOwner);
        newBoxCollider.localCenter = this.localCenter;
        newBoxCollider.localSize = this.localSize;
        return newBoxCollider;
    }

    @Override
    protected void clear() {
        localCenter = null;
        localSize = null;
    }

    public Vector2 getLocalCenter() {
        return new Vector2(localCenter);
    }

    public Vector2 getCenter() {
        return transform().getGlobalPosition().add(localCenter);
    }

    public Vector2 getLocalSize() {
        return new Vector2(localSize);
    }

    public Vector2 getSize() {
        return localSize.scaleUp(transform().getGlobalScale());
    }

    public void setLocalCenter(Vector2 center) {
        this.localCenter = center;
    }

    public void setLocalSize(Vector2 size) {
        this.localSize = size;
    }

    public Vector2 getMinBound() {
        return getCenter().subtract(getExtents());
    }

    public Vector2 getMaxBound() {
        return getCenter().add(getExtents());
    }

    public Vector2 getExtents() {
        return getLocalSize().divide(2.0);
    }

}
