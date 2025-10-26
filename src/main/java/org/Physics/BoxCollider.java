package org.Physics;

import org.GameObject.GameObject;
import org.Layer.Layer;
import org.GameObject.MonoBehaviour;
import utils.Vector2;

import java.util.function.Consumer;

/**
 * Class that allows physics collision to happen.
 */
public class BoxCollider extends MonoBehaviour {

    protected Consumer<CollisionData> onCollisionEnter = null;
    protected Consumer<CollisionData> onTriggerEnter = null;

    /**
     * Read-only field. Can only be written to via {@link #setLocalCenter}.
     */
    private Vector2 _localCenter;

    /**
     * Set the center for the bounding box in local space.
     *
     * @param center The center in local space.
     */
    public void setLocalCenter(Vector2 center) {
        this._localCenter = center;
    }

    /**
     * Read-only field. Can only be written to via {@link #setLocalSize}.
     */
    private Vector2 _localSize;

    /**
     * Set the size for the bounding box in local space.
     *
     * @param size The size in local space.
     */
    public void setLocalSize(Vector2 size) {
        this._localSize = size;
    }

    private boolean isTrigger = false;
    private int includeLayer = Layer.EVERYTHING;

    public BoxCollider(GameObject owner) {
        super(owner);
        _localCenter = new Vector2(0.0, 0.0);
        _localSize = new Vector2(1.0, 1.0);
        PhysicsManager.registerCollider(this);
        isTrigger = false;
        includeLayer = Layer.EVERYTHING;
    }

    @Override
    protected void onDestroy() {
        PhysicsManager.unregisterCollider(this);
    }

    /**
     * Set the layer mask for collision check.
     *
     * @param layerMask The layers to include in collision check.
     */
    public void setIncludeLayer(int layerMask) {
        includeLayer = layerMask;
    }

    /**
     * Set the excluded layer mask from collision checks
     *
     * @param layerMask The layer mask to exclude from collision checks.
     */
    public void setExcludeLayer(int layerMask) {
        includeLayer &= ~layerMask;
    }

    /**
     * Get the included layers for collision check.
     *
     * @return The included layers for collision check.
     */
    public int getIncludeLayer() {
        return includeLayer;
    }

    /**
     * Get the center of the bounding box in local space.
     *
     * @return The center of the bounding box in local space
     */
    public Vector2 getLocalCenter() {
        return new Vector2(_localCenter);
    }

    /**
     * Get the center of the bounding box in world space.
     *
     * @return The center of the bounding box in world space.
     */
    public Vector2 getGlobalCenter() {
        return getTransform().getGlobalPosition().add(_localCenter);
    }

    /**
     * Get the size of the bounding box in local space.
     *
     * @return The size of the bounding box in local space.
     */
    public Vector2 getLocalSize() {
        return new Vector2(_localSize);
    }

    /**
     * Get the size of the bounding box in world space.
     *
     * @return The size of the bounding box in world space.
     */
    public Vector2 getGlobalSize() {
        return _localSize.scaleUp(getTransform().getGlobalScale());
    }

    /**
     * Get the min bound of the bounding box. This is
     * always equal to {@code center} - {@code extents}.
     *
     * @return The min bound of the bounding box.
     */
    public Vector2 getMinBound() {
        return getGlobalCenter().subtract(getExtents());
    }

    /**
     * Get the max bound of the bounding box. This is
     * always equal to {@code center} + {@code extents}.
     *
     * @return The max bound of the bounding box.
     */
    public Vector2 getMaxBound() {
        return getGlobalCenter().add(getExtents());
    }

    /**
     * Get the extents of the bounding box. This is
     * always equal to twice the {@code size}.
     *
     * @return The extents of the bounding box.
     */
    public Vector2 getExtents() {
        return getGlobalSize().divide(2.0);
    }

    /**
     * Set the callback when collision happens.
     *
     * @param onCollisionEnter The callback when collision happens.
     */
    public void setOnCollisionEnterCallback(Consumer<CollisionData> onCollisionEnter) {
        this.onCollisionEnter = onCollisionEnter;
    }

    /**
     * Set the callback when trigger happens.
     *
     * @param onTriggerEnter The callback when trigger happens.
     */
    public void setOnTriggerEnterCallback(Consumer<CollisionData> onTriggerEnter) {
        this.onTriggerEnter = onTriggerEnter;
    }

    /**
     * Get the trigger property of this box collider.
     *
     * @return The trigger property of this box collider.
     */
    public boolean isTrigger() {
        return isTrigger;
    }

    /**
     * Set the trigger property of this box collider.
     *
     * @param trigger The trigger property to set.
     */
    public void setTrigger(boolean trigger) {
        isTrigger = trigger;
    }

}