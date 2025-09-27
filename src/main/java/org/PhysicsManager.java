package org;

import utils.Vector2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PhysicsManager {

    private static final HashSet<BoxCollider> colliderSet = new HashSet<>();

    /**
     * Register a collider. Should only be called
     * within a {@link BoxCollider}'s constructor.
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
    protected static CollisionData validateMovement(BoxCollider collider, Vector2 movement) {

        var result = new CollisionData();

        // Only proceed if collider is valid
        if (collider == null) {
            return result;
        }

        // Set this collider
        result.thisCollider = collider;

        // Define collision movement attributes
        var from = collider.getCenter();
        var to = from.add(movement);
        var extents = collider.getExtents();
        double collisionTime = Double.MAX_VALUE;
        boolean collided = false;
        BoxCollider hitCollider = null;
        Vector2 hitNormal = null;

        // Process all non-trigger colliders
        for (var other : colliderSet) {

            // Skip if processing the same colliders or collider is trigger or collider is inactive
            if (!other.gameObject.isActive() || other == collider || other.isTrigger) {
                continue;
            }

            // Get other's bound
            var minBound = other.getMinBound();
            var maxBound = other.getMaxBound();

            // Check collision top (min y)
            double topCollision = minBound.y - extents.y;
            double topTime = getArrivalTime(from.y, to.y, topCollision);
            Vector2 topNormal = new Vector2(0.0, -1.0);
            if (canCollide(movement, topNormal) &&
                    isArrivalValid(topTime) &&
                    topTime < collisionTime &&
                    isOverlap(getArrivalPoint(from.x, to.x, topTime), extents.x, minBound.x, maxBound.x)) {

                collisionTime = topTime;
                collided = true;
                hitCollider = other;
                hitNormal = topNormal;

            }

            // Check collision left (min x)
            double leftCollision = minBound.x - extents.x;
            double leftTime = getArrivalTime(from.x, to.x, leftCollision);
            Vector2 leftNormal = new Vector2(-1.0, 0.0);
            if (canCollide(movement, leftNormal) &&
                    isArrivalValid(leftTime) &&
                    leftTime < collisionTime &&
                    isOverlap(getArrivalPoint(from.y, to.y, leftTime), extents.y, minBound.y, maxBound.y)) {

                collisionTime = leftTime;
                collided = true;
                hitCollider = other;
                hitNormal = leftNormal;

            }

            // Check collision bottom (max y)
            double bottomCollision = maxBound.y + extents.y;
            double bottomTime = getArrivalTime(from.y, to.y, bottomCollision);
            Vector2 bottomNormal = new Vector2(0.0, 1.0);
            if (canCollide(movement, bottomNormal) &&
                    isArrivalValid(bottomTime) &&
                    bottomTime < collisionTime &&
                    isOverlap(getArrivalPoint(from.x, to.x, bottomTime), extents.x, minBound.x, maxBound.x)) {

                collisionTime = bottomTime;
                collided = true;
                hitCollider = other;
                hitNormal = bottomNormal;

            }

            // Check collision right (max x)
            double rightCollision = maxBound.x + extents.x;
            double rightTime = getArrivalTime(from.x, to.x, rightCollision);
            Vector2 rightNormal = new Vector2(1.0, 0.0);
            if (canCollide(movement, rightNormal) &&
                    isArrivalValid(rightTime) &&
                    rightTime < collisionTime &&
                    isOverlap(getArrivalPoint(from.y, to.y, rightTime), extents.y, minBound.y, maxBound.y)) {

                collisionTime = rightTime;
                collided = true;
                hitCollider = other;
                hitNormal = rightNormal;

            }

        }

        // Finalize collision data if a collision happens
        if (collided) {

            // Call collision on the first collider
            result.collided = true;
            result.otherCollider = hitCollider;
            result.contactPoint = from.add(movement.multiply(collisionTime));
            result.hitNormal = hitNormal;

            if (collider.onCollisionEnter != null) {
                collider.onCollisionEnter.accept(result);
            }

            // Call collision on the second collider
            var inverseResult = result.getInverseData();
            inverseResult.contactPoint = hitCollider.getCenter();

            if (hitCollider.onCollisionEnter != null) {
                hitCollider.onCollisionEnter.accept(inverseResult);
            }

        }

        return result;

    }

    /**
     * Return the collision data array for all collisions with trigger colliders.
     *
     * @param collider The collider to check, this must be a non-trigger collider.
     * @param movement The movement of {@code collider}.
     * @return The collision data array against trigger colliders.
     */
    protected static CollisionData[] checkForTrigger(BoxCollider collider, Vector2 movement) {

        // Only proceed if this collider is valid
        if (collider == null) {
            return null;
        }

        Vector2 from = collider.getCenter();
        Vector2 to = from.add(movement);
        Vector2 extents = collider.getExtents();
        List<CollisionData> dataList = new ArrayList<CollisionData>();

        for (var other : colliderSet) {

            // Skip if processing the same colliders or other's trigger is not the same (trigger hits
            // non-trigger or vice versa) or other is inactive
            if (!other.gameObject.isActive() || other == collider || other.isTrigger == collider.isTrigger) {
                continue;
            }

            // Get other bounds
            Vector2 minBound = other.getMinBound();
            Vector2 maxBound = other.getMaxBound();

            // Check left surface (max x)
            Vector2 rightNormal = Vector2.right();
            double rightCollision = maxBound.x + extents.x;
            double rightTime = getArrivalTime(from.x, to.x, rightCollision);
            if (canCollide(movement, rightNormal) &&
                    isArrivalValid(rightTime) &&
                    isOverlap(getArrivalPoint(from.y, to.y, rightTime), extents.y, minBound.y, maxBound.y)) {

                var collisionData = new CollisionData();
                collisionData.contactPoint = from;
                collisionData.hitNormal = rightNormal;
                collisionData.thisCollider = collider;
                collisionData.otherCollider = other;
                collisionData.collided = true;
                dataList.add(collisionData);

                continue;

            }

            // Check left surface (min x)
            Vector2 leftNormal = Vector2.left();
            double leftCollision = minBound.x - extents.x;
            double leftTime = getArrivalTime(from.x, to.x, leftCollision);
            if (canCollide(movement, leftNormal) &&
                    isArrivalValid(leftTime) &&
                    isOverlap(getArrivalPoint(from.y, to.y, leftTime), extents.y, minBound.y, maxBound.y)) {

                var collisionData = new CollisionData();
                collisionData.contactPoint = from;
                collisionData.hitNormal = leftNormal;
                collisionData.thisCollider = collider;
                collisionData.otherCollider = other;
                collisionData.collided = true;
                dataList.add(collisionData);

                continue;

            }

            // Check bottom surface (max y)
            Vector2 bottomNormal = Vector2.down();
            double bottomCollision = maxBound.y + extents.y;
            double bottomTime = getArrivalTime(from.y, to.y, bottomCollision);
            if (canCollide(movement, bottomNormal) &&
                    isArrivalValid(bottomTime) &&
                    isOverlap(getArrivalPoint(from.x, to.x, bottomTime), extents.x, minBound.x, maxBound.x)) {

                var collisionData = new CollisionData();
                collisionData.contactPoint = from;
                collisionData.hitNormal = bottomNormal;
                collisionData.thisCollider = collider;
                collisionData.otherCollider = other;
                collisionData.collided = true;
                dataList.add(collisionData);

                continue;

            }

            // Check top surface (min y)
            Vector2 topNormal = Vector2.up();
            double topCollision = minBound.y - extents.y;
            double topTime = getArrivalTime(from.y, to.y, topCollision);
            if (canCollide(movement, topNormal) &&
                    isArrivalValid(topTime) &&
                    isOverlap(getArrivalPoint(from.x, to.x, topTime), extents.x, minBound.x, maxBound.x)) {

                var collisionData = new CollisionData();
                collisionData.contactPoint = from;
                collisionData.hitNormal = topNormal;
                collisionData.thisCollider = collider;
                collisionData.otherCollider = other;
                collisionData.collided = true;
                dataList.add(collisionData);

            }

        }

        CollisionData[] resultArray = new CollisionData[dataList.size()];
        for (int i = 0; i < resultArray.length; i++) {

            var data = dataList.get(i);
            var inverseData = data.getInverseData();

            if (collider.onTriggerEnter != null) {
                collider.onTriggerEnter.accept(data);
            }

            if (data.otherCollider.onTriggerEnter != null) {
                data.otherCollider.onTriggerEnter.accept(inverseData);
            }

            resultArray[i] = data;

        }

        return resultArray;

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
