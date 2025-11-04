package org.Particle;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Rendering.SpriteRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParticlePool extends MonoBehaviour {

    private static ParticlePool instance;

    private final HashMap<ParticleType, List<Particle>> particleHashMap = new HashMap<>();

    public ParticlePool(GameObject owner) {
        super(owner);
        instance = this;
    }

    public static ParticlePool getInstance() {
        return instance;
    }

    public Particle getParticle(ParticleType particleType) {
        List<Particle> particles = particleHashMap.computeIfAbsent(particleType, k -> new ArrayList<>());
        System.out.println(particles.size());
        for (Particle particle : particles) {
            if (!particle.getGameObject().isActive()) {
                particle.getGameObject().setActive(true);
                return particle;
            }
        }

        Particle particle = GameObjectManager.instantiate("Particle")
                .addComponent(Particle.class);
        particle.setParticleType(particleType);

        SpriteRenderer sr = particle.addComponent(SpriteRenderer.class);
        sr.setImage(particleType.getImageIndex().getImage());

        particles.add(particle);
        return particle;
    }

    public void releaseParticle(Particle particle) {
        particle.getGameObject().setActive(false);
    }
}
