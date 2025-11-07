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


    public static void initializeAudioManager() {
        System.out.println("[AudioManager] Init AudioManager");

        for (var value : SFXAsset.SFXIndex.values()) {
            if (value == SFXAsset.SFXIndex.None) continue;
            new SFXPlayer(value);
        }

        for (var value : MusicAsset.MusicIndex.values()) {
            if (value == MusicAsset.MusicIndex.None) continue;
            new MusicPlayer(value);
        }
        setMusicVolume(0);
        // Delay listener registration until all players are done constructing
        MusicHandler.initializeMusicHandler();
    }

    public static void setMasterVolume(double volume) {
        masterVolume = volume;
        if (currentMusicPlayer != null) {
            currentMusicPlayer.setVolume(masterVolume * musicVolume);

        }
    }

    public static void setMusicVolume(double musicVolume) {
        AudioManager.musicVolume = musicVolume;
        if (currentMusicPlayer != null) {
            currentMusicPlayer.setVolume(musicVolume * masterVolume);
        }

    }

    public static void setSfxVolume(double sfxVolume) {
        AudioManager.sfxVolume = sfxVolume;
    }

    public static double getMasterVolume() {
        return masterVolume;
    }

    public static void setCurrentMusicPlayer(MusicAsset.MusicIndex musicIndex) {
        currentMusicPlayer = musicIndexMusicPlayerHashMap.get(musicIndex);
        currentMusicPlayer.setLoop(true);
        currentMusicPlayer.setVolume(masterVolume * currentMusicPlayer.getVolume());
    }

    public static void playMusic() {
        if (currentMusicPlayer == null) {
            System.err.println("[AudioManager] Can't play music because current music player is null");
        }
        System.out.println("[AudioManager] Playing music:" + currentMusicPlayer.getMusicIndex());
        currentMusicPlayer.setVolume(masterVolume * musicVolume);
        currentMusicPlayer.play();
    }

    public static void stopMusic() {
        if (currentMusicPlayer == null) {
            return;
        }
        currentMusicPlayer.stop();
    }

    /**
     * Play SFX
     *
     * @param sfxIndex index of the SFX that you want to play.
     */
    public static void playSFX(SFXAsset.SFXIndex sfxIndex) {

        var sfx = sfxIndexSFXPlayerHashMap.get(sfxIndex);
        sfx.setVolume(sfxVolume * masterVolume);
        sfx.play();
    }

    // TODO: Stop SFX

    public static void addAudioPlayer(AudioPlayer audioPlayer) {
        if (audioPlayer instanceof MusicPlayer musicPlayer) {
            musicIndexMusicPlayerHashMap.put(musicPlayer.getMusicIndex(), musicPlayer);
        } else if (audioPlayer instanceof SFXPlayer sfxPlayer) {
            sfxIndexSFXPlayerHashMap.put(sfxPlayer.getSFXIndex(), sfxPlayer);
        }
    }

}
