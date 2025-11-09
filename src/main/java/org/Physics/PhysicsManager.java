package org.Physics;

import org.Event.EventActionID;
import org.GameObject.Transform;
import utils.Vector2;

import java.util.*;

/**
 * Central logic class to handle basic physical collision.
 * <p>
 * This manager uses linear interpolation instead of AABB to perform
 * collision checks.
 * </p>
 * <p>
 * Spatial partitioning is also implemented for optimization.
 * </p>
 */
public class PhysicsManager {

    private static final double CELL_PARTITION_SIZE = 32.0;

    private static class CellRange {
        public int fromRow;
        public int toRow;
        public int fromColumn;
        public int toColumn;

        public CellRange(int fromRow, int fromColumn, int toRow, int toColumn) {
            this.fromRow = fromRow;
            this.toRow = toRow;
            this.fromColumn = fromColumn;
            this.toColumn = toColumn;
        }
    }

    private static final HashSet<BoxCollider> colliderSet = new HashSet<>();
    private static final HashMap<BoxCollider, CellRange> registeredCellRange = new HashMap<>();
    private static final HashMap<Long, HashSet<BoxCollider>> spatialPartitioningMap = new HashMap<>();

    private enum BoxColliderChangedEventID {
        Position,
        Scale,
        Center,
        Size,
    }

    private static final HashMap<BoxCollider, EnumMap<BoxColliderChangedEventID, EventActionID>> colliderEventActionIDMap = new HashMap<>();

    /**
     * Register a collider.<br><br>
     * <b><i><u>NOTE</u> : Should only be called within a
     * {@link BoxCollider}'s constructor.</i></b>
     *
     * @param collider The collider to unregister.
     */
    protected static void registerCollider(BoxCollider collider) {
        colliderSet.add(collider);
        EnumMap<BoxColliderChangedEventID, EventActionID> eventActionIDMap = new EnumMap<>(BoxColliderChangedEventID.class);
        eventActionIDMap.put(BoxColliderChangedEventID.Position, collider.getTransform().onPositionChanged.addListener(
                PhysicsManager::boxCollider_onPositionChanged
        ));
        eventActionIDMap.put(BoxColliderChangedEventID.Scale, collider.getTransform().onScaleChanged.addListener(
                PhysicsManager::boxCollider_onScaleChanged
        ));
        eventActionIDMap.put(BoxColliderChangedEventID.Center, collider.onCenterChanged.addListener(
                PhysicsManager::boxCollider_onCenterChanged
        ));
        eventActionIDMap.put(BoxColliderChangedEventID.Size, collider.onSizeChanged.addListener(
                PhysicsManager::boxCollider_onSizeChanged
        ));
        colliderEventActionIDMap.put(collider, eventActionIDMap);

        assignColliderSpatialPartitioning(collider);
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
        var eventActionIDMap = colliderEventActionIDMap.remove(collider);
        collider.getTransform().onPositionChanged.removeListener(
                eventActionIDMap.remove(BoxColliderChangedEventID.Position)
        );
        collider.getTransform().onScaleChanged.removeListener(
                eventActionIDMap.remove(BoxColliderChangedEventID.Scale)
        );
        collider.onCenterChanged.removeListener(
                eventActionIDMap.remove(BoxColliderChangedEventID.Center)
        );
        collider.onSizeChanged.removeListener(
                eventActionIDMap.remove(BoxColliderChangedEventID.Size)
        );
        removeColliderSpatialPartitioning(collider);
    }

    /**
     * Called when {@link org.GameObject.Transform#onPositionChanged} is invoked.<br><br>
     * This function handles changes in collider's position for spatial partitioning.
     *
     * @param sender Event caller {@link BoxCollider}.
     * @param e      Empty event argument.
     */
    private static void boxCollider_onPositionChanged(Object sender, Void e) {
        if (sender instanceof Transform transform) {
            var collider = transform.getComponent(BoxCollider.class);
            removeColliderSpatialPartitioning(collider);
            assignColliderSpatialPartitioning(collider);
        }
    }

    /**
     * Called when {@link org.GameObject.Transform#onScaleChanged} is invoked.<br><br>
     * This function handles changes in collider's position for spatial partitioning.
     *
     * @param sender Event caller {@link BoxCollider}.
     * @param e      Empty event argument.
     */
    private static void boxCollider_onScaleChanged(Object sender, Void e) {
        if (sender instanceof Transform transform) {
            var collider = transform.getComponent(BoxCollider.class);
            removeColliderSpatialPartitioning(collider);
            assignColliderSpatialPartitioning(collider);
        }
    }

