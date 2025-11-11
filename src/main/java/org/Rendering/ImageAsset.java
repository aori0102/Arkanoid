package org.Rendering;

import javafx.scene.image.Image;

public class ImageAsset {

    public enum ImageIndex {

        /// Power ups
        DuplicateBall("/PowerUp/DuplicateBall.png"),
        TriplicateBall("/PowerUp/TriplicateBall.png"),
        Explosive("/explosive.png"),
        FireBallICon("/PowerUp/FireBallIcon.png"),
        FireBall("/PowerUp/FireBall.png"),
        BlizzardBall("/PowerUp/BlizzardBall.png"),
        BlizzardBallIcon("/PowerUp/BlizzardBallIcon.png"),
        LaserBeam("/PowerUp/LaserBeam.png"),
        ShieldIcon("/PowerUp/ShieldIcon.png"),
        HealIcon("/PowerUp/HealIcon.png"),

        /// Player object
        Ball("/ball.png"),
        Paddle("/paddle.png"),

        /// Obstacle
        Laser("/laser.png"),
        Arrow("/Arrow.png"),

        /// Buttons
        GeneralButtons("/UI/GeneralButtons.png"),
        PauseButton("/UI/PauseButton.png"),
        ResumeButton("/UI/ResumeButton.png"),
        GoBackButton("/UI/GoBackButton.png"),

        /// Game Title
        GameTitle("/UI/GameTitle.png"),

        /// Background
        GamePlayBackground_Normal("/UI/GamePlayBackground_Normal.png"),
        GamePlayBackground_Boss("/UI/GamePlayBackground_Boss.png"),
        DimmedBackground("/UI/DimmedBackground.png"),

        /// Perks
        Perks("/UI/Perks.png"),

        /// Voltraxis
        Voltraxis_Anim_Idle("/Voltraxis/Main/Idle/SpriteSheet.png"),
        Voltraxis_Anim_NormalAttack("/Voltraxis/Main/NormalAttack/SpriteSheet.png"),
        Voltraxis_Anim_Charging("/Voltraxis/Main/Charging/Charging.png"),
        Voltraxis_Anim_ChargingEnter("/Voltraxis/Main/Charging/EnterCharging.png"),
        Voltraxis_Anim_UnleashingLaser("/Voltraxis/Main/Charging/UnleashingLaser.png"),
        Voltraxis_ElectricBall("/Voltraxis/Object/ElectricBall.png"),
        Voltraxis_UltimateLaser("/Voltraxis/Object/UltimateLaser/UltimateLaser.png"),
        Voltraxis_PowerCore_Anim_Idle("/Voltraxis/Object/PowerCore/Idle/SpriteSheet.png"),
        Voltraxis_PowerCore_UI_HealthBar_Background("/Voltraxis/UI/PowerCore/Background.png"),
        Voltraxis_PowerCore_UI_HealthBar_Lost("/Voltraxis/UI/PowerCore/FillLost.png"),
        Voltraxis_PowerCore_UI_HealthBar_Remain("/Voltraxis/UI/PowerCore/FillRemain.png"),
        Voltraxis_PowerCore_UI_HealthBar_Outline("/Voltraxis/UI/PowerCore/Outline.png"),
        Voltraxis_UI_HealthBar_Background("/Voltraxis/UI/HealthBar/Background.png"),
        Voltraxis_UI_HealthBar_Lost("/Voltraxis/UI/HealthBar/FillLost.png"),
        Voltraxis_UI_HealthBar_Remain("/Voltraxis/UI/HealthBar/FillRemain.png"),
        Voltraxis_UI_HealthBar_Outline("/Voltraxis/UI/HealthBar/Outline.png"),
        Voltraxis_UI_EffectIcon_AttackIncrement("/Voltraxis/UI/EffectIcons/AttackIncrement.png"),
        Voltraxis_UI_EffectIcon_DefenseIncrement("/Voltraxis/UI/EffectIcons/DefenseIncrement.png"),
        Voltraxis_UI_EffectIcon_SkillCooldownDecrement("/Voltraxis/UI/EffectIcons/SkillCooldownDecrement.png"),
        Voltraxis_UI_EffectIcon_PowerCore("/Voltraxis/UI/EffectIcons/SpecialSkill.png"),
        Voltraxis_UI_EffectIcon_Charging("/Voltraxis/UI/EffectIcons/Charging.png"),
        Voltraxis_UI_EffectIcon_DamageTakenIncrement("/Voltraxis/UI/EffectIcons/DamageTakenIncrement.png"),
        Voltraxis_UI_EffectIcon_DamageTakenDecrement("/Voltraxis/UI/EffectIcons/DamageTakenDecrement.png"),
        Voltraxis_UI_Groggy_Background("/Voltraxis/UI/Groggy/Background.png"),
        Voltraxis_UI_Groggy_Fill("/Voltraxis/UI/Groggy/Fill.png"),
        Voltraxis_UI_Groggy_Outline("/Voltraxis/UI/Groggy/Outline.png"),
        Voltraxis_UI_Charging_Background("/Voltraxis/UI/Charging/Background.png"),
        Voltraxis_UI_Charging_Fill("/Voltraxis/UI/Charging/Fill.png"),
        Voltraxis_UI_Charging_Outline("/Voltraxis/UI/Charging/Outline.png"),

