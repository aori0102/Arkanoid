package ecs;

import utils.Vector2;

import java.util.HashSet;

public class PhysicsManager {

    private static final HashSet<BoxCollider> colliderSet = new HashSet<>();

    protected static void RegisterCollider(BoxCollider collider) {
        colliderSet.add(collider);
    }

    protected static void UnregisterCollider(BoxCollider collider) {
        colliderSet.remove(collider);
    }

    protected static CollisionData validateMovement(BoxCollider collider, Vector2 from, Vector2 to) {

        var result = new CollisionData();

        if (collider == null) {
            return result;
        }

        result.thisCollider = collider;

        var movement = to.subtract(from);
        var extents = collider.getExtents();
        double collisionTime = Double.MAX_VALUE;
        boolean collided = false;
        BoxCollider otherCollider = null;
        Vector2 hitNormal = null;

        for (var other : colliderSet) {

            if (otherCollider == collider) {
                continue;
            }

            var minBound = other.getMinBound();
            var maxBound = other.getMaxBound();

            // Check collision top (min y)
            double topCollision = minBound.y - extents.y;
            double topTime = getArrivalTime(from.y, to.y, topCollision);
            Vector2 topNormal = new Vector2(0.0, -1.0);
            if (isCollidable(movement, topNormal) && isArrivalValid(topTime) && topTime < collisionTime) {

                collisionTime = topTime;
                collided = true;
                otherCollider = other;
                hitNormal = topNormal;

            }

            // Check collision left (min x)
            double leftCollision = minBound.x - extents.x;
            double leftTime = getArrivalTime(from.x, to.x, leftCollision);
            Vector2 leftNormal = new Vector2(-1.0, 0.0);
            if (isCollidable(movement, leftNormal) && isArrivalValid(leftTime) && leftTime < collisionTime) {

                collisionTime = leftTime;
                collided = true;
                otherCollider = other;
                hitNormal = leftNormal;

            }

            // Check collision bottom (max y)
            double bottomCollision = maxBound.y + extents.y;
            double bottomTime = getArrivalTime(from.y, to.y, bottomCollision);
            Vector2 bottomNormal = new Vector2(0.0, 1.0);
            if (isCollidable(movement, bottomNormal) && isArrivalValid(bottomTime) && bottomTime < collisionTime) {

                collisionTime = bottomTime;
                collided = true;
                otherCollider = other;
                hitNormal = bottomNormal;

            }

            // Check collision right (max x)
            double rightCollision = maxBound.x + extents.x;
            double rightTime = getArrivalTime(from.x, to.x, rightCollision);
            Vector2 rightNormal = new Vector2(1.0, 0.0);
            if (isCollidable(movement, rightNormal) && isArrivalValid(rightTime) && rightTime < collisionTime) {

                collisionTime = rightTime;
                collided = true;
                otherCollider = other;
                hitNormal = rightNormal;

            }

        }

        if (collided) {

            to = from.add(movement.multiply(collisionTime));

            if (collider.onCollisionEnter != null) {
                collider.onCollisionEnter.accept(otherCollider);
            }

            if (otherCollider.onCollisionEnter != null) {
                otherCollider.onCollisionEnter.accept(collider);
            }

            result.collided = true;
            result.otherCollider = otherCollider;
            result.contactPoint = to;
            result.hitNormal = hitNormal;

        }

        return result;

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
