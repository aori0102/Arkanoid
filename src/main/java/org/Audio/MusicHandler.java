package org.Audio;

import org.Scene.SceneKey;
import org.Scene.SceneManager;

public class MusicHandler {

    public static void initializeMusicHandler() {
        System.out.println("add listener to Music Handler!");
        SceneManager.onSceneChanged.addListener(MusicHandler::audioHandler_OnSceneChanged);
    }


    public static void audioHandler_OnSceneChanged(Object sender, SceneKey e) {
        System.out.println("Music Handler on scene changed!");
        if (e == SceneKey.Menu) {
            AudioManager.stopMusic();
            AudioManager.setCurrentMusicPlayer(MusicAsset.MusicIndex.MainMenuOST);
            AudioManager.playMusic();
        } else if (e == SceneKey.InGame) {
            AudioManager.stopMusic();
            AudioManager.setCurrentMusicPlayer(MusicAsset.MusicIndex.GameOST);
            AudioManager.playMusic();
            // TODO: boss fight event fuck you
        }
    }
}
