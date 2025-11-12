package game.Scoreboard;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Layer.RenderLayer;
import org.Prefab.Prefab;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

public final class ScoreboardPrefab extends Prefab {

    private static final Vector2 BACKGROUND_POSITION = new Vector2(-16.0, -16.0);

    @Override
    public GameObject instantiatePrefab() {

        var playerInfoObject = GameObjectManager.instantiate("PlayerInfo");

        // Background
        var backgroundRenderer = GameObjectManager.instantiate("Background")
                .addComponent(SpriteRenderer.class);
        backgroundRenderer.setImage(ImageAsset.ImageIndex.Player_UI_Scoreboard_Background.getImage());
        backgroundRenderer.setRenderLayer(RenderLayer.UI_3);
        backgroundRenderer.getTransform().setGlobalPosition(BACKGROUND_POSITION);
        backgroundRenderer.getGameObject().setParent(playerInfoObject);

        PrefabManager.instantiatePrefab(PrefabIndex.Scoreboard_RankUI)
                .setParent(playerInfoObject);
        PrefabManager.instantiatePrefab(PrefabIndex.Scoreboard_ScoreUI)
                .setParent(playerInfoObject);

        return playerInfoObject;

    }

}