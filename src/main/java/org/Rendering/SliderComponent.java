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

        setSize(new Vector2(400, 50));

        slider.getStylesheets().add(
                getClass().getResource("/CSS/neon_style_slider.css").toExternalForm()
        );

        // Default pivot at center
        setPivot(new Vector2(0.5, 0.5));

        onRenderPositionChanged.addListener(this::updateSliderLayout);
        onRenderSizeChanged.addListener(this::updateSliderLayout);
        onPivotChanged.addListener(this::updateSliderLayout);

        getTransform().onPositionChanged.addListener(this::updateSliderLayout);
        getTransform().onScaleChanged.addListener(this::updateSliderLayout);

        updateSliderLayout(null, null);
    }

    @Override
    public Node getNode() {
        return slider;
    }

    public Slider getSliderNode() {
        return slider;
    }

    /**
     * Update the slider's layout and size based on Renderable and Transform properties.
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
