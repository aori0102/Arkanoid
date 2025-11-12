package org.Audio;

import java.util.HashMap;

/**
 * AudioManager is responsible for playing and handling audio in general(Music and SFX).
 * All uses of AudioPlayer (MusicPlayer and SFXPlayer) is handled through here.
 * Don't use the {@link MusicPlayer#play()} or {@link SFXPlayer#play()} or any method that related to these classes.
 * Instead, control the audio in {@link AudioManager} using methods such as
 * {@link #playMusic()}, {@link #playSFX(SFXAsset.SFXIndex)},...
 */
public final class AudioManager {

    private static final HashMap<MusicAsset.MusicIndex, MusicPlayer> musicIndexMusicPlayerHashMap =
            new HashMap<MusicAsset.MusicIndex, MusicPlayer>();
    private static final HashMap<SFXAsset.SFXIndex, SFXPlayer> sfxIndexSFXPlayerHashMap =
            new HashMap<SFXAsset.SFXIndex, SFXPlayer>();

    private static MusicPlayer currentMusicPlayer = null;
    private static double masterVolume = 1;
    private static double musicVolume = 1;
    private static double sfxVolume = 1;


    /**
     * Initializes the AudioManager system.
     * <p>
     * This method iterates through all available music and SFX assets,
     * creates an instance of {@link MusicPlayer} or {@link SFXPlayer} for each,
     * and registers them with the AudioManager.
     * </p>
     * It sets the initial music volume to 0 (usually for a smooth fade-in later).
     */
    public static void initializeAudioManager() {
        System.out.println("[AudioManager] Init AudioManager");

        // Initialize and register all SFX players
        for (var value : SFXAsset.SFXIndex.values()) {
            if (value == SFXAsset.SFXIndex.None) continue;
            new SFXPlayer(value);
        }

        // Initialize and register all Music players
        for (var value : MusicAsset.MusicIndex.values()) {
            if (value == MusicAsset.MusicIndex.None) continue;
            new MusicPlayer(value);
        }

        // Initial setup for music volume state
        setMusicVolume(0);
    }

    /**
     * Sets the global master volume level.
     * This volume acts as a multiplier for all other volume settings (music and SFX).
     * The volume of the {@link #currentMusicPlayer} is immediately updated.
     *
     * @param volume The master volume level (0.0 to 1.0).
     */
    public static void setMasterVolume(double volume) {
        masterVolume = volume;
        if (currentMusicPlayer != null) {
            currentMusicPlayer.setVolume(masterVolume * musicVolume);

        }
    }

    /**
     * Sets the music volume level.
     * This volume is combined with the {@link #masterVolume}.
     * The volume of the {@link #currentMusicPlayer} is immediately updated.
     *
     * @param musicVolume The music volume level (0.0 to 1.0).
     */
    public static void setMusicVolume(double musicVolume) {
        AudioManager.musicVolume = musicVolume;
        if (currentMusicPlayer != null) {
            currentMusicPlayer.setVolume(musicVolume * masterVolume);
        }

    }

    /**
     * Sets the sound effects (SFX) volume level.
     * This volume is combined with the {@link #masterVolume} and applied
     * when a SFX is played.
     *
     * @param sfxVolume The SFX volume level (0.0 to 1.0).
     */
    public static void setSfxVolume(double sfxVolume) {
        AudioManager.sfxVolume = sfxVolume;
    }

    /**
     * Gets the current global master volume level.
     *
     * @return The master volume (0.0 to 1.0).
     */
    public static double getMasterVolume() {
        return masterVolume;
    }

    /**
     * Gets the current music volume level.
     *
     * @return The music volume (0.0 to 1.0).
     */
    public static double getMusicVolume() {
        return musicVolume;
    }

    /**
     * Gets the current sound effects (SFX) volume level.
     *
     * @return The SFX volume (0.0 to 1.0).
     */
    public static double getSfxVolume() {
        return sfxVolume;
    }

    /**
     * Selects and sets the music track that should be active.
     * The selected player is set to loop and its volume is immediately updated.
     * Note: This only sets the player; call {@link #playMusic()} to start playback.
     *
     * @param musicIndex The index of the music track to make current.
     */
    public static void setCurrentMusicPlayer(MusicAsset.MusicIndex musicIndex) {
        currentMusicPlayer = musicIndexMusicPlayerHashMap.get(musicIndex);
        currentMusicPlayer.setLoop(true);
        currentMusicPlayer.setVolume(masterVolume * musicVolume);
    }

    /**
     * Starts playback of the currently selected music track.
     * The volume is applied immediately before playing.
     */
    public static void playMusic() {
        if (currentMusicPlayer == null) {
            System.err.println("[AudioManager] Can't play music because current music player is null");
        }
        System.out.println("[AudioManager] Playing music:" + currentMusicPlayer.getMusicIndex());
        currentMusicPlayer.setVolume(masterVolume * musicVolume);
        currentMusicPlayer.play();
    }

    /**
     * Stops playback of the currently selected music track, if one is playing.
     */
    public static void stopMusic() {
        if (currentMusicPlayer == null) {
            return;
        }
        currentMusicPlayer.stop();
    }

    /**
     * Starts playback of the specified sound effect.
     * The calculated effective volume (SFX Volume * Master Volume) is applied just before playing.
     *
     * @param sfxIndex The index of the SFX that you want to play.
     */
    public static void playSFX(SFXAsset.SFXIndex sfxIndex) {

        var sfx = sfxIndexSFXPlayerHashMap.get(sfxIndex);
        sfx.setVolume(sfxVolume * masterVolume);
        sfx.play();
    }

    /**
     * Stops playback of the specified sound effect.
     * The volume is reapplied (though unnecessary for stopping) and the track is stopped.
     *
     * @param sfxIndex The index of the SFX that you want to stop.
     */
    public static void stopSFX(SFXAsset.SFXIndex sfxIndex) {
        var sfx = sfxIndexSFXPlayerHashMap.get(sfxIndex);
        sfx.setVolume(sfxVolume * masterVolume);
        sfx.stop();
    }

    /**
     * Registers an {@link AudioPlayer} (either {@link MusicPlayer} or {@link SFXPlayer})
     * with the AudioManager's internal maps.
     * This method is intended to be called by the constructors of the player classes.
     *
     * @param audioPlayer The {@link AudioPlayer} instance to be registered.
     */
    public static void addAudioPlayer(AudioPlayer audioPlayer) {
        if (audioPlayer instanceof MusicPlayer musicPlayer) {
            musicIndexMusicPlayerHashMap.put(musicPlayer.getMusicIndex(), musicPlayer);
        } else if (audioPlayer instanceof SFXPlayer sfxPlayer) {
            sfxIndexSFXPlayerHashMap.put(sfxPlayer.getSFXIndex(), sfxPlayer);
        }
    }

}
