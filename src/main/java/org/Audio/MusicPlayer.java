package org.Audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.GameObject.GameObject;

public class MusicPlayer extends AudioPlayer {
    private MediaPlayer mediaPlayer;
    private Media media;
    private MusicAsset.MusicIndex musicIndex;

    public MusicPlayer(MusicAsset.MusicIndex musicIndex) {
        this.musicIndex = musicIndex;
        setMedia(musicIndex.getMedia());

        AudioManager.addAudioPlayer(this);
        System.out.println("Music Player created: " + musicIndex);
    }

    public MusicAsset.MusicIndex getMusicIndex() {
        return musicIndex;
    }

    private void setMedia(Media media) {
        this.media = media;
        if (media == null) {
            System.err.println("[MusicPlayer] Media is null:" + musicIndex);
            return;
        }
        mediaPlayer = new MediaPlayer(media);
    }

    @Override
    public void play() {
        mediaPlayer.play();
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void resume() {
        mediaPlayer.play();
    }

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
