package org.Rendering;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import org.GameObject.GameObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Unity-like VideoPlayer for JavaFX-based engine.
 * Works with preloaded VideoAsset.
 */
public class VideoPlayer extends Renderable {

    // Keep references to prevent GC
    private static final List<MediaPlayer> activePlayers = new ArrayList<>();

    private MediaPlayer activeMediaPlayer;
    private final MediaView mediaView = new MediaView();

    private double currentVolume = 1.0;
    private boolean isMute = false;
    private boolean isLoop = false;

    public VideoPlayer(GameObject owner) {
        super(owner);
        mediaView.setPreserveRatio(true);
    }

    @Override
    public Node getNode() {
        return mediaView;
    }

    /**
     *
     * @param media
     */
    public void setVideo(Media media) {
        if (media == null) {
            System.err.println("[VideoPlayer] Cannot set video: media is null");
            return;
        }

        Platform.runLater(() -> {
            // Dispose old player
            if (activeMediaPlayer != null) {
                activeMediaPlayer.stop();
                activeMediaPlayer.dispose();
                activePlayers.remove(activeMediaPlayer);
            }

            try {
                activeMediaPlayer = new MediaPlayer(media);
                activePlayers.add(activeMediaPlayer);
                mediaView.setMediaPlayer(activeMediaPlayer);

                // Apply current properties
                activeMediaPlayer.setMute(isMute);
                activeMediaPlayer.setVolume(currentVolume);
                enableLooping(isLoop);

                activeMediaPlayer.setOnError(() ->
                        System.err.println("[VideoPlayer] Media error: " + activeMediaPlayer.getError())
                );

            } catch (Exception e) {
                System.err.println("[VideoPlayer] Failed to set video: " + e.getMessage());
            }
        });
    }

    /**
     * Set video using path (optional Unity-like workflow)
     */
    public void setVideo(String resourcePath) {
        var url = getClass().getResource(resourcePath);
        if (url == null) {
            System.err.println("[VideoPlayer] Resource not found: " + resourcePath);
            return;
        }
        Media media = new Media(url.toExternalForm());
        setVideo(media);
    }

    /** Plays the video */
    public void playVideo() {
        if (activeMediaPlayer != null) activeMediaPlayer.play();
    }

    /** Stops the video */
    public void stopVideo() {
        if (activeMediaPlayer != null) activeMediaPlayer.stop();
    }

    /** Pauses the video */
    public void pauseVideo() {
        if (activeMediaPlayer != null) activeMediaPlayer.pause();
    }

    /** Enables/disables looping */
    public void setLoop(boolean loop) {
        isLoop = loop;
        enableLooping(loop);
    }

    private void enableLooping(boolean loop) {
        if (activeMediaPlayer == null) return;
        if (loop) {
            activeMediaPlayer.setOnEndOfMedia(() -> {
                activeMediaPlayer.seek(Duration.ZERO);
                activeMediaPlayer.play();
            });
        } else {
            activeMediaPlayer.setOnEndOfMedia(null);
        }
    }

    /** Sets visible width */
    public void setWidth(double width) {
        mediaView.setFitWidth(width);
    }

    /** Sets visible height */
    public void setHeight(double height) {
        mediaView.setFitHeight(height);
    }

    /** Sets volume (0.0 - 1.0) */
    public void setVolume(double volume) {
        currentVolume = Math.max(0.0, Math.min(1.0, volume));
        if (activeMediaPlayer != null) activeMediaPlayer.setVolume(currentVolume);
    }

    /** Mutes/unmutes */
    public void setMute(boolean mute) {
        isMute = mute;
        if (activeMediaPlayer != null) activeMediaPlayer.setMute(mute);
    }
}
