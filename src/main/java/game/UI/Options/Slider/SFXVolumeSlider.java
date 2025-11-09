package game.UI.Options.Slider;

import org.Audio.AudioManager;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Layer.RenderLayer;
import org.Rendering.SliderComponent;

public class SFXVolumeSlider extends MonoBehaviour {
    private final SliderComponent slider;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public SFXVolumeSlider(GameObject owner) {
        super(owner);

        slider = owner.addComponent(SliderComponent.class);
        slider.setRenderLayer(RenderLayer._16);

    }

    @Override
    public void awake() {
        var sliderNode = slider.getSliderNode();

        if (sliderNode != null) {
            sliderNode.setMin(0);
            sliderNode.setMax(1);
            sliderNode.setValue(AudioManager.getSfxVolume()); // initial volume
            sliderNode.valueProperty()
                    .addListener((obs, oldVal, newVal) -> {
                        AudioManager.setSfxVolume(newVal.doubleValue());
                    });
        }
    }
}
