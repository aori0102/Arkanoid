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

/**
 * LaserPrefab is responsible for generating a fully configured Laser obstacle.
 * <p>
 * Responsibilities:
 * - Instantiate Laser obstacle GameObject.
 * - Add LaserStat, LaserDamageDealer, and Laser components.
 * - Setup BoxCollider for collision detection.
 * - Attach a visual sprite renderer.
 * - Attach a particle emitter for laser effect.
 * </p>
 */
public class LaserPrefab extends ObstaclePrefab {

    /**
     * Generates a Laser obstacle with all necessary components and visual/particle effects.
     *
     * @return The fully configured Laser obstacle.
     */
    @Override
    public Obstacle generateObstacle() {

        // Instantiate main Laser GameObject
        var laser = GameObjectManager.instantiate("Laser")
                .addComponent(LaserStat.class)
                .addComponent(LaserDamageDealer.class)
                .addComponent(Laser.class);

        // Add collision detection
        laser.addComponent(BoxCollider.class);

        // Initially inactive and positioned at default coordinates
        laser.getGameObject().setActive(false);
        laser.getTransform().setGlobalPosition(new Vector2(300, 300));

        // Create visual representation as a child of the laser
        var laserVisual = GameObjectManager.instantiate("LaserVisual");
        laserVisual.setParent(laser.getGameObject());
        var spriteRenderer = laserVisual.addComponent(SpriteRenderer.class);
        spriteRenderer.setImage(ImageAsset.ImageIndex.Laser.getImage());
        spriteRenderer.setPivot(new Vector2(0.5, 0.5));

        // Setup particle emitter for the laser
        var laserParticleEmitter = EmitterGenerator.emitterHashMap.get(EmitTypes.Cone)
                .generateEmitter(500, 200, 400, 0.5, 0.9, 60);
        laserParticleEmitter.setParticleType(ParticleType.Laser);
        laserParticleEmitter.setBaseDirection(Vector2.up());
        laserParticleEmitter.getGameObject().setParent(laser.getGameObject());
        laserParticleEmitter.startEmit();

        return laser;

    }
}
