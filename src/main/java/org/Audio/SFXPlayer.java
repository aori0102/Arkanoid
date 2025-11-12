package org.Audio;

import javafx.scene.media.AudioClip;
import org.GameObject.GameObject;

/**
 * Concrete implementation of an {@link AudioPlayer} specifically for Sound Effects (SFX) playback.
 * <p>
 * This class uses the JavaFX {@link AudioClip}, which is ideal for short, rapid,
 * and low-latency sounds like SFX. Unlike {@code MusicPlayer}, this class only
 * manages playback controls suitable for fire-and-forget sounds.
 * </p>
 * Volume and playback controls are intended to be managed exclusively by the static
 * {@link AudioManager} class.
 */
public class SFXPlayer extends AudioPlayer {
    private AudioClip audioClip;
    private SFXAsset.SFXIndex sfxIndex;

    /**
     * Constructs a new SFXPlayer and initializes it with the specified sound effect asset.
     * The player registers itself with the {@link AudioManager} upon creation.
     *
     * @param sfxIndex The {@link SFXAsset.SFXIndex} representing the sound effect track.
     */
    public SFXPlayer(SFXAsset.SFXIndex sfxIndex) {
        this.sfxIndex = sfxIndex;
        setAudio(sfxIndex.getAudioClip());

        AudioManager.addAudioPlayer(this);
    }

    /**
     * Retrieves the index used to identify this sound effect track.
     *
     * @return The {@link SFXAsset.SFXIndex} associated with this player.
     */
    public SFXAsset.SFXIndex getSFXIndex() {
        return sfxIndex;
    }

    /**
     * Sets the volume level for this sound effect player.
     *
     * @param volume The volume level, usually between 0.0 (silent) and 1.0 (max).
     */
    public void setVolume(double volume) {
        audioClip.setVolume(volume);
    }

    /**
     * Starts playback of the sound effect.
     * Implements the abstract method from {@link AudioPlayer}. Since {@link AudioClip}
     * handles concurrent playbacks automatically, this method simply initiates the sound.
     */
    @Override
    public void play() {
        audioClip.play();
    }

    /**
     * Stops any currently playing instances of this sound effect.
     */
    public void stop() {
        audioClip.stop();
    }

    /**
     * Sets the internal {@link AudioClip} instance for this player.
     * This is called during construction to link the player to its asset.
     *
     * @param audioClip The preloaded JavaFX {@link AudioClip} asset.
     */
    protected void setAudio(AudioClip audioClip) {
        this.audioClip = audioClip;
    }
}
