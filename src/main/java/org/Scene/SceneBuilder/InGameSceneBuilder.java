package org.Scene.SceneBuilder;

import game.Brick.ExplodingBrickParticleManager;
import game.GameObject.Border.Border;
import game.GameObject.Border.BorderType;
import game.GameObject.Shield.Shield;
import game.PowerUp.Index.PowerUpManager;
import game.UI.GamePlayBackground;
import game.UI.PauseMenu.PauseMenuManager;
import org.GameObject.GameObjectManager;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;
import org.ParticleSystem.ParticlePool;
import utils.Vector2;

public final class InGameSceneBuilder extends SceneBuilder {

    @Override
    protected void build() {
        ParticlePool particlePool = GameObjectManager.instantiate("ParticlePool").addComponent(ParticlePool.class);

        PrefabManager.instantiatePrefab(PrefabIndex.Manager_PerkManager);
        PrefabManager.instantiatePrefab(PrefabIndex.Manager_BrickMapManager);
        PrefabManager.instantiatePrefab(PrefabIndex.Manager_RankManager);
        PrefabManager.instantiatePrefab(PrefabIndex.Manager_ScoreManager);
        PrefabManager.instantiatePrefab(PrefabIndex.Manager_ObstacleManager);
        PrefabManager.instantiatePrefab(PrefabIndex.Manager_BallsManager);
        PrefabManager.instantiatePrefab(PrefabIndex.Manager_LevelManager);
        PrefabManager.instantiatePrefab(PrefabIndex.Shield);
        PrefabManager.instantiatePrefab(PrefabIndex.Manager_GameOverManager);

        PrefabManager.instantiatePrefab(PrefabIndex.Player);
        PrefabManager.instantiatePrefab(PrefabIndex.PlayerStatUIHandler);

        PrefabManager.instantiatePrefab(PrefabIndex.Scoreboard);
        PrefabManager.instantiatePrefab(PrefabIndex.StatBoard);
        PrefabManager.instantiatePrefab(PrefabIndex.SkillSetUI);


        GameObjectManager.instantiate("GamePlayBackground").addComponent(GamePlayBackground.class);

        var powerUpManager = GameObjectManager.instantiate("powerUpManager");
        powerUpManager.addComponent(PowerUpManager.class);

        var borderLeft = GameObjectManager.instantiate("Border_Left");
        borderLeft.addComponent(Border.class).setBorderType(BorderType.BorderLeft);
        borderLeft.getTransform().setLocalPosition(new Vector2(250.0, 0));

        var borderRight = GameObjectManager.instantiate("Border_Right");
        borderRight.addComponent(Border.class).setBorderType(BorderType.BorderRight);
        borderRight.getTransform().setLocalPosition(new Vector2(950.0, 0));

        var borderTop = GameObjectManager.instantiate("Border_Top");
        borderTop.addComponent(Border.class).setBorderType(BorderType.BorderTop);
        borderTop.getTransform().setLocalPosition(new Vector2(0, 5.0));

        var borderBottom = GameObjectManager.instantiate("Border_Bottom");
        borderBottom.addComponent(Border.class).setBorderType(BorderType.BorderBottom);
        borderBottom.getTransform().setLocalPosition(new Vector2(1190, 750));

        GameObjectManager.instantiate("PauseMenuManager").addComponent(PauseMenuManager.class);
        GameObjectManager.instantiate("ExplodingBrickCaller").addComponent(ExplodingBrickParticleManager.class);

    }

}