    /**
     * Called when {@link BoxCollider#onCenterChanged} is invoked.<br><br>
     * This function handles changes in collider's position for spatial partitioning.
     *
     * @param sender Event caller {@link BoxCollider}.
     * @param e      Empty event argument.
     */
    private static void boxCollider_onCenterChanged(Object sender, Void e) {
        if (sender instanceof BoxCollider collider) {
            removeColliderSpatialPartitioning(collider);
            assignColliderSpatialPartitioning(collider);
        }
    }

    /**
     * Called when {@link BoxCollider#onSizeChanged} is invoked.<br><br>
     * This function handles changes in collider's position for spatial partitioning.
     *
     * @param sender Event caller {@link BoxCollider}.
     * @param e      Empty event argument.
     */
    private static void boxCollider_onSizeChanged(Object sender, Void e) {
        if (sender instanceof BoxCollider collider) {
            removeColliderSpatialPartitioning(collider);
            assignColliderSpatialPartitioning(collider);
        }
    }

    /**
     * Return the corresponding spatial partitioning mapping key based on the provided cell.
     *
     * @param row    The cell's row.
     * @param column The cell's column.
     * @return The corresponding key for the cell.
     */
    private static long getCellKey(int row, int column) {
        return ((long) row << 32) | (column & 0xFFFFFFFFL);
    }

    /**
     * Remove this provided collider's spatial partitioning data.
     *
     * @param collider The collider whose data is to be removed.
     */
    private static void removeColliderSpatialPartitioning(BoxCollider collider) {

        if (registeredCellRange.containsKey(collider)) {
            // Remove collider's current hashing if present
            var cellRange = registeredCellRange.remove(collider);
            for (int row = cellRange.fromRow; row <= cellRange.toRow; row++) {
                for (int column = cellRange.fromColumn; column <= cellRange.toColumn; column++) {
                    var cellKey = getCellKey(row, column);
                    if (!spatialPartitioningMap.containsKey(cellKey) || !spatialPartitioningMap.get(cellKey).remove(collider)) {
                        throw new RuntimeException("Spatial partitioning not detected for collider " + collider);
                    } else {
                        if (spatialPartitioningMap.get(cellKey).isEmpty()) {
                            spatialPartitioningMap.remove(cellKey);
                        }
                    }
                }
            }
        }

    }

    /**
     * Add the provided collider's spatial partitioning data.
     *
     * @param collider The collider whose data is to be added.
     */
    private static void assignColliderSpatialPartitioning(BoxCollider collider) {

        var range = getCellRangeFromBound(collider.getMinBound(), collider.getMaxBound());
        registeredCellRange.put(collider, range);

        for (int row = range.fromRow; row <= range.toRow; row++) {
            for (int column = range.fromColumn; column <= range.toColumn; column++) {

                var assigningCellKey = getCellKey(row, column);

                if (!spatialPartitioningMap.containsKey(assigningCellKey)) {
                    spatialPartitioningMap.put(assigningCellKey, new HashSet<>());
                }
                spatialPartitioningMap.get(assigningCellKey).add(collider);

            }
        }

    }

    /**
     * Get a set of unique {@link BoxCollider}s within a cell range.
     *
     * @param cellRange The range to detect colliders.
     * @return A set of unique {@link BoxCollider}s within {@code cellRange}.
     */
    private static HashSet<BoxCollider> getColliderWithinRange(CellRange cellRange) {
        HashSet<BoxCollider> result = new HashSet<>();
        for (int row = cellRange.fromRow; row <= cellRange.toRow; row++) {
            for (int column = cellRange.fromColumn; column <= cellRange.toColumn; column++) {
                var key = getCellKey(row, column);
                if (spatialPartitioningMap.containsKey(key)) {
                    result.addAll(spatialPartitioningMap.get(key));
                }
            }
        }
        return result;
    }

