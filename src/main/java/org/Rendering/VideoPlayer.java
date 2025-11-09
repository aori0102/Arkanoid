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

    @Override
    public Node getNode() {
        return root;
    }

    // ==============================================================
    //                   MEDIA CONTROL METHODS
    // ==============================================================

    /** Load video from file or resource path */
    public void setVideo(String resourcePath) {
        File file = new File(resourcePath);
        if (!file.exists()) {
            System.err.println("[VideoPlayer] File not found: " + resourcePath);
            return;
        }
        this.currentMediaPath = file.getAbsolutePath();
    }

    /** Play video */
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

    /** Pause playback */
    public void pauseVideo() {
        if (mediaPlayer.status().isPlaying()) {
            mediaPlayer.controls().pause();
        }
    }

    /** Stop playback */
    public void stopVideo() {
        if (mediaPlayer.status().isPlaying()) {
            mediaPlayer.controls().stop();
        }
    }

    /** Looping behavior */
    public void setLoop(boolean loop) {
        this.isLoop = loop;
    }

    /** Volume control (0.0 - 1.0) */
    public void setVolume(double volume) {
        this.currentVolume = Math.max(0.0, Math.min(1.0, volume));
        mediaPlayer.audio().setVolume((int) (currentVolume * 100));
    }

    /** Mute/unmute */
    public void setMute(boolean mute) {
        this.isMute = mute;
        mediaPlayer.audio().setMute(mute);
    }

    /** Resize view */
    public void setWidth(double width) {
        imageView.setFitWidth(width);
    }

    public void setHeight(double height) {
        imageView.setFitHeight(height);
    }

    /** Clean up */
    public void dispose() {
        stopVideo();
        mediaPlayer.release();
        mediaFactory.release();
        activePlayers.remove(mediaPlayer);
    }
}
