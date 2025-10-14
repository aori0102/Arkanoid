package game;

import game.Voltraxis.VoltraxisPrefab;
import org.GameObjectManager;

public class Init {

    public static void Init_Kine() {

    }

    public static void Init_Dui() {

    }

    public static void Init_Duc() {

    }

    public static void Init_Aori() {
        System.out.println("Making instance of prefab");
        VoltraxisPrefab.instantiate();
    }

}
