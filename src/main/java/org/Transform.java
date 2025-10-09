package org;

import utils.Vector2;

import java.util.HashSet;

public class Transform extends MonoBehaviour {

    private Vector2 _localPosition;
    private Vector2 _localScale;
    private Transform parent;

    protected EventHandler<Void> onPositionChanged = new EventHandler<>(this);
    protected EventHandler<Void> onScaleChanged = new EventHandler<>(this);

    public Transform(GameObject owner) {
        super(owner);
        _localPosition = new Vector2();
        _localScale = new Vector2(1.0, 1.0);
        parent = null;
    }

    /**
     * Get this object's scale in local space relative to its parent.
     *
     * @return The transform position.
     */
    public Vector2 getLocalPosition() {
        return new Vector2(_localPosition);
    }

    /**
     * Get this object's scale in local space relative to its parent.
     *
     * @return The local scale.
     */
    public Vector2 getLocalScale() {
        return new Vector2(_localScale);
    }

    /**
     * Get this object's position in world space.
     *
     * @return The global position.
     */
    public Vector2 getGlobalPosition() {
        if (parent == null) {
            return getLocalPosition();
        }
        return _localPosition.add(parent.getGlobalPosition());
    }

    /**
     * Get this object's scale in world space.
     *
     * @return The global scale.
     */
    public Vector2 getGlobalScale() {
        if (parent == null) {
            return getLocalScale();
        }
        return _localScale.scaleUp(parent.getGlobalScale());
    }

    /**
     * Set the global position for this object.
     *
     * @param globalPosition The global position.
     */
    public void setGlobalPosition(Vector2 globalPosition) {
        if (parent == null) {
            setLocalPosition(globalPosition);
        } else {
            setLocalPosition(globalPosition.add(parent.getGlobalPosition()));
        }
    }

    /**
     * Set the global scale of this object.
     *
     * @param globalScale The global scale
     */
    public void setGlobalScale(Vector2 globalScale) {
        if (parent == null) {
            setLocalScale(globalScale);
        } else {
            setLocalScale(globalScale.scaleUp(parent.getGlobalScale()));
        }
    }

    /**
     * Set the local position for this object.
     */
    public void setLocalPosition(Vector2 localPosition) {
        this._localPosition = new Vector2(localPosition);
        onPositionChanged.invoke(this, null);
    }

    /**
     * Set the local scale for this object.
     */
    public void setLocalScale(Vector2 localScale) {
        this._localScale = new Vector2(localScale);
        onScaleChanged.invoke(this, null);
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {

        Transform newTransform = new Transform(newOwner);
        newTransform.parent = newOwner.getTransform();
        newTransform.setLocalPosition(this._localPosition);
        newTransform.setLocalScale(this._localScale);
        return newTransform;

    }

    /**
     * Translate this transform by {@code translation}.
     *
     * @param translation The translation (movement).
     */
    public void translate(Vector2 translation) {

        if (translation.equals(Vector2.zero())) {
            return;
        }

        Vector2 destination = getGlobalPosition().add(translation);
        var collider = getComponent(BoxCollider.class);
        if (collider != null) {

            var collisionData = PhysicsManager.validateMovement(collider, translation);
            if (collisionData.collided && !collider.isTrigger) {
                destination = collisionData.contactPoint.subtract(collider.getLocalCenter());
            }

            var movement = destination.subtract(getGlobalPosition());
            PhysicsManager.handleTriggerCollision(collider, movement);

        }

        setGlobalPosition(destination);

    }

    /**
     * Set the parent for this object.
     *
     * @param parent The parent.
     */
    public void setParent(Transform parent) {
        if (parent == this) {
            throw new RuntimeException(gameObject.getName() + " cannot be its own parent!");
        }

        if (this.parent != null) {
            this.parent.gameObject.removeChild(gameObject);
            this.parent.onScaleChanged.removeListener(this::parent_onScaleChanged);
            this.parent.onPositionChanged.removeListener(this::parent_onPositionChanged);
        }

        this.parent = parent;
        parent.gameObject.addChild(gameObject);
        parent.onScaleChanged.addListener(this::parent_onScaleChanged);
        parent.onPositionChanged.addListener(this::parent_onPositionChanged);
    }

    private void parent_onPositionChanged(Object sender, Void e) {
        onPositionChanged.invoke(this, null);
    }

    private void parent_onScaleChanged(Object sender, Void e) {
        onScaleChanged.invoke(this, null);
    }

    /**
     * Get this object's parent.
     *
     * @return The object's parent.
     */
    public Transform getParent() {
        return parent;
    }

    @Override
    protected void destroyComponent() {
        _localPosition = null;
        _localScale = null;
        parent = null;
        onPositionChanged = null;
        onScaleChanged = null;
    }

    /**
     * Get all child transforms of this Transform.
     *
     * @return A copy of all children transforms.
     */
    public HashSet<Transform> getChildren() {
        var children = new HashSet<Transform>();
        for (var child : gameObject.getChildren()) {
            children.add(child.getTransform());
        }
        return children;
    }


}
