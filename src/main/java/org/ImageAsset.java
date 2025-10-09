package org;

import javafx.scene.image.Image;

public class ImageAsset {

    public enum ImageIndex {

        Bocchi("/bocchi.png"),
        Bocchi_Anim("/bocchi_anim.png"),
        Mambo("/mambo.png"),
        DuplicateBall("/duplicate_ball.png"),
        TriplicateBall("/triplicate_ball.png"),
        ElectricBall("/electric_ball.png"),
        Ball("/ball.png"),
        Explosive("/explosive.png");

        private final Image image;

        ImageIndex(String path) {

            Image loadedImage = null;
            try {
                loadedImage = loadImage(path);
            } catch (RuntimeException e) {
                System.out.println(
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
