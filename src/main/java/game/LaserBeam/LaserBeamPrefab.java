package game.LaserBeam;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Layer.Layer;
import org.ParticleSystem.Emitter.EmitTypes;
import org.ParticleSystem.EmitterGenerator;
import org.ParticleSystem.ParticleType;
import org.Physics.BoxCollider;
import org.Prefab.Prefab;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

public class LaserBeamPrefab extends Prefab {
    @Override
    public GameObject instantiatePrefab() {
        var laserBeamObject = GameObjectManager.instantiate("LaserBeam")
                .addComponent(LaserBeamStat.class)
                .addComponent(LaserBeamDamageDealer.class)
                .addComponent(LaserBeam.class)
                .addComponent(BoxCollider.class)
                .addComponent(SpriteRenderer.class)
                .getGameObject();
        laserBeamObject.setLayer(Layer.Skill);

        // Collider
        var collider = laserBeamObject.getComponent(BoxCollider.class);
        collider.setLocalCenter(new Vector2(0, 0));
        collider.setTrigger(true);
        collider.setLocalSize(new Vector2(32, 256));

        // Renderer
        var spriteRenderer = laserBeamObject.getComponent(SpriteRenderer.class);
        spriteRenderer.setPivot(new Vector2(0.5, 0.5));
        spriteRenderer.setImage(ImageAsset.ImageIndex.LaserBeam.getImage());

        var particleEmitter = EmitterGenerator.emitterHashMap.get(EmitTypes.Cone)
                        .generateEmitter(100, 100, 300, 0.5, 0.6, 30);
        particleEmitter.setBaseDirection(Vector2.down());
        particleEmitter.setParticleType(ParticleType.LaserBeam);
        particleEmitter.getGameObject().setParent(laserBeamObject);
        particleEmitter.startEmit();

        laserBeamObject.getTransform().setLocalScale(new Vector2(0.5, 0.5));

        return laserBeamObject;
    }
}