    /**
     * Get the occupied cells based on the movement and its bounding extents. The return {@link CellRange}
     * is guaranteed to be in correct order, which means min components are not bigger than max components.
     *
     * @param from    The starting point of the movement.
     * @param to      The ending point of the movement.
     * @param extents The bounding extents.
     * @return The occupied {@link CellRange} of the movement.
     */
    private static CellRange getMovementOccupationRange(Vector2 from, Vector2 to, Vector2 extents) {
        var minFrom = from.subtract(extents);
        var minTo = to.subtract(extents);
        var maxFrom = from.add(extents);
        var maxTo = to.add(extents);
        var minCheckingBound = new Vector2(
                Math.min(minFrom.x, minTo.x),
                Math.min(minFrom.y, minTo.y)
        );
        var maxCheckingBound = new Vector2(
                Math.max(maxFrom.x, maxTo.x),
                Math.max(maxFrom.y, maxTo.y)
        );
        return getCellRangeFromBound(minCheckingBound, maxCheckingBound);
    }

    /**
     * Get the occupied cells based on the center point and extents of the static object.
     * The return {@link CellRange} is guaranteed to be in correct order, which means min
     * components are not bigger than max components.
     *
     * @param center  The bounding center point.
     * @param extents The bounding extents.
     * @return The occupied {@link CellRange} of the movement static object.
     */
    private static CellRange getStaticOccupationRange(Vector2 center, Vector2 extents) {
        var minBound = center.subtract(extents);
        var maxBound = center.add(extents);
        var minCheckingBound = new Vector2(
                minBound.x,
                minBound.y
        );
        var maxCheckingBound = new Vector2(
                maxBound.x,
                maxBound.y
        );
        return getCellRangeFromBound(minCheckingBound, maxCheckingBound);
    }

    /**
     * Get the occupied cells based on the bounding provided. The return {@link CellRange}
     * is guaranteed to be in correct order, which means min components are not bigger than
     * max components.
     *
     * @param firstPoint  The first point of the bounding box.
     * @param secondPoint The second point of the bounding box.
     * @return The occupied {@link CellRange} of the bounding.
     */
    private static CellRange getCellRangeFromBound(Vector2 firstPoint, Vector2 secondPoint) {

        var minBound = new Vector2(
                Math.min(firstPoint.x, secondPoint.x),
                Math.min(firstPoint.y, secondPoint.y)
        );
        var maxBound = new Vector2(
                Math.max(firstPoint.x, secondPoint.x),
                Math.max(firstPoint.y, secondPoint.y)
        );

        double epsilon = 0.000001;
        var fromRow = (int) ((minBound.y + epsilon) / CELL_PARTITION_SIZE);
        var fromColumn = (int) ((minBound.x + epsilon) / CELL_PARTITION_SIZE);
        var toRow = (int) ((maxBound.y + epsilon) / CELL_PARTITION_SIZE);
        var toColumn = (int) ((maxBound.x + epsilon) / CELL_PARTITION_SIZE);

        return new CellRange(fromRow, fromColumn, toRow, toColumn);

    }

