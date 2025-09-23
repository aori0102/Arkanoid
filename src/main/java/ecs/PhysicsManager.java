package ecs;

import utils.Vector2;

import java.sql.SQLSyntaxErrorException;
import java.util.HashSet;

public class PhysicsManager {

    private static final HashSet<BoxCollider> colliderSet = new HashSet<>();

    protected static void RegisterCollider(BoxCollider collider) {
        colliderSet.add(collider);
    }

    protected static void UnregisterCollider(BoxCollider collider) {
        colliderSet.remove(collider);
    }

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
            if (isCollidable(movement, topNormal) &&
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
            if (isCollidable(movement, leftNormal) &&
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
            if (isCollidable(movement, bottomNormal) &&
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
            if (isCollidable(movement, rightNormal) &&
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

    private static boolean isOverlap(double point, double extents, double min, double max) {
        return min <= point + extents && point - extents <= max;
    }

    private static double getArrivalPoint(double start, double end, double hit) {
        return (1.0 - hit) * start + hit * end;
    }

    private static double getArrivalTime(double start, double end, double point) {
        return (point - start) / (end - start);
    }

    private static boolean isArrivalValid(double arrivalTime) {
        return arrivalTime >= 0 && arrivalTime <= 1;
    }

    private static boolean isCollidable(Vector2 movement, Vector2 normal) {
        return Vector2.dot(movement, normal) < 0;
    }

}
