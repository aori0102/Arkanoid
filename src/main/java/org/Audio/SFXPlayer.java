package org.Audio;

import javafx.scene.media.AudioClip;
import org.GameObject.GameObject;

public class SFXPlayer extends AudioPlayer {
    private AudioClip audioClip;
    private SFXAsset.SFXIndex sfxIndex;

    public SFXPlayer(SFXAsset.SFXIndex sfxIndex) {
        this.sfxIndex = sfxIndex;
        setAudio(sfxIndex.getAudioClip());

        AudioManager.addAudioPlayer(this);
    }

    public SFXAsset.SFXIndex getSFXIndex()
    {
        return sfxIndex;
    }
    public void setVolume(double volume)
    {
        audioClip.setVolume(volume);
    }
    @Override
    public void play() {
        audioClip.play();
    }

    protected void setAudio(AudioClip audioClip) {
        this.audioClip = audioClip;
    }
}