        /// Player health bar
        Player_UI_HealthBar_Background("/Player/UI/HealthBar/Background.png"),
        Player_UI_HealthBar_Fill("/Player/UI/HealthBar/Fill.png"),
        Player_UI_HealthBar_LifeLost("/Player/UI/HealthBar/LifeLost.png"),
        Player_UI_HealthBar_LifeRemain("/Player/UI/HealthBar/LifeRemain.png"),
        Player_UI_HealthBar_Outline("/Player/UI/HealthBar/Outline.png"),

        /// Player scoreboard
        Player_UI_Scoreboard_Background("/Player/UI/Scoreboard/Background.png"),
        Player_UI_Rank_Background("/Player/UI/Scoreboard/Rank/Background.png"),
        Player_UI_Rank_Fill("/Player/UI/Scoreboard/Rank/Fill.png"),
        Player_UI_Rank_Outline("/Player/UI/Scoreboard/Rank/Outline.png"),
        Player_UI_Rank_LevelUpIcon("/Player/UI/Scoreboard/Rank/LevelUpIcon.png"),

        /// Player skill
        Player_UI_Skill_SkillBackground("/Player/UI/Skill/SkillBackground.png"),
        Player_UI_Skill_ChargingRing("/Player/UI/Skill/ChargingRing.png"),
        Player_UI_Skill_NotReadyOverlay("/Player/UI/Skill/NotReadyOverlay.png"),
        Player_UI_Skill_ReadyOverlay("/Player/UI/Skill/ReadyOverlay.png"),
        Player_UI_Skill_ChargingIndicatorNob("/Player/UI/Skill/ChargingIndicatorNob.png"),
        Player_UI_Skill_TooltipBackground("/Player/UI/Skill/TooltipBackground.png"),

        /// Player skill icon
        Player_UI_Skill_Icon_InvincibleSkillIcon("/Player/UI/Skill/Icon/InvincibleSkillIcon.png"),
        Player_UI_Skill_Icon_DashSkillIcon("/Player/UI/Skill/Icon/DashSkillIcon.png"),
        Player_UI_Skill_Icon_LaserBeamSkillIcon("/Player/UI/Skill/Icon/LaserBeamSkillIcon.png"),
        Player_UI_Skill_Icon_UpdraftSkillIcon("/Player/UI/Skill/Icon/UpdraftSkillIcon.png"),

        /// Bricks
        PurpleBrick("/Brick/PurpleBrick.png"),
        OrangeBrick("/Brick/OrangeBrick.png"),
        CyanBrick("/Brick/CyanBrick.png"),
        BrickNormal("/Brick/brick_normal.png"),
        BrickSteel("/Brick/SteelBrick.png"),
        BrickCrazyDiamond("/Brick/DiamondBrick.png"),
        BrickGift("/Brick/GiftBrick.png"),
        BrickReborn("/Brick/RebornBrick.png"),
        BrickRock("/Brick/RockBrick.png"),
        BrickRocket("/Brick/RocketBrick.png"),
        BrickAngel("/Brick/AngelBrick.png"),
        BrickBomb("/Brick/BombBrick.png"),

        /// Effects
        HealthLossVignette("/Effect/HealthLossVignette.png"),

        /// Particle
        PurpleParticle("/Particle/ParticlePurple.png"),
        FireParticle("/Particle/FireParticle.png"),
        EnergyParticle("/Particle/EnergyParticle.png"),
        BallParticle("/Particle/BallParticle.png"),
        ExplodingBrickParticle("/Particle/ExplodingBrickParticle.png"),
        DashParticle("/Particle/DashParticle.png"),

        /// RecordUI
        RecordUI("/UI/RecordUI.png"),

        None("");

        private final Image image;

        ImageIndex(String path) {

            Image loadedImage = null;
            try {
                loadedImage = loadImage(path);
            } catch (RuntimeException e) {
                System.err.println(
                        ImageAsset.class.getSimpleName()
                                + " | Image failed to load at index ["
                                + this + "]: "
                                + e.getMessage()
                );
            }
            image = loadedImage;

        }

        public Image getImage() {
            return image;
        }

    }

    /**
     * Load an image with the specified relative path within
     * {@code resource} folder.
     *
     * @param path The path of the image. All image will be
     *             within the folder 'resources', so {@code path} should
     *             begin with a {@code \}, meaning from the root
     *             of 'resources' folder.
     * @return The loaded image, or {@link Exception} if there is an
     * error.
     */
    private static Image loadImage(String path) throws RuntimeException {

        System.out.println(ImageAsset.class.getSimpleName() + " | Loading image " + path);

        try {

            // Get the resource folder
            java.io.InputStream stream = ImageAsset.class.getResourceAsStream(path);
            if (stream == null) {
                throw new Exception("Path stream is null");
            }
            // Set image and sprite
            return new Image(stream);

        } catch (Exception e) {
            throw new RuntimeException("Image not found: " + e);
        }

    }

}
