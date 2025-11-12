package game.UI.Options.Slider;

import org.Audio.AudioManager;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Layer.RenderLayer;
import org.Rendering.SliderComponent;

public class MasterVolumeSlider extends MonoBehaviour {
    private SliderComponent slider;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public MasterVolumeSlider(GameObject owner) {
        super(owner);

        slider = owner.addComponent(SliderComponent.class);
        slider.setRenderLayer(RenderLayer.UI_5);
    }

    @Override
    public void awake() {
        var sliderNode = slider.getSliderNode();

        if (sliderNode != null) {
            sliderNode.setMin(0);
            sliderNode.setMax(1);
            sliderNode.setValue(AudioManager.getMasterVolume()); // initial volume
            sliderNode.valueProperty()
                    .addListener((obs, oldVal, newVal) -> {
                        AudioManager.setMasterVolume(newVal.doubleValue());
                    });
        }
    }
}

