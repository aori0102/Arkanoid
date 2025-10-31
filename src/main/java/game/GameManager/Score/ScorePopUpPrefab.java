package game.GameManager.Score;

import org.GameObject.GameObjectManager;

public final class ScorePopUpPrefab {

    public static ScorePopUp instantiate() {
        return GameObjectManager.instantiate("ScorePopUpPrefab")
                .addComponent(ScorePopUp.class);
    }

}
