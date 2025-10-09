package org;

import javafx.scene.Node;

public interface IRenderable {

    /**
     * Get the JavaFX graphic node for rendering
     * entity.
     *
     * @return The rendering node ({@link Node}).
     */
    Node getNode();

    /**
     * Get the rendering layer of the rendering
     * entity.
     *
     * @return The rendering layer.
     */
    RenderLayer getRenderLayer();

    /**
     * Whether this object is active for rendering.
     *
     * @return {@code true} if renderable, otherwise {@code false}.
     */
    boolean isActiveForRendering();

}
