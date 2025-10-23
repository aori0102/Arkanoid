package org.GameObject;

import org.Event.EventActionID;
import org.Event.EventHandler;
import org.Physics.BoxCollider;
import org.Physics.PhysicsManager;
import utils.Vector2;

import java.util.HashSet;

/**
 * A component responsible for handling an objects position and
 * scale, along with its movement and collision check if attached
 * with a {@link BoxCollider}.
 */
public class Transform extends MonoBehaviour {

    private EventActionID parentPositionChangedEventActionID = null;
    private EventActionID parentScaleChangedEventActionID = null;

    public EventHandler<Void> onPositionChanged = new EventHandler<>(Transform.class);
    public EventHandler<Void> onScaleChanged = new EventHandler<>(Transform.class);

    /**
     * Read-only field. Can only be written to within {@link #setLocalPosition}.
     */
    private Vector2 _localPosition = Vector2.zero();

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
     * Read-only field. Can only be written to within {@link #setLocalScale}.
     */
    private Vector2 _localScale = Vector2.one();

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

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public Transform(GameObject owner) {
        super(owner);
        gameObject.onParentChanged.addListener(this::gameObject_onParentChanged);
    }

    /**
     * Called when {@link GameObject#onParentChanged} is invoked.<br><br>
     * This function relink parent's events and invoke as position and scale might change.
     */
    private void gameObject_onParentChanged(Object sender, GameObject.OnParentChangedEventArgs e) {

        if (e.newParent != e.previousParent) {

            if (e.previousParent != null) {
                var parentTransform = e.previousParent.getTransform();
                parentTransform.onPositionChanged.removeListener(parentPositionChangedEventActionID);
                parentTransform.onScaleChanged.removeListener(parentScaleChangedEventActionID);
            }

            if (e.newParent != null) {
                var parentTransform = e.newParent.getTransform();
                parentPositionChangedEventActionID
                        = parentTransform.onPositionChanged.addListener(this::transform_onPositionChanged);
                parentScaleChangedEventActionID
                        = parentTransform.onScaleChanged.addListener(this::transform_onScaleChanged);
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
            setLocalPosition(globalPosition.subtract(parent.getTransform().getGlobalPosition()));
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
            setLocalScale(globalScale.scaleDown(parent.getTransform().getGlobalScale()));
        }
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
            if (collisionData.collided && !collider.isTrigger()) {
                destination = collisionData.contactPoint.subtract(collider.getLocalCenter());
            }

            var movement = destination.subtract(getGlobalPosition());
            PhysicsManager.handleTriggerCollision(collider, movement);

        }

        setGlobalPosition(destination);

    }

    /**
     * Called when {@link Transform#onPositionChanged} is invoked.<br><br>
     * This function handles changes from its parent's position.
     */
    private void transform_onPositionChanged(Object sender, Void e) {
        onPositionChanged.invoke(this, null);
    }

    /**
     * Called when {@link Transform#onScaleChanged} is invoked.<br><br>
     * This function handles changes from its parent's scale.
     */
    private void transform_onScaleChanged(Object sender, Void e) {
        onScaleChanged.invoke(this, null);
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