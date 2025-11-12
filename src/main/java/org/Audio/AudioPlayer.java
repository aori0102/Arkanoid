package org.Audio;

import javafx.scene.media.Media;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

/**
 * Abstract base class for all audio components in the system (Music and SFX).
 * <p>
 * This class provides a common interface for controlling audio playback,
 * primarily through the required {@link #play()} method. Concrete subclasses
 * like {@code MusicPlayer} and {@code SFXPlayer} implement the actual playback
 * logic using specific media objects.
 * </p>
 * Note: Direct interaction with concrete subclasses should be avoided;
 * control audio via {@link AudioManager}.
 */
public abstract class AudioPlayer {

    public AudioPlayer() {
    }

    /**
     * Constructor that may be used by subclasses to initialize with a specific
     * JavaFX {@link Media} object.
     *
     * @param media The JavaFX {@link Media} object associated with this player.
     */
    public AudioPlayer(Media media) {
    }

    /**
     * Starts or resumes playback of the audio track associated with this player.
     * <p>
     * This is an abstract method and must be implemented by concrete subclasses
     * to perform the specific play operation (e.g., creating and playing a {@code MediaPlayer}).
     * </p>
     */
    public abstract void play();

}
