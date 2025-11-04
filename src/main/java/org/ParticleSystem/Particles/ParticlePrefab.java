package org.ParticleSystem.Particles;

import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.ParticleSystem.Emitter.ParticleEmitter;
import org.ParticleSystem.ParticleType;
import utils.Vector2;

public class ParticlePrefab extends MonoBehaviour {

    protected ParticleEmitter particleEmitter;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public ParticlePrefab(GameObject owner) {
        super(owner);
    }

    public void startEmit() {
        System.out.println("Starting particle emitter");
        particleEmitter.startEmit();
    }
    public void stopEmit() {
        particleEmitter.stopEmit();
    }

    public void setPosition(Vector2 position) {
        particleEmitter.getTransform().setLocalPosition(position);
    }

    public void setDirection(Vector2 direction) {
        particleEmitter.setBaseDirection(direction);
    }

    public void setParent(GameObject parent) {
        particleEmitter.getGameObject().setParent(parent);
    }

    public void setParticleType(ParticleType particleType) {
        particleEmitter.setParticleType(particleType);
    }
}
