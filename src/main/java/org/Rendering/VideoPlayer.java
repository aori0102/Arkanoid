package org.Rendering;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Rendering.Renderable;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.javafx.videosurface.ImageViewVideoSurface;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Unity-like VideoPlayer using VLCJ backend.
 * Provides stable video playback independent of JavaFX Media limitations.
 */
public class VideoPlayer extends Renderable {

    private static final List<EmbeddedMediaPlayer> activePlayers = new ArrayList<>();

    private final StackPane root = new StackPane();
    private final ImageView imageView = new ImageView();

    private final MediaPlayerFactory mediaFactory;
    private final EmbeddedMediaPlayer mediaPlayer;

    private boolean isLoop = false;
    private boolean isMute = false;
    private double currentVolume = 1.0;
    private String currentMediaPath;

    public VideoPlayer(GameObject owner) {
        super(owner);

        mediaFactory = new MediaPlayerFactory();
        mediaPlayer = mediaFactory.mediaPlayers().newEmbeddedMediaPlayer();
        mediaPlayer.videoSurface().set(new ImageViewVideoSurface(imageView));

        imageView.setPreserveRatio(true);
        root.getChildren().add(imageView);

        // Prevent GC and store
        activePlayers.add(mediaPlayer);
    }

    /**
     * Returns the root JavaFX node containing the video view.
     *
     * @return The {@link StackPane} that holds the {@link ImageView} displaying the video.
     */
    @Override
    public Node getNode() {
        return root;
    }

    // ==============================================================
    //                   MEDIA CONTROL METHODS
    // ==============================================================

    /**
     * Loads a video from the specified file path.
     * Note: This only sets the media path; {@link #playVideo()} must be called to start playback.
     *
     * @param resourcePath The file path (absolute or relative) to the video resource.
     */
    public void setVideo(String resourcePath) {
        File file = new File(resourcePath);
        if (!file.exists()) {
            System.err.println("[VideoPlayer] File not found: " + resourcePath);
            return;
        }
        this.currentMediaPath = file.getAbsolutePath();
    }

    /**
     * Starts or resumes video playback from the current media path.
     * If looping is enabled, an event listener is added to restart the video when it finishes.
     */
    public void playVideo() {
        if (currentMediaPath == null) {
            System.err.println("[VideoPlayer] No media set.");
            return;
        }

        if (!mediaPlayer.status().isPlaying()) {
            mediaPlayer.media().play(currentMediaPath);
            mediaPlayer.audio().setVolume((int) (currentVolume * 100));
            mediaPlayer.audio().setMute(isMute);

            if (isLoop) {
                mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
                    @Override
                    public void finished(MediaPlayer player) {
                        Platform.runLater(() -> player.media().play(currentMediaPath));
                    }
                });
            }
        }
    }

    /**
     * Pauses video playback if it is currently playing.
     */
    public void pauseVideo() {
        if (mediaPlayer.status().isPlaying()) {
            mediaPlayer.controls().pause();
        }
    }

    /**
     * Stops video playback and releases the current media.
     */
    public void stopVideo() {
        if (mediaPlayer.status().isPlaying()) {
            mediaPlayer.controls().stop();
        }
    }

    /**
     * Sets whether the video should automatically loop when it finishes.
     * This setting takes effect the next time {@link #playVideo()} is called.
     *
     * @param loop {@code true} to enable looping, {@code false} to disable.
     */
    public void setLoop(boolean loop) {
        this.isLoop = loop;
    }

    /**
     * Sets the playback volume.
     * The volume is clamped between 0.0 (mute) and 1.0 (max volume).
     *
     * @param volume The desired volume level, between 0.0 and 1.0.
     */
    public void setVolume(double volume) {
        this.currentVolume = Math.max(0.0, Math.min(1.0, volume));
        mediaPlayer.audio().setVolume((int) (currentVolume * 100));
    }

    /**
     * Toggles the mute state of the video player.
     *
     * @param mute {@code true} to mute the audio, {@code false} to unmute.
     */
    public void setMute(boolean mute) {
        this.isMute = mute;
        mediaPlayer.audio().setMute(mute);
    }

    /**
     * Sets the maximum display width for the video {@link ImageView}.
     *
     * @param width The fit width in pixels.
     */
    public void setWidth(double width) {
        imageView.setFitWidth(width);
    }

    /**
     * Sets the maximum display height for the video {@link ImageView}.
     *
     * @param height The fit height in pixels.
     */
    public void setHeight(double height) {
        imageView.setFitHeight(height);
    }

    /**
     * Performs clean-up operations, releasing all native VLCJ resources.
     * The video player must not be used after this method is called.
     */
    public void dispose() {
        stopVideo();
        mediaPlayer.release();
        mediaFactory.release();
        activePlayers.remove(mediaPlayer);
    }
}
