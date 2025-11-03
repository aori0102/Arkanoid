package org.Particle;

import org.Particle.ParticlesPrefab.FireParticlePrefab;
import org.Particle.ParticlesPrefab.ParticlePrefab;
import org.Particle.ParticlesPrefab.ParticleType;

import java.util.HashMap;

public final class ParticleGenerator {
    public static final HashMap<ParticleType, ParticlePrefab> particlePrefabHashMap = new HashMap<>();

    static {
        {
            particlePrefabHashMap.put(ParticleType.Fire, new FireParticlePrefab());
        }
    }
}
