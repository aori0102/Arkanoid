package org.Physics;

import utils.Vector2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PhysicsManager {

    // TODO: take a closer look

    private static final HashSet<BoxCollider> colliderSet = new HashSet<>();

    /**
     * Register a collider.<br><br>
     * <b>Should only be called within a {@link BoxCollider}'s constructor.</b>
     *
     * @param collider The collider to unregister.
     */
    protected static void RegisterCollider(BoxCollider collider) {
        colliderSet.add(collider);
    }

    /**
     * Register a collider. Should only be called
     * within a {@link BoxCollider}'s destructor.
     *
     * @param collider The collider to unregister.
     */
    protected static void UnregisterCollider(BoxCollider collider) {
        colliderSet.remove(collider);
    }

    /**
     * Get the collision data for collider {@code collider}
     * after movement by a vector {@code movement}.
     * Should only be called from {@code Transform.translate()}.
     *
     * @param collider The collider to check.
     * @param movement The movement of the collider.
     * @return The collision data for the collider.
     */
    public static CollisionData validateMovement(BoxCollider collider, Vector2 movement) {

        var result = new CollisionData();

        // Only proceed if collider is valid
        if (collider == null || collider.getGameObject().isDestroyed()) {
            return result;
        }

        // Set this collider
        result.thisCollider = collider;

        // Define collision movement attributes
        var from = collider.getGlobalCenter();
        var to = from.add(movement);
        var extents = collider.getExtents();
        double collisionTime = Double.MAX_VALUE;
        boolean collided = false;
        BoxCollider hitCollider = null;
        Vector2 hitNormal = null;
        int layerMask = collider.getIncludeLayer();

        // Process all non-trigger colliders
        for (var other : colliderSet) {

            // Skip if processing a destroyed game object.
            if (other.getGameObject().isDestroyed()) {
                continue;
            }

            // Skip if processing an inactive game object
            if (!other.getGameObject().isActive()) {
                continue;
            }

            // Skip if processing the same collider
            if (other == collider) {
                continue;
            }

            // Skip if processing a trigger collider
            if (other.isTrigger()) {
                continue;
            }

            // Skip if processing a non-valid layer
            if ((other.getGameObject().getLayerMask() & layerMask) == 0) {
                continue;
            }

            // Skip if the other collider cannot collide with this object's layer
            if ((other.getIncludeLayer() & collider.getGameObject().getLayerMask()) == 0) {
                continue;
            }

            // Get other's bound
            var minBound = other.getMinBound();
            var maxBound = other.getMaxBound();

            // Check collision top (min y)
            double topCollision = minBound.y - extents.y;
            double topTime = getArrivalTime(from.y, to.y, topCollision);
            Vector2 topNormal = new Vector2(0.0, -1.0);
            if (topTime < collisionTime
                    && checkSide(movement, topNormal, topTime, from.x, to.x, extents.x, minBound.x, maxBound.x)) {

                collisionTime = topTime;
                collided = true;
                hitCollider = other;
                hitNormal = topNormal;

            }

            // Check collision left (min x)
            double leftCollision = minBound.x - extents.x;
            double leftTime = getArrivalTime(from.x, to.x, leftCollision);
            Vector2 leftNormal = new Vector2(-1.0, 0.0);
            if (leftTime < collisionTime
                    && checkSide(movement, leftNormal, leftTime, from.y, to.y, extents.y, minBound.y, maxBound.y)) {

                collisionTime = leftTime;
                collided = true;
                hitCollider = other;
                hitNormal = leftNormal;

            }

            // Check collision bottom (max y)
            double bottomCollision = maxBound.y + extents.y;
            double bottomTime = getArrivalTime(from.y, to.y, bottomCollision);
            Vector2 bottomNormal = new Vector2(0.0, 1.0);
            if (bottomTime < collisionTime
                    && checkSide(movement, bottomNormal, bottomTime, from.x, to.x, extents.x, minBound.x, maxBound.x)) {

                collisionTime = bottomTime;
                collided = true;
                hitCollider = other;
                hitNormal = bottomNormal;

            }

            // Check collision right (max x)
            double rightCollision = maxBound.x + extents.x;
            double rightTime = getArrivalTime(from.x, to.x, rightCollision);
            Vector2 rightNormal = new Vector2(1.0, 0.0);
            if (rightTime < collisionTime
                    && checkSide(movement, rightNormal, rightTime, from.y, to.y, extents.y, minBound.y, maxBound.y)) {

                collisionTime = rightTime;
                collided = true;
                hitCollider = other;
                hitNormal = rightNormal;

            }

        }

        // Finalize collision data if a collision happens
        if (collided && !hitCollider.getGameObject().isDestroyed()) {

            // Call collision on the first collider
            result.collided = true;
            result.otherCollider = hitCollider;
            result.contactPoint = from.add(movement.multiply(collisionTime));
            result.hitNormal = hitNormal;

            if (collider.onCollisionEnter != null) {
                collider.onCollisionEnter.accept(result);
            }

            if (!hitCollider.getGameObject().isDestroyed() && !collider.getGameObject().isDestroyed()) {

                // Call collision on the second collider
                var inverseResult = result.getInverseData();
                inverseResult.contactPoint = hitCollider.getGlobalCenter();

                if (hitCollider.onCollisionEnter != null) {
                    hitCollider.onCollisionEnter.accept(inverseResult);
                }

            }

        }

        return result;

    }

    /**
     * Handle all trigger collision for through {@code collider}'s movement.
     *
     * @param collider The collider to check, this must be a non-trigger collider.
     * @param movement The movement of {@code collider}.
     */
    public static void handleTriggerCollision(BoxCollider collider, Vector2 movement) {

        // Only proceed if this collider is valid
        if (collider == null) {
            return;
        }

        Vector2 from = collider.getGlobalCenter();
        Vector2 to = from.add(movement);
        Vector2 extents = collider.getExtents();
        List<CollisionData> dataList = new ArrayList<>();
        var layerMask = collider.getIncludeLayer();

        for (var other : colliderSet) {

            // Skip if processing a destroyed object.
            if (other.getGameObject().isDestroyed()) {
                continue;
            }

            // Skip if processing an inactive game object
            if (!other.getGameObject().isActive()) {
                continue;
            }

            // Skip if processing the same collider
            if (other == collider) {
                continue;
            }

            // Skip if processing a collider of the same trigger (trigger collision only occurs between a
            // trigger collider and a non-trigger collider)
            if (other.isTrigger() == collider.isTrigger()) {
                continue;
            }

            // Skip if processing a game object with invalid layer
            if ((other.getGameObject().getLayerMask() & layerMask) == 0) {
                continue;
            }

            // Skip if the other collider cannot collide with this object's layer
            if ((other.getIncludeLayer() & collider.getGameObject().getLayerMask()) == 0) {
                continue;
            }

            // Get other bounds
            Vector2 minBound = other.getMinBound();
            Vector2 maxBound = other.getMaxBound();

            // Collision data
            var collisionData = new CollisionData();
            collisionData.thisCollider = collider;
            collisionData.otherCollider = other;
            collisionData.collided = true;
            collisionData.contactPoint = from;

            // Check left surface (max x)
            Vector2 rightNormal = Vector2.right();
            double rightCollision = maxBound.x + extents.x;
            double rightTime = getArrivalTime(from.x, to.x, rightCollision);
            if (checkSide(movement, rightNormal, rightTime, from.y, to.y, extents.y, minBound.y, maxBound.y)) {

                collisionData.hitNormal = rightNormal;
                dataList.add(collisionData);

                continue;

            }

            // Check left surface (min x)
            Vector2 leftNormal = Vector2.left();
            double leftCollision = minBound.x - extents.x;
            double leftTime = getArrivalTime(from.x, to.x, leftCollision);
            if (checkSide(movement, leftNormal, leftTime, from.y, to.y, extents.y, minBound.y, maxBound.y)) {

                collisionData.hitNormal = leftNormal;
                dataList.add(collisionData);

                continue;

            }

            // Check bottom surface (max y)
            Vector2 bottomNormal = Vector2.down();
            double bottomCollision = maxBound.y + extents.y;
            double bottomTime = getArrivalTime(from.y, to.y, bottomCollision);
            if (checkSide(movement, bottomNormal, bottomTime, from.x, to.x, extents.x, minBound.x, maxBound.x)) {

                collisionData.hitNormal = bottomNormal;
                dataList.add(collisionData);

                continue;

            }

            // Check top surface (min y)
            Vector2 topNormal = Vector2.up();
            double topCollision = minBound.y - extents.y;
            double topTime = getArrivalTime(from.y, to.y, topCollision);
            if (checkSide(movement, topNormal, topTime, from.x, to.x, extents.x, minBound.x, maxBound.x)) {

                collisionData.hitNormal = topNormal;
                dataList.add(collisionData);

            }

        }

        for (CollisionData data : dataList) {

            var inverseData = data.getInverseData();

            if (collider.onTriggerEnter != null) {
                collider.onTriggerEnter.accept(data);
            }

            if (collider.getGameObject().isDestroyed()) {
                break;
            }

            if (data.otherCollider.onTriggerEnter != null && !data.otherCollider.getGameObject().isDestroyed()) {
                data.otherCollider.onTriggerEnter.accept(inverseData);
            }

        }

    }

    /**
     * Check possible collision on one side of the bounding box. This check
     * follows after calculating the collider's path and determine whether
     * a collision would happen with the side with normal {@code normal} of
     * the other collider. For example, checking possible collision between
     * collider {@code A} with collider {@code B} on the left side of {@code B}.
     * The surface {@code normal} will be {@code Vector2.left()}, which is {@code (-1, 0)}
     * and is along the {@code X}-axis. That means you have to pass in {@code from}, {@code to},
     * {@code extents}, {@code min} and {@code max} value along to {@code Y}-axis
     *
     * @param movement              The movement of this collider.
     * @param normal                The normal of the surface to check.
     * @param expectedCollisionTime The expected collision time if there is
     *                              ever a collision. This is calculated by interpolation
     *                              along the axis the same direction as {@code normal}.
     * @param from                  The starting point of this collider's movement along the axis perpendicular
     *                              to {@code normal}.
     * @param to                    The end point of this collider's movement alone the axis perpendicular
     *                              to {@code normal}.
     * @param extents               The extents component in the axis perpendicular to {@code normal} of
     *                              this collider.
     * @param min                   The minimum bound component in the axis perpendicular to {@code normal}
     *                              of the other collider.
     * @param max                   The maximum bound component in the axis perpendicular to {@code normal}
     *                              or the other collider.
     * @return {@code true} if there is a collision, otherwise {@code false}.
     */
    private static boolean checkSide(
            Vector2 movement, Vector2 normal,
            double expectedCollisionTime,
            double from, double to,
            double extents, double min, double max) {

        return canCollide(movement, normal) &&
                isArrivalValid(expectedCollisionTime) &&
                isOverlap(getArrivalPoint(from, to, expectedCollisionTime), extents, min, max);

    }

    /**
     * Check if a range overlap a certain ({@code min}, {@code max}).
     *
     * @param point   The point to check.
     * @param extents The interval to both side from center {@code point}.
     * @param min     The range min point.
     * @param max     The range max point.
     * @return {@code true} if the checking range is within ({@code min}, {@code max}),
     */
    private static boolean isOverlap(double point, double extents, double min, double max) {
        return min <= point + extents && point - extents <= max;
    }

    /**
     * Get the point going from {@code start} to {@code end}
     * by time {@code time}.
     *
     * @param start The start point.
     * @param end   The end point.
     * @param time  The time to check.
     * @return The point at time {@code time}.
     */
    private static double getArrivalPoint(double start, double end, double time) {
        return (1.0 - time) * start + time * end;
    }

    /**
     * Get the time to reach {@code point} from {@code start}
     * to {@code end}. With {@code start} being 0 and {@code end}
     * being 1.
     *
     * @param start The start point.
     * @param end   The end point.
     * @param point The check point.
     * @return The time upon reaching {@code point}.
     */
    private static double getArrivalTime(double start, double end, double point) {
        return (point - start) / (end - start);
    }

    /**
     * Check if the interpolation time {@code arrivalTime} is valid
     * (within 0 and 1)
     *
     * @param arrivalTime The interpolation time.
     * @return {@code true} if is a valid interpolation time, {@code false} otherwise.
     */
    private static boolean isArrivalValid(double arrivalTime) {
        return arrivalTime >= 0 && arrivalTime <= 1;
    }

    /**
     * Check if {@code movement} can lead to a collision against surface
     * with normal {@code normal} based on the direction.
     *
     * @param movement The movement to check.
     * @param normal   The normal of the surface to check.
     * @return {@code true} if there is a potential collision, {@code false} otherwise.
     */
    private static boolean canCollide(Vector2 movement, Vector2 normal) {
        return Vector2.dot(movement, normal) < 0;
    }

}
