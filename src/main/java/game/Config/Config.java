package game.Config;

import org.Audio.AudioManager;

import javax.swing.*;

public final class Config {

    private double masterVolume = 1.0;
    private double sfxVolume = 1.0;
    private double musicVolume = 1.0;

    public double getMasterVolume() {
        return masterVolume;
    }

    public void setMasterVolume(double masterVolume) {
        this.masterVolume = masterVolume;
    }

    public double getSfxVolume() {
        return sfxVolume;
    }

    public void setSfxVolume(double sfxVolume) {
        this.sfxVolume = sfxVolume;
    }

    public double getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(double musicVolume) {
        this.musicVolume = musicVolume;
    }

    public void overrideConfig(Config config) {
        masterVolume = config.masterVolume;
        sfxVolume = config.sfxVolume;
        musicVolume = config.musicVolume;
    }

    public void loadToAudioManager() {
        AudioManager.setMusicVolume(musicVolume);
        AudioManager.setMasterVolume(masterVolume);
        AudioManager.setSfxVolume(sfxVolume);
    }

    public void saveFromAudioManager() {
        musicVolume = AudioManager.getMusicVolume();
        sfxVolume = AudioManager.getSfxVolume();
        masterVolume = AudioManager.getMasterVolume();
    }

    @Override
    public String toString() {
        return "Player Config:\n"
                + "- Master Volume : [" + masterVolume + "]\n"
                + "- SFX Volume : [" + sfxVolume + "]\n"
                + "- Music Volume : [" + musicVolume + "]";
    }

}