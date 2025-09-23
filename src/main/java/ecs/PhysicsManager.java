package ecs;

import utils.Vector2;

import java.util.HashSet;

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

        if (collider == null) {
            return result;
        }

        result.thisCollider = collider;

        var from = collider.getCenter();
        var to = from.add(movement);
        var extents = collider.getExtents();
        double collisionTime = Double.MAX_VALUE;
        boolean collided = false;
        BoxCollider otherCollider = null;
        Vector2 hitNormal = null;

        for (var other : colliderSet) {

            if (other == collider) {
                continue;
            }

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
                otherCollider = other;
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
                otherCollider = other;
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
                otherCollider = other;
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
                otherCollider = other;
                hitNormal = rightNormal;

            }

        }

        if (collided) {

            to = from.add(movement.multiply(collisionTime));

            result.collided = true;
            result.otherCollider = otherCollider;
            result.contactPoint = to;
            result.hitNormal = hitNormal;

            if (collider.onCollisionEnter != null) {
                collider.onCollisionEnter.accept(result);
            }

            result.contactPoint = otherCollider.getCenter();
            result.hitNormal = hitNormal.inverse();
            result.thisCollider = otherCollider;
            result.otherCollider = collider;

            if (otherCollider.onCollisionEnter != null) {
                otherCollider.onCollisionEnter.accept(result);
            }

        }

        return result;

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
