package org;

import utils.Vector2;

import java.util.function.Function;

public class Transform extends MonoBehaviour {

    private Vector2 localPosition;
    private Vector2 localScale;
    private Transform parent;

    protected Function<Void, Void> onPositionChanged;

    public Transform(GameObject owner) {
        super(owner);
        localPosition = new Vector2();
        localScale = new Vector2(1.0, 1.0);
        parent = null;
    }

    /**
     * Get this object's scale in local space relative to its parent.
     *
     * @return The transform position.
     */
    public Vector2 getLocalPosition() {
        return new Vector2(localPosition);
    }

    /**
     * Get this object's scale in local space relative to its parent.
     *
     * @return The local scale.
     */
    public Vector2 getLocalScale() {
        return new Vector2(localScale);
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
        return localPosition.add(parent.getGlobalPosition());
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
        return localScale.scaleUp(parent.getGlobalScale());
    }

    /**
     * Set the global position for this object.
     *
     * @param globalPosition The global position.
     */
    public void setGlobalPosition(Vector2 globalPosition) {
        if (parent == null) {
            localPosition = globalPosition;
        } else {
            localPosition = globalPosition.subtract(parent.getGlobalPosition());
        }
    }

    /**
     * Set the global scale of this object.
     *
     * @param globalScale The global scale
     */
    public void setGlobalScale(Vector2 globalScale) {
        if (parent == null) {
            localScale = globalScale;
        } else {
            localScale = globalScale.scaleDown(parent.getGlobalScale());
        }
    }

    /**
     * Set the local position for this object.
     */
    public void setLocalPosition(Vector2 localPosition) {
        this.localPosition = new Vector2(localPosition);
    }

    /**
     * Set the local scale for this object.
     */
    public void setLocalScale(Vector2 localScale) {
        this.localScale = new Vector2(localScale);
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {

        Transform newTransform = new Transform(newOwner);
        newTransform.localPosition = this.localPosition;
        newTransform.localScale = this.localScale;
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
            PhysicsManager.checkForTrigger(collider, movement);

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
        }

        this.parent = parent;
        parent.gameObject.addChild(gameObject);
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
        localPosition = null;
        localScale = null;
        parent = null;
    }

}
