package org.ParticleSystem;

import org.Rendering.ImageAsset;

public enum ParticleType {

    Fire(ImageAsset.ImageIndex.FireParticle),
    Blizzard(ImageAsset.ImageIndex.None),
    Energy(ImageAsset.ImageIndex.EnergyParticle),
    Ball(ImageAsset.ImageIndex.BallParticle),
    ExplodingBrick(ImageAsset.ImageIndex.ExplodingBrickParticle),
    Dash(ImageAsset.ImageIndex.DashParticle),
    Laser(ImageAsset.ImageIndex.LaserParticle),
    LaserBeam(ImageAsset.ImageIndex.LaserBeamParticle),;

    private final ImageAsset.ImageIndex imageIndex;

    ParticleType(ImageAsset.ImageIndex imageIndex) {
        this.imageIndex = imageIndex;
    }

    public ImageAsset.ImageIndex getImageIndex() {
        return imageIndex;
    }
}

