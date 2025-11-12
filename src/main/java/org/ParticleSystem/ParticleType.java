package org.ParticleSystem;

import javafx.geometry.Rectangle2D;
import org.Rendering.ImageAsset;
import utils.Vector2;

public enum ParticleType {

    Fire(ImageAsset.ImageIndex.FireParticle),
    Blizzard(ImageAsset.ImageIndex.None),
    Energy(ImageAsset.ImageIndex.EnergyParticle),
    Ball(ImageAsset.ImageIndex.BallParticle),
    Smoke(
            ImageAsset.ImageIndex.Voltraxis_Effect_Smoke,
            new Rectangle2D(0, 0, 64, 64),
            new Rectangle2D(64, 0, 64, 64),
            new Rectangle2D(128, 0, 64, 64)
    ),
    ExplodingBrick(ImageAsset.ImageIndex.ExplodingBrickParticle),
    Dash(ImageAsset.ImageIndex.DashParticle),
    Laser(ImageAsset.ImageIndex.LaserParticle),
    LaserBeam(ImageAsset.ImageIndex.LaserBeamParticle),;

    public final ImageAsset.ImageIndex imageIndex;
    public final Rectangle2D[] clips;

    ParticleType(ImageAsset.ImageIndex imageIndex, Rectangle2D... clips) {
        this.imageIndex = imageIndex;
        this.clips = clips;
    }

}

