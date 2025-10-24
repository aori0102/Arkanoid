package org.Physics;

import utils.Vector2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Central logic class to handle basic physical collision.<br><br>
 * This manager uses linear interpolation instead of AABB to perform
 * collision checks.
 */
public class PhysicsManager {

    private static final HashSet<BoxCollider> colliderSet = new HashSet<>();

    /**
     * Register a collider.<br><br>
     * <b><i><u>NOTE</u> : Should only be called within a
     * {@link BoxCollider}'s constructor.</i></b>
     *
     * @param collider The collider to unregister.
     */
    protected static void registerCollider(BoxCollider collider) {
        colliderSet.add(collider);
    }

    /**
     * Unregister a collider.<br><br>
     * <b><i><u>NOTE</u> : Should only be called within
     * {@link BoxCollider#onDestroy}.</i></b>
     *
     * @param collider The collider to unregister.
     */
    protected static void unregisterCollider(BoxCollider collider) {
        colliderSet.remove(collider);
    }

    /**
     * Validate an object's movement.
     * <p>
     * This function checks for physical contacts between the queried {@code collider} with
     * other objects with {@link BoxCollider}. This function only checks for physical contacts,
     * so any {@link BoxCollider} with {@code isTrigger} enabled will be ignored.
     * </p>
     * <p>
     * <b><i><u>NOTE</u> : Should only be called within {@link org.GameObject.Transform#translate}.</i></b>
     * </p>
     *
     * @param collider The {@link BoxCollider} with movement to process, this collider should
     *                 be non-trigger ({@code isTrigger = false}).
     * @param movement The movement of {@code collider}.
     * @return A {@link CollisionData} object, which holds the information of the collision
     * of the queried {@link BoxCollider}, or {@code null} if there is no collision. See
     * {@link CollisionData} for more info about collision parameters.
     */
    public static CollisionData handlePhysicsCollision(BoxCollider collider, Vector2 movement) {

        CollisionData result = null;

        // Only proceed if collider is valid
        if (collider == null || collider.getGameObject().isDestroyed()) {
            return result;
        }

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

            // Skip if processing the same collider
            if (other == collider) {
                continue;
            }

            // Skip if processing a destroyed game object.
            if (other.getGameObject().isDestroyed()) {
                continue;
            }

            // Skip if processing an inactive game object
            if (!other.getGameObject().isActive()) {
                continue;
            }

            // Skip if processing a trigger collider
            if (other.isTrigger()) {
                continue;
            }

            // Skip if the other collider's layer is not included within the collider's constraint
            if ((other.getGameObject().getLayerMask() & layerMask) == 0) {
                continue;
            }

            // Skip if this collider's layer is not included within the other collider's constraint
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

            // Call collision callback on the first collider
            result = new CollisionData();
            result.thisCollider = hitCollider;
            result.otherCollider = hitCollider;
            result.contactPoint = from.add(movement.multiply(collisionTime));
            result.hitNormal = hitNormal;

            if (collider.onCollisionEnter != null) {
                collider.onCollisionEnter.accept(result);
            }

            // The previous callback is out of PhysicsManager's control, which mean
            // within this callback, either one or both objects could be destroyed.
            if (!hitCollider.getGameObject().isDestroyed() && !collider.getGameObject().isDestroyed()) {

                // Call collision callback on the second collider
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
     * <h>
     * Validate an object's movement.
     * </h>
     * <p>
     * This function checks for trigger contact between the queried {@code collider} with
     * other objects with {@link BoxCollider}. This function only checks for trigger contacts,
     * so any {@link BoxCollider} with {@code isTrigger} disabled will be ignored.
     * </p>
     * <p>
     * <b><i><u>NOTE</u> : Should only be called within {@link org.GameObject.Transform#translate}.</i></b>
     * </p>
     *
     * @param collider The {@link BoxCollider} with movement to process, this collider should
     *                 be trigger ({@code isTrigger = true}).
     * @param movement The movement of {@code collider}.
     */
    public static void handleTriggerCollision(BoxCollider collider, Vector2 movement) {

        // Only proceed if this collider is valid
        if (collider == null || collider.getGameObject().isDestroyed()) {
            return;
        }

        // Define variables
        Vector2 from = collider.getGlobalCenter();
        Vector2 to = from.add(movement);
        Vector2 extents = collider.getExtents();
        List<CollisionData> dataList = new ArrayList<>();
        var layerMask = collider.getIncludeLayer();

        for (var other : colliderSet) {

            // Skip if processing the same collider
            if (other == collider) {
                continue;
            }

            // Skip if processing a destroyed object.
            if (other.getGameObject().isDestroyed()) {
                continue;
            }

            // Skip if processing an inactive game object
            if (!other.getGameObject().isActive()) {
                continue;
            }

            // Skip if processing a collider of the same trigger (trigger collision only occurs between a
            // trigger collider and a non-trigger collider, cannot occur when there are two trigger colliers)
            if (other.isTrigger() == collider.isTrigger()) {
                continue;
            }

            // Skip if the other collider's layer is not included within this collider's constraint
            if ((other.getGameObject().getLayerMask() & layerMask) == 0) {
                continue;
            }

            // Skip if this collider's layer is not included within the other collider's constraint
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
     * Check possible collision on one side of the bounding box.
     * <p>
     * This check follows after calculating the collider's path to determine whether a
     * collision would happen with the side with {@code normal} of the other
     * collider. For example, checking possible collision between collider {@code A}
     * with collider {@code B} on the left side of {@code B}. The surface {@code normal}
     * will be {@link Vector2#left()}, which is {@code (-1, 0)} and is along the
     * {@code X}-axis. That means you have to pass in {@code from}, {@code to}, {@code extents},
     * {@code min} and {@code max} value along to {@code Y}-axis
     * </p>
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
            Vector2 movement,
            Vector2 normal,
            double expectedCollisionTime,
            double from,
            double to,
            double extents,
            double min,
            double max) {

        return canCollide(movement, normal) &&
                isArrivalValid(expectedCollisionTime) &&
                isOverlap(getArrivalPoint(from, to, expectedCollisionTime), extents, min, max);

    }

    /**
     * Check if the range from {@code point} extended by {@code extents}
     * overlaps the range {@code [min, max]}.
     *
     * @param point   The point to check.
     * @param extents The extension from {@code point} to either side.
     * @param min     The referenced range minimum point.
     * @param max     The referenced range maximum point.
     * @return {@code true} if the checking range overlaps {@code [min, max]},
     * otherwise {@code false}.
     */
    private static boolean isOverlap(double point, double extents, double min, double max) {
        return min <= point + extents && point - extents <= max;
    }

    /**
     * Get the point going from {@code start} to {@code end} by {@code time}.
     * <p>
     * This function uses linear interpolation math to calculate. {@code time = 0}
     * is the starting point, {@code time = 1} is the ending point.
     * {@code 0 < time < 1} means the point within the segment, and either
     * {@code time < 0} or {@code time > 1} means the point outside the segment.
     * </p>
     *
     * @param start The starting point.
     * @param end   The ending point.
     * @param time  The interpolation time to check, with any points within
     *              segment from {@code start} to {@code end} sees {@code time}
     *              within {@code [0, 1]}.
     * @return The point at {@code time}.
     */
    private static double getArrivalPoint(double start, double end, double time) {
        return (1.0 - time) * start + time * end;
    }

    /**
     * Get the time to reach {@code point} from {@code start} to {@code end}.
     * <p>
     * This function uses linear interpolation math to calculate. {@code time = 0}
     * is the starting point, {@code time = 1} is the ending point.
     * {@code 0 < time < 1} means the point within the segment, and either
     * {@code time < 0} or {@code time > 1} means the point outside the segment.
     * </p>
     *
     * @param start The starting point.
     * @param end   The ending point.
     * @param point The checking point.
     * @return The time upon reaching {@code point} from {@code start} to {@code end}.
     */
    private static double getArrivalTime(double start, double end, double point) {
        return (point - start) / (end - start);
    }

    /**
     * Check if the interpolation time {@code arrivalTime} is valid.
     * <p>
     * A valid interpolation time within this context is within {@code [0, 1]}.
     * Any time lies beyond this range is considered invalid, which means not
     * happening in the current frame.
     * </p>
     *
     * @param arrivalTime The interpolation time to check.
     * @return {@code true} if is a valid interpolation time, {@code false} otherwise.
     */
    private static boolean isArrivalValid(double arrivalTime) {
        return arrivalTime >= 0 && arrivalTime <= 1;
    }

    /**
     * Broad check if {@code movement} can lead to a collision against a surface
     * with {@code normal} based on the direction.
     * <p>
     * The idea is that {@code movement}'s direction must face the surface in order to
     * lead to a potential collision. This means that {@code movement} and the surface's
     * {@code normal} has to be in opposite direction, or their dot product is negative.
     * </p>
     *
     * @param movement The movement to check.
     * @param normal   The normal of the surface to check.
     * @return {@code true} if there is a potential collision, {@code false} otherwise.
     */
    private static boolean canCollide(Vector2 movement, Vector2 normal) {
        return Vector2.dot(movement, normal) < 0;
    }

}