package org.ParticleSystem;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Rendering.SpriteRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParticlePool extends MonoBehaviour {

    private static ParticlePool instance;

    private final HashMap<ParticleType, List<ParticleObject>> particleHashMap = new HashMap<>();

    public ParticlePool(GameObject owner) {
        super(owner);
        instance = this;
    }

    public static ParticlePool getInstance() {
        return instance;
    }

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

        particles.add(particle);
        return particle;
    }

    public void releaseParticle(ParticleObject particle) {
        particle.getGameObject().setActive(false);
    }
}
