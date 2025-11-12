package org.Prefab;

import org.GameObject.GameObject;

/**
 * The {@code Prefab} class serves as a blueprint for creating pre-configured
 * {@link GameObject} instances.
 * <p>
 * Subclasses of {@code Prefab} should define the properties and components
 * that make up a specific type of {@link GameObject}, and provide an implementation
 * for {@link #instantiatePrefab()} to generate an instance of that prefab at runtime.
 * </p>
 *
 * <p><b>Usage example:</b></p>
 * <pre>{@code
 * public class EnemyPrefab extends Prefab {
 *     @Override
 *     public GameObject instantiatePrefab() {
 *         GameObject enemy = new GameObject();
 *         enemy.addComponent(new EnemyAI());
 *         enemy.addComponent(new SpriteRenderer("enemy.png"));
 *         return enemy;
 *     }
 * }
 * }</pre>
 *
 */
public abstract class Prefab {
    /**
     * Creates and returns a new instance of the prefab as a {@link GameObject}.
     * <p>
     * Implementations should fully initialize the {@link GameObject} with all
     * required components and default properties.
     * </p>
     *
     * @return a new {@link GameObject} instance representing this prefab
     */
    public abstract GameObject instantiatePrefab();
}
