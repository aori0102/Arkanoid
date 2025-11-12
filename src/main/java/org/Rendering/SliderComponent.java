package org.Rendering;

import javafx.scene.Node;
import javafx.scene.control.Slider;
import org.GameObject.GameObject;
import utils.Vector2;

public class SliderComponent extends Renderable {

    private final Slider slider;

    public SliderComponent(GameObject owner) {
        super(owner);

        this.slider = new Slider();

        // Set initial size
        setSize(new Vector2(400, 50));

        // Load custom styling
        slider.getStylesheets().add(
                getClass().getResource("/CSS/neon_style_slider.css").toExternalForm()
        );

        // Default pivot at center
        setPivot(new Vector2(0.5, 0.5));

        // Listeners for Renderable property changes
        onRenderPositionChanged.addListener(this::updateSliderLayout);
        onRenderSizeChanged.addListener(this::updateSliderLayout);
        onPivotChanged.addListener(this::updateSliderLayout);

        // Listeners for Transform property changes
        getTransform().onPositionChanged.addListener(this::updateSliderLayout);
        getTransform().onScaleChanged.addListener(this::updateSliderLayout);

        // Initial layout update to apply starting values
        updateSliderLayout(null, null);
    }

    /**
     * Returns the root JavaFX node for rendering.
     *
     * @return The underlying {@link Slider} control.
     */
    @Override
    public Node getNode() {
        return slider;
    }

    /**
     * Provides direct access to the underlying JavaFX {@link Slider} control.
     *
     * @return The internal {@link Slider} instance.
     */
    public Slider getSliderNode() {
        return slider;
    }

    /**
     * Updates the slider's layout properties (position, preferred width/height)
     * using the calculated values from the {@link Renderable} base class.
     * <p>
     * This method is triggered whenever the component's position, size, pivot,
     * or the owner's transformation changes.
     *
     * @param sender The object that triggered the event (ignored in this implementation).
     * @param e      The event argument (ignored in this implementation).
     */
    private void updateSliderLayout(Object sender, Void e) {
        Vector2 renderPos = getRenderPosition();
        slider.setLayoutX(renderPos.x);
        slider.setLayoutY(renderPos.y);

        Vector2 renderSize = getRenderSize();
        slider.setPrefWidth(renderSize.x > 0 ? renderSize.x : slider.getPrefWidth());
        slider.setPrefHeight(renderSize.y > 0 ? renderSize.y : slider.getPrefHeight());
    }
}
