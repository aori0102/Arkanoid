package game.UI.Options;

import javafx.scene.control.Slider;
import org.Audio.AudioManager;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Rendering.Renderable;

public class VolumeSlider extends MonoBehaviour {
    private Slider slider;

    public VolumeSlider(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        // Assuming the prefab has a Slider node as one of its Renderables
        for (var comp : getGameObject().getAllComponents()) {
            if (comp instanceof Renderable hasNode && hasNode.getNode() instanceof Slider s) {
                slider = s;
                break;
            }
        }

        if (slider != null) {
            slider.setMin(0);
            slider.setMax(1);
            slider.setValue(AudioManager.getMasterVolume()); // initial volume
            slider.valueProperty().addListener((obs, oldVal, newVal) -> {
                AudioManager.setMasterVolume(newVal.doubleValue());
            });
        }
    }
}

