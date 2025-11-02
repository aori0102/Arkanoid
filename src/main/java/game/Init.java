package game;

import org.Scene.SceneKey;
import org.Scene.SceneManager;

public class Init {

    public static void initManager() {
    }

    public static void Init_Kine() {
    }

    public static void Init_Dui() {
//        PerkManager.getInstance().instantiatePerks();

    }

    public static void Init_Duc() {

    }

    public static void Init_Aori() {

        //VoltraxisPrefab.instantiate();

    }

    public static void initMenu() {

        SceneManager.loadScene(SceneKey.Menu);
    }

    public static void initGame() {

        SceneManager.loadScene(SceneKey.InGame);

    }

    public static void initRecord() {

    }

}
