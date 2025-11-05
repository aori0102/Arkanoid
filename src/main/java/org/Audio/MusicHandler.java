package org.Audio;

import game.GameManager.GameManager;
import org.Scene.SceneKey;
import org.Scene.SceneManager;

public class MusicHandler {

    public static void initializeMusicHandler() {
        System.out.println("add listener to Music Handler!");
        SceneManager.OnSceneChanged.addListener(MusicHandler::audioHandler_OnSceneChanged);
    }


    public static void audioHandler_OnSceneChanged(Object sender, SceneKey e) {
        System.out.println("Music Handler on scene changed!");
        if (e == SceneKey.Menu) {
            AudioManager.stopMusic();
            AudioManager.setCurrentMusicPlayer(MusicAsset.MusicIndex.MainMenuOST);
            AudioManager.playMusic();
        } else if (e == SceneKey.InGame) {
//            if (!GameManager.getInstance().isBossFight()) {
//                AudioManager.stopMusic();
//                AudioManager.setCurrentMusicPlayer(MusicAsset.MusicIndex.GameOST);
//                AudioManager.playMusic();
//            }

        }
    }
}
