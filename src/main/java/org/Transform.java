package org;

import utils.Vector2;

import java.util.HashSet;

public class Transform extends MonoBehaviour {

    private Vector2 _localPosition = Vector2.zero();
    private Vector2 _localScale = Vector2.one();

    protected EventHandler<Void> onPositionChanged = new EventHandler<>(this);
    protected EventHandler<Void> onScaleChanged = new EventHandler<>(this);

    public Transform(GameObject owner) {
        super(owner);
        gameObject.onParentChanged.addListener(this::gameObject_onParentChanged);
    }

    private void gameObject_onParentChanged(Object sender, GameObject.OnParentChangedEventArgs e) {

        if (e.newParent != e.previousParent) {

            if (e.previousParent != null) {
                var parentTransform = e.previousParent.getTransform();
                parentTransform.onPositionChanged.removeListener(this::parent_onPositionChanged);
                parentTransform.onScaleChanged.removeListener(this::parent_onScaleChanged);
            }

            if (e.newParent != null) {
                var parentTransform = e.newParent.getTransform();
                parentTransform.onPositionChanged.addListener(this::parent_onPositionChanged);
                parentTransform.onScaleChanged.addListener(this::parent_onScaleChanged);
            }

        }

        onPositionChanged.invoke(this, null);
        onScaleChanged.invoke(this, null);

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
        var parent = gameObject.getParent();
        if (parent == null) {
            return getLocalPosition();
        }
        return _localPosition.add(parent.getTransform().getGlobalPosition());
    }

    /**
     * Get this object's scale in world space.
     *
     * @return The global scale.
     */
    public Vector2 getGlobalScale() {
        var parent = gameObject.getParent();
        if (parent == null) {
            return getLocalScale();
        }
        return _localScale.scaleUp(parent.getTransform().getGlobalScale());
    }

    /**
     * Set the global position for this object.
     *
     * @param globalPosition The global position.
     */
    public void setGlobalPosition(Vector2 globalPosition) {
        if (gameObject.isDestroyed()) {
            return;
        }
        var parent = gameObject.getParent();
        if (parent == null) {
            setLocalPosition(globalPosition);
        } else {
            setLocalPosition(globalPosition.add(parent.getTransform().getGlobalPosition()));
        }
    }

    /**
     * Set the global scale of this object.
     *
     * @param globalScale The global scale
     */
    public void setGlobalScale(Vector2 globalScale) {
        if (gameObject.isDestroyed()) {
            return;
        }
        var parent = gameObject.getParent();
        if (parent == null) {
            setLocalScale(globalScale);
        } else {
            setLocalScale(globalScale.scaleUp(parent.getTransform().getGlobalScale()));
        }
    }

    /**
     * Set the local position for this object.
     */
    public void setLocalPosition(Vector2 localPosition) {
        if (gameObject.isDestroyed()) {
            return;
        }
        this._localPosition = new Vector2(localPosition);
        onPositionChanged.invoke(this, null);
    }

    /**
     * Set the local scale for this object.
     */
    public void setLocalScale(Vector2 localScale) {
        if (gameObject.isDestroyed()) {
            return;
        }
        this._localScale = new Vector2(localScale);
        onScaleChanged.invoke(this, null);
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {

        Transform newTransform = new Transform(newOwner);
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

    private void parent_onPositionChanged(Object sender, Void e) {
        onPositionChanged.invoke(this, null);
    }

    private void parent_onScaleChanged(Object sender, Void e) {
        onScaleChanged.invoke(this, null);
    }

    @Override
    protected void destroyComponent() {
        _localPosition = null;
        _localScale = null;
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