    /**
     * Validate an object's movement.
     * <p>
     * This function checks for physical contacts between the provided {@code collider} with
     * other objects with {@link BoxCollider}. This function only checks for physical contacts,
     * so any {@link BoxCollider} with {@code isTrigger} enabled will be ignored.
     * </p>
     * <p>
     * Only {@link BoxCollider}s with the same {@link org.Layer.Layer} included within the provided
     * {@link BoxCollider}'s constraint and does not exclude the provided {@link BoxCollider}'s
     * {@link org.Layer.Layer} is queried.
     * </p>
     * <p>
     * This function is based on simple linear interpolation formula. Thus, it can detect collision
     * between fast moving objects and prevent object glitching when using AABB. Although there is
     * slightly more calculation, spatial partition is used to optimize how colliders are queried.
     * </p>
     * <p>
     * <b><i><u>NOTE</u> : Should only be called within {@link org.GameObject.Transform#translate}.
     * </i></b>
     * </p>
     *
     * @param collider The {@link BoxCollider} with movement to process, this collider should
     *                 be non-trigger ({@code isTrigger = false}).
     * @param movement The movement of {@code collider}.
     * @return A {@link CollisionData} object, which holds the information of the collision
     * of the provided {@link BoxCollider}, or {@code null} if there is no collision. See
     * {@link CollisionData} for more info about collision data.
     */
    public static CollisionData handlePhysicsCollision(BoxCollider collider, Vector2 movement) {

        // Only proceed if collider is valid
        if (collider == null || collider.getGameObject().isDestroyed()) {
            return null;
        }

        CollisionData result = null;

        // Define collision movement attributes
        var from = collider.getGlobalCenter();
        var to = from.add(movement);
        var extents = collider.getExtents();
        double collisionTime = Double.MAX_VALUE;
        boolean collided = false;
        BoxCollider hitCollider = null;
        Vector2 hitNormal = null;
        int layerMask = collider.getIncludeLayer();

        // Get cell range to check for
        var cellRange = getMovementOccupationRange(from, to, extents);

        // All checked colliders
        HashSet<BoxCollider> processColliderSet = getColliderWithinRange(cellRange);

        // Process all non-trigger colliders
        for (var other : processColliderSet) {

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
            result.thisCollider = collider;
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
     * Validate an object's movement.
     * <p>
     * This function checks for trigger contact between the queried {@code collider} with
     * other objects with {@link BoxCollider}. This function only checks for trigger contacts,
     * so any {@link BoxCollider} with {@code isTrigger} disabled will be ignored.
     * </p>
     * <p>
     * Only {@link BoxCollider}s with the same {@link org.Layer.Layer} included within the provided
     * {@link BoxCollider}'s constraint and does not exclude the provided {@link BoxCollider}'s
     * {@link org.Layer.Layer} is queried.
     * </p>
     * <p>
     * This function is based on simple linear interpolation formula. Thus, it can detect collision
     * between fast moving objects and prevent object glitching when using AABB. Although there is
     * slightly more calculation, spatial partition is used to optimize how colliders are queried.
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

        // Get cell range
        var cellRange = getMovementOccupationRange(from, to, extents);
        var processColliderSet = getColliderWithinRange(cellRange);

        for (var other : processColliderSet) {

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
     * Get all {@link BoxCollider} that is overlapping the provided collider.
     * <p>
     * Only {@link BoxCollider}s with the same {@link org.Layer.Layer} included within the provided
     * {@link BoxCollider}'s constraint and does not exclude the provided {@link BoxCollider}'s
     * {@link org.Layer.Layer} is queried.
     * </p>
     *
     * @param collider       The collider to be checked for overlapping.
     * @param processTrigger Whether to process trigger {@link BoxCollider}s.
     * @return A list containing all overlapped {@link BoxCollider}s.
     */
    public static List<BoxCollider> getOverlapColliders(BoxCollider collider, boolean processTrigger) {

        List<BoxCollider> result = new ArrayList<>();

        Vector2 center = collider.getGlobalCenter();
        Vector2 extents = collider.getExtents();
        var cellRange = getStaticOccupationRange(center, extents);
        var processColliderSet = getColliderWithinRange(cellRange);
        var layerMask = collider.getIncludeLayer();
        for (var other : processColliderSet) {

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
            if (!processTrigger && other.isTrigger()) {
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

            if (isBoxesOverlap(center, other.getGlobalCenter(), extents, other.getExtents())) {
                result.add(other);
            }
        }

        return result;

    }

    /**
     * Check if two boxes overlap using traditional AABB technique.
     *
     * @param firstCenter   The center point of the first box.
     * @param secondCenter  The center point of the second box.
     * @param firstExtents  The extents of the first box.
     * @param secondExtents The extents of the second box.
     * @return {@code true} if two boxes overlap, otherwise {@code false}.
     */
    private static boolean isBoxesOverlap(
            Vector2 firstCenter,
            Vector2 secondCenter,
            Vector2 firstExtents,
            Vector2 secondExtents) {
        Vector2 firstMin = firstCenter.subtract(firstExtents);
        Vector2 firstMax = firstCenter.add(firstExtents);
        Vector2 secondMin = secondCenter.subtract(secondExtents);
        Vector2 secondMax = secondCenter.add(secondExtents);
        return firstMin.x <= secondMax.x
                && firstMax.x >= secondMin.x
                && firstMin.y <= secondMax.y
                && firstMax.y >= secondMin.y;
    }

    /**
     * Check possible collision on one side of the bounding box.
     * <p>
     * This check follows after calculating the collider's path to determine whether a
     * collision would happen against the side of the other collider (with normal
     * {@code normal}) For example, checking possible collision between collider {@code A}
     * with collider {@code B} on the left side of {@code B}. The surface has a normal
     * equals {@link Vector2#left}, which is {@code (-1, 0)} and is along the {@code X}-axis.
     * That means you have to pass in {@code from}, {@code to}, {@code extents}, {@code min}
     * and {@code max} value along to {@code Y}-axis
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