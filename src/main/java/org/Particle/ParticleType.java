package org.Particle;

import org.Rendering.ImageAsset;

public enum ParticleType {

    Fire(ImageAsset.ImageIndex.FireParticle),
    Blizzard(ImageAsset.ImageIndex.None),
    Energy(ImageAsset.ImageIndex.EnergyParticle);

    private final ImageAsset.ImageIndex imageIndex;

    ParticleType(ImageAsset.ImageIndex imageIndex) {
        this.imageIndex = imageIndex;
    }

    public ImageAsset.ImageIndex getImageIndex() {
        return imageIndex;
    }
}

