package org.Particle.ParticlesPrefab;

import org.Rendering.ImageAsset;

public enum ParticleType {

    Fire(ImageAsset.ImageIndex.FireBall),
    Blizzard(ImageAsset.ImageIndex.None),
    Energy(ImageAsset.ImageIndex.None);

    private final ImageAsset.ImageIndex imageIndex;

    ParticleType(ImageAsset.ImageIndex imageIndex) {
        this.imageIndex = imageIndex;
    }

    public ImageAsset.ImageIndex getImageIndex() {
        return imageIndex;
    }
}

