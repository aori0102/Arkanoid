package game.Audio;

import game.Level.LevelManager;
import game.Level.LevelType;
import org.Audio.AudioManager;
import org.Audio.MusicAsset;
import org.Event.EventActionID;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Scene.SceneKey;
import org.Scene.SceneManager;

public class MusicHandler extends MonoBehaviour {

    private EventActionID musicHandler_onLevelLoaded = null;
    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public MusicHandler(GameObject owner) {
        super(owner);

        SceneManager.onSceneChanged.addListener(MusicHandler::audioHandler_onSceneChanged);
    }


    @Override
    public void start() {
        musicHandler_onLevelLoaded = LevelManager.onAnyLevelLoaded.addListener(MusicHandler::audioHandler_onLevelLoaded);

    }

    @Override
    protected void onDestroy() {
        LevelManager.onAnyLevelLoaded.removeListener(musicHandler_onLevelLoaded);
    }


    public static void audioHandler_onSceneChanged(Object sender, SceneKey e) {
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

    public static void audioHandler_onLevelLoaded(Object sender, LevelManager.OnLevelLoadedEventArgs e) {
        if(e.type == LevelType.Showdown){
            AudioManager.stopMusic();
            AudioManager.setCurrentMusicPlayer(MusicAsset.MusicIndex.BossOST);
            AudioManager.playMusic();
        } else {
            AudioManager.stopMusic();
            AudioManager.setCurrentMusicPlayer(MusicAsset.MusicIndex.GameOST);
            AudioManager.playMusic();
        }
    }

}
