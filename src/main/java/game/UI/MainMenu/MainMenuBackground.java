package game.UI.MainMenu;

import javafx.scene.layout.Background;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Layer.RenderLayer;
import org.Main;
import org.Rendering.SpriteRenderer;
import org.Rendering.VideoAsset;
import org.Rendering.VideoPlayer;
import utils.Vector2;

/**
 * Manages the background video display for the main menu screen.
 * This component initializes a {@link VideoPlayer}, configures it to loop, mute,
 * and scale up slightly to cover the entire stage area, and starts playback.
 */
public class MainMenuBackground extends MonoBehaviour {
    private VideoPlayer videoPlayer;

    private Vector2 pivot = new Vector2(0.5, 0.5);
    private final double SCALE = 1.3;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public MainMenuBackground(GameObject owner) {
        super(owner);

        videoPlayer = owner.addComponent(VideoPlayer.class);
        videoPlayer.setRenderLayer(RenderLayer._2);
        setVideoPlayer(videoPlayer);
    }

    /**
     * Configures the properties of the {@link VideoPlayer} component.
     * Sets the video source, pivot, dimensions, and playback options.
     *
     * @param videoPlayer The {@link VideoPlayer} component to configure.
     */
    private void setVideoPlayer(VideoPlayer videoPlayer) {
        videoPlayer.setVideo(VideoAsset.VideoIndex.MainMenuBackground.getVideoPath());
        videoPlayer.setPivot(pivot);
        videoPlayer.setHeight(Main.STAGE_HEIGHT * SCALE);
        videoPlayer.setWidth(Main.STAGE_WIDTH * SCALE);
        videoPlayer.setLoop(true);
        videoPlayer.setMute(true);
    }

    @Override
    public void start() {
        videoPlayer.playVideo();
    }
}
