package org;

import javafx.scene.image.Image;

public class ImageAsset {

    public enum ImageIndex {

        Bocchi("/bocchi.png"),
        Bocchi_Anim("/bocchi_anim.png"),
        Mambo("/mambo.png"),
        DuplicateBall("/duplicateBall.png"),
        TriplicateBall("/triplicate_ball.png"),
        Ball("/ball.png"),
        GeneralButtons("/GeneralButtons.png"),

        /// Voltraxis
        Voltraxis_Anim_Idle("/Animation/Voltraxis/Idle/SpriteSheet.png"),
        Voltraxis_ElectricBall("/electric_ball.png"),
        Voltraxis_UI_HealthBar_Background("/UI/Voltraxis/HealthBar/Background.png"),
        Voltraxis_UI_HealthBar_Lost("/UI/Voltraxis/HealthBar/FillLost.png"),
        Remain("/UI/Voltraxis/HealthBar/FillRemain.png"),
        Voltraxis_UI_HealthBar_Outline("/UI/Voltraxis/HealthBar/Outline.png"),
        Voltraxis_UI_EffectIcon_Offensive("/UI/Voltraxis/EffectIcons/OffensiveSkill.png"),
        Voltraxis_UI_EffectIcon_Defensive("/UI/Voltraxis/EffectIcons/DefensiveSkill.png"),
        Voltraxis_UI_EffectIcon_Special("/UI/Voltraxis/EffectIcons/SpecialSkill.png"),
        Voltraxis_UI_EffectIcon_Charging("/UI/Voltraxis/EffectIcons/Charging.png"),
        Voltraxis_UI_Groggy_Background("/UI/Voltraxis/Groggy/Background.png"),
        Voltraxis_UI_Groggy_Fill("/UI/Voltraxis/Groggy/Fill.png"),
        Voltraxis_UI_Groggy_Outline("/UI/Voltraxis/Groggy/Outline.png"),

        Paddle("/paddle.png"),
        Laser("/laser.png"),
        Arrow("/arrow.png"),

        Explosive("/explosive.png");

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
