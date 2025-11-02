package org.Audio;

import javafx.scene.media.Media;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

public abstract class AudioPlayer{
    public AudioPlayer(){

    }
    public AudioPlayer(Media media){}
    public abstract void play();

}
