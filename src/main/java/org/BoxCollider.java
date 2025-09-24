package org;

import utils.Vector2;

import java.util.function.Consumer;

public class BoxCollider extends MonoBehaviour {

    private Vector2 localCenter;
    private Vector2 localSize;
    protected Consumer<CollisionData> onCollisionEnter;

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
        onCollisionEnter = null;
    }

    /**
     * Get the center of the bounding box in local space.
     *
     * @return The center of the bounding box in local space
     */
    public Vector2 getLocalCenter() {
        return new Vector2(localCenter);
    }

    /**
     * Get the center of the bounding box in world space.
     *
     * @return The center of the bounding box in world space.
     */
    public Vector2 getCenter() {
        return transform().getGlobalPosition().add(localCenter);
    }

    /**
     * Get the size of the bounding box in local space.
     *
     * @return The size of the bounding box in local space.
     */
    public Vector2 getLocalSize() {
        return new Vector2(localSize);
    }

    /**
     * Get the size of the bounding box in world space.
     *
     * @return The size of the bounding box in world space.
     */
    public Vector2 getSize() {
        return localSize.scaleUp(transform().getGlobalScale());
    }

    /**
     * Set the center for the bounding box in local space.
     *
     * @param center The center in local space.
     */
    public void setLocalCenter(Vector2 center) {
        this.localCenter = center;
    }

    /**
     * Set the size for the bounding box in local space.
     *
     * @param size The size in local space.
     */
    public void setLocalSize(Vector2 size) {
        this.localSize = size;
    }

    /**
     * Get the min bound of the bounding box. This is
     * always equal to {@code center} - {@code extents}.
     *
     * @return The min bound of the bounding box.
     */
    public Vector2 getMinBound() {
        return getCenter().subtract(getExtents());
    }

    /**
     * Get the max bound of the bounding box. This is
     * always equal to {@code center} + {@code extents}.
     *
     * @return The max bound of the bounding box.
     */
    public Vector2 getMaxBound() {
        return getCenter().add(getExtents());
    }

    /**
     * Get the extents of the bounding box. This is
     * always equal to twice the {@code size}.
     *
     * @return The extents of the bounding box.
     */
    public Vector2 getExtents() {
        return getLocalSize().divide(2.0);
    }

    /**
     * Set the callback when collision happens.
     *
     * @param onCollisionEnter The callback when collision happens.
     */
    public void setOnCollisionEnterCallback(Consumer<CollisionData> onCollisionEnter) {
        this.onCollisionEnter = onCollisionEnter;
    }

}
