package org.Audio;

import javafx.scene.media.AudioClip;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

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

        // Delay listener registration until all players are done constructing
        MusicHandler.initializeMusicHandler();
    }


    public static void setCurrentMusicPlayer(MusicAsset.MusicIndex musicIndex) {
        currentMusicPlayer = musicIndexMusicPlayerHashMap.get(musicIndex);
        currentMusicPlayer.setLoop(true);
    }

    public static void playMusic() {
        if(currentMusicPlayer == null){
            System.err.println("[AudioManager] Can't play music because current music player is null");
        }
        System.out.println("[AudioManager] Playing music:" + currentMusicPlayer.getMusicIndex());
        currentMusicPlayer.play();
    }

    public static void stopMusic() {
        if(currentMusicPlayer == null){
            return;
        }
        currentMusicPlayer.stop();
    }

    /**
     * Play SFX
     * @param sfxIndex index of the SFX that you want to play.
     */
    public static void playSFX(SFXAsset.SFXIndex sfxIndex) {
        sfxIndexSFXPlayerHashMap.get(sfxIndex).play();
    }

    public static void addAudioPlayer(AudioPlayer audioPlayer) {
        if (audioPlayer instanceof MusicPlayer musicPlayer) {
            musicIndexMusicPlayerHashMap.put(musicPlayer.getMusicIndex(), musicPlayer);
        } else if (audioPlayer instanceof SFXPlayer sfxPlayer) {
            sfxIndexSFXPlayerHashMap.put(sfxPlayer.getSFXIndex(), sfxPlayer);
        }
    }

}
