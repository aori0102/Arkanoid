package game.GameManager;

import org.GameObject.GameObjectManager;

public final class LevelUIPrefab {

    public static LevelUI instantiate() {
        return GameObjectManager.instantiate("LevelUI")
                .addComponent(LevelUI.class);
    }

}