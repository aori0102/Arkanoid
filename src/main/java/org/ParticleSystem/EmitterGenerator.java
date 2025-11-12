package org.ParticleSystem;

import org.ParticleSystem.Emitter.EmitTypes;
import org.ParticleSystem.EmitterPrefab.CircleEmitterPrefab;
import org.ParticleSystem.EmitterPrefab.ConeEmitterPrefab;
import org.ParticleSystem.EmitterPrefab.EmitterPrefab;

import java.util.HashMap;

/**
 * A static class used for quick generating an emitter
 */
public final class EmitterGenerator {

    public static final HashMap<EmitTypes, EmitterPrefab> emitterHashMap = new HashMap<>();

    static {
        {
            emitterHashMap.put(EmitTypes.Cone, new ConeEmitterPrefab());
            emitterHashMap.put(EmitTypes.Circle, new CircleEmitterPrefab());
        }
    }

}
