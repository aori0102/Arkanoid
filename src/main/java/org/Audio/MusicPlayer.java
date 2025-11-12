package org.Audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * Concrete implementation of an {@link AudioPlayer} specifically for music playback.
 * <p>
 * This class utilizes the JavaFX {@link MediaPlayer} to handle a single, looping music track.
 * Volume and playback controls are exposed publicly but are intended to be managed
 * exclusively by the static {@link AudioManager} class.
 */
public class MusicPlayer extends AudioPlayer {
    private MediaPlayer mediaPlayer;
    private Media media;
    private MusicAsset.MusicIndex musicIndex;

    /**
     * Constructs a new MusicPlayer and initializes it with the specified music asset.
     * The player registers itself with the {@link AudioManager} upon creation.
     *
     * @param musicIndex The {@link MusicAsset.MusicIndex} representing the music track to play.
     */
    public MusicPlayer(MusicAsset.MusicIndex musicIndex) {
        this.musicIndex = musicIndex;
        setMedia(musicIndex.getMedia());

        AudioManager.addAudioPlayer(this);
        System.out.println("Music Player created: " + musicIndex);
    }

    /**
     * Retrieves the index used to identify this music track.
     *
     * @return The {@link MusicAsset.MusicIndex} associated with this player.
     */
    public MusicAsset.MusicIndex getMusicIndex() {
        return musicIndex;
    }

    /**
     * Initializes the JavaFX {@link Media} object and creates the internal {@link MediaPlayer}.
     * This method is called during construction.
     *
     * @param media The JavaFX {@link Media} asset to be used for playback.
     */
    private void setMedia(Media media) {
        this.media = media;
        if (media == null) {
            System.err.println("[MusicPlayer] Media is null:" + musicIndex);
            return;
        }
        mediaPlayer = new MediaPlayer(media);
    }

    /**
     * Starts or resumes playback of the music track.
     * Implements the abstract method from {@link AudioPlayer}.
     */
    @Override
    public void play() {
        mediaPlayer.play();
    }

    /**
     * Stops playback and resets the track position to the beginning.
     */
    public void stop() {
        mediaPlayer.stop();
    }

    /**
     * Sets the volume level for this music player.
     *
     * @param volume The volume level, usually between 0.0 (silent) and 1.0 (max).
     */
    public void setVolume(double volume) {
        mediaPlayer.setVolume(volume);
    }

    /**
     * Gets the current volume level of the music player.
     *
     * @return The current volume level.
     */
    public double getVolume() {
        return mediaPlayer.getVolume();
    }

    /**
     * Pauses playback of the music track.
     */
    public void pause() {
        mediaPlayer.pause();
    }

    /**
     * Resumes playback from the current position.
     * (Functionally identical to {@link #play()} in this context).
     */
    public void resume() {
        mediaPlayer.play();
    }

    /**
     * Enables or disables continuous looping of the music track.
     * When looping is enabled, an event handler is set to seek to the start
     * and play again when the media reaches its end.
     *
     * @param loop {@code true} to enable looping, {@code false} to disable.
     */
    public void setLoop(boolean loop) {
        if (loop) {
            mediaPlayer.setOnEndOfMedia(() -> {
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
            });
        } else {
            mediaPlayer.setOnEndOfMedia(null);
        }
    }

}
