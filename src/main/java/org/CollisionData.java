package org;

import utils.Vector2;

public class CollisionData {
    /**
     * The collider that was checked.
     */
    public BoxCollider thisCollider = null;

    /**
     * The collider that {@code thisCollider} hits.
     */
    public BoxCollider otherCollider = null;

    /**
     * Whether a collision happens.
     */
    public boolean collided = false;

    /**
     * The position of {@code thisCollider} when it hits
     * {@code otherCollider}.
     */
    public Vector2 contactPoint = null;

    /**
     * The normal of the surface {@code thisCollider} hits.
     */
    public Vector2 hitNormal = null;

    /**
     * Flip all the data for this collision. e.g.
     * two colliders swap places.
     * WARN: The contact point stays the same!
     */
    public CollisionData getInverseData() {
        var flipped = new CollisionData();
        flipped.thisCollider = otherCollider;
        flipped.otherCollider = thisCollider;
        flipped.contactPoint = contactPoint;
        flipped.hitNormal = hitNormal.inverse();
        flipped.collided = collided;
        return flipped;
    }

}