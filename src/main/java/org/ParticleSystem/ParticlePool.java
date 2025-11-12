package org.ParticleSystem;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParticlePool extends MonoBehaviour {

    private static ParticlePool instance;

    /// The hash map to store the particle
    private final HashMap<ParticleType, List<ParticleObject>> particleHashMap = new HashMap<>();

    public ParticlePool(GameObject owner) {
        super(owner);
        instance = this;
    }

    public static ParticlePool getInstance() {
        return instance;
    }

    /**
     * Will spawn a new particle if the current particle number is not enough. On the contrary, will return
     * a particle.
     * @param particleType: Particle type that we want to get
     * @return a new particle if not having enough particle or an old existed particle
     */
    public ParticleObject getParticle(ParticleType particleType) {
        List<ParticleObject> particles = particleHashMap.computeIfAbsent(particleType, k -> new ArrayList<>());
        for (ParticleObject particle : particles) {
            if (!particle.getGameObject().isActive()) {
                particle.getGameObject().setActive(true);
                return particle;
            }
        }

        ParticleObject particle = GameObjectManager.instantiate("Particle")
                .addComponent(ParticleObject.class);
        particle.setParticleType(particleType);

        SpriteRenderer sr = particle.addComponent(SpriteRenderer.class);
        sr.setImage(particleType.getImageIndex().getImage());
        sr.setPivot(new Vector2(0.5, 0.5));

        particles.add(particle);
        return particle;
    }

    /**
     * Release particle when not using it.
     * @param particle : the particle wanted to release
     */
    public void releaseParticle(ParticleObject particle) {
        particle.getGameObject().setActive(false);
    }
}
