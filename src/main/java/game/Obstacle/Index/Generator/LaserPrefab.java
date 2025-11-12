package game.Obstacle.Index.Generator;

import game.Obstacle.Index.Obstacle;
import game.Obstacle.Laser.Laser;
import game.Obstacle.Laser.LaserDamageDealer;
import game.Obstacle.Laser.LaserStat;
import org.GameObject.GameObjectManager;
import org.ParticleSystem.Emitter.EmitTypes;
import org.ParticleSystem.EmitterGenerator;
import org.ParticleSystem.ParticleType;
import org.Physics.BoxCollider;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

public class LaserPrefab extends ObstaclePrefab {
    @Override
    public Obstacle generateObstacle() {

        var laser = GameObjectManager.instantiate("Laser")
                .addComponent(LaserStat.class)
                .addComponent(LaserDamageDealer.class)
                .addComponent(Laser.class);
        laser.addComponent(BoxCollider.class);
        laser.getGameObject().setActive(false);
        laser.getTransform().setGlobalPosition(new Vector2(300, 300));

        var laserVisual = GameObjectManager.instantiate("LaserVisual");
        laserVisual.setParent(laser.getGameObject());
        laserVisual.addComponent(SpriteRenderer.class).setImage(ImageAsset.ImageIndex.Laser.getImage());
        laserVisual.getComponent(SpriteRenderer.class).setPivot(new Vector2(0.5, 0.5));

        var laserParticleEmitter = EmitterGenerator.emitterHashMap.get(EmitTypes.Cone)
                .generateEmitter(500, 200, 400, 0.5, 0.9, 60);
        laserParticleEmitter.setParticleType(ParticleType.Laser);
        laserParticleEmitter.setBaseDirection(Vector2.up());
        laserParticleEmitter.getGameObject().setParent(laser.getGameObject());
        laserParticleEmitter.startEmit();

        return laser;

    }
}
