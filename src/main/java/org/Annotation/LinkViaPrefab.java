package org.Annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a field as prefab-linking.
 * <p>
 * This means that the field should be null by default and have a dedicated setter
 * function that can be accessed via a specific prefab.
 * </p>
 * <p>
 * The marked field is usually of type {@link org.GameObject.MonoBehaviour} within
 * a child object (aka the one not in charge of centralizing). Do this instead of
 * {@link org.GameObject.GameObject#addComponent} to avoid circle component initialization.
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface LinkViaPrefab {
}
