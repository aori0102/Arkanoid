package org.Scene.SceneBuilder;

import game.GameManager.GameManager;
import game.GameManager.Score.ScoreManagerPrefab;
import game.GameObject.BallsManager;
import game.GameObject.Border.Border;
import game.GameObject.Border.BorderType;
import game.GameObject.Shield;
import game.MapGenerator.BrickMapManager;
import game.Obstacle.Index.ObstacleManager;
import game.Perks.Index.PerkManager;
import game.Player.Player;
import game.Player.Prefab.PlayerPrefab;
import game.PowerUp.Index.PowerUpManager;
import game.UI.UIManager;
import org.GameObject.GameObjectManager;
import org.Particle.Emitter.ConeEmitter;
import org.Particle.ParticlePool;
import org.Particle.ParticleType;
import utils.Vector2;

public final class InGameSceneBuilder extends SceneBuilder {

    @Override
    protected void build() {
        var shield = GameObjectManager.instantiate("Shield").addComponent(Shield.class);

        GameObjectManager.instantiate("PerkManager").addComponent(PerkManager.class);
        GameObjectManager.instantiate("UIManager").addComponent(UIManager.class);
        GameObjectManager.instantiate("GameManager").addComponent(GameManager.class);
        GameObjectManager.instantiate("BrickMapManager").addComponent(BrickMapManager.class);
        ScoreManagerPrefab.instantiate();

        new PlayerPrefab().instantiatePrefab();

        var ballsManager = GameObjectManager.instantiate("ballManager");
        ballsManager.addComponent(BallsManager.class);
        BallsManager.getInstance().spawnInitialBall();

        var obstacleManager = GameObjectManager.instantiate("obstacleManager");
        obstacleManager.addComponent(ObstacleManager.class);

        var powerUpManager = GameObjectManager.instantiate("powerUpManager");
        powerUpManager.addComponent(PowerUpManager.class);

        //new VoltraxisPrefab().instantiatePrefab();

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

        var circleEmitter1 =  GameObjectManager.instantiate("Circle_Emitter").addComponent(ConeEmitter.class);
        circleEmitter1.setEmissionRate(50);
        circleEmitter1.setSpreadAngle(30);
        circleEmitter1.setMinSpeed(100);
        circleEmitter1.setMaxSpeed(200);
        circleEmitter1.setMinLifeTime(0.1);
        circleEmitter1.setMaxLifeTime(0.3);
        circleEmitter1.setBaseDirection(Vector2.down());
        circleEmitter1.setParticleType(ParticleType.Energy);
        circleEmitter1.getGameObject().setParent(Player.getInstance().getPlayerPaddle().getGameObject());
        circleEmitter1.getTransform().setLocalPosition(new Vector2(38, 10));
        circleEmitter1.startEmit();

        var circleEmitter =  GameObjectManager.instantiate("Circle_Emitter").addComponent(ConeEmitter.class);
        circleEmitter.setEmissionRate(50);
        circleEmitter.setSpreadAngle(30);
        circleEmitter.setMinSpeed(100);
        circleEmitter.setMaxSpeed(200);
        circleEmitter.setMinLifeTime(0.1);
        circleEmitter.setMaxLifeTime(0.3);
        circleEmitter.setBaseDirection(Vector2.down());
        circleEmitter.setParticleType(ParticleType.Energy);
        circleEmitter.getGameObject().setParent(Player.getInstance().getPlayerPaddle().getGameObject());
        circleEmitter.getTransform().setLocalPosition(new Vector2(-40, 10));
        circleEmitter.startEmit();


        ParticlePool particlePool = GameObjectManager.instantiate("ParticlePool").addComponent(ParticlePool.class);

    }

}
