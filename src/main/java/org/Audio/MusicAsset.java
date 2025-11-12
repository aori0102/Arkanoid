package org.Audio;

import javafx.scene.media.Media;

/**
 * Utility class for managing and preloading music assets using JavaFX {@link Media}.
 * <p>
 * This class defines an enumeration {@link MusicIndex} to catalog all music tracks
 * by their classpath resource path and handles the loading of these resources
 * into JavaFX {@link Media} objects upon initialization.
 */
public class MusicAsset {

    public enum MusicIndex {
        MainMenuOST("/Audio/OST/MainMenuOST.mp3"),
        BossOST("/Audio/OST/BossOST.mp3"),
        GameOST("/Audio/OST/GameOST.mp3"),
        GameOverOST("/Audio/OST/GameOverOST.mp3"),
        None("");

        private Media media;

        MusicIndex(String path) {
            media = null;
            if (path == null || path.isEmpty()) return;
            try {
                System.out.println("[MusicAsset] Loading Music: " + path);
                media = loadMusic(path);
            } catch (RuntimeException e) {
                System.err.println(e.getMessage());
                return;
            }

            System.out.println("[MusicAsset] Loaded Music: " + path);
        }

        /**
         * Retrieves the loaded JavaFX {@link Media} object for this music track.
         *
         * @return The {@link Media} object, or {@code null} if loading failed or if the index is {@link #None}.
         */
        public Media getMedia() {
            return media;
        }
    }

    /**
     * Loads a music resource from the classpath and creates a JavaFX {@link Media} object.
     *
     * @param path The classpath resource path of the music file (e.g., "/Audio/OST/track.mp3").
     * @return A new JavaFX {@link Media} object representing the music file.
     * @throws RuntimeException if the resource is not found or if the {@link Media} object cannot be created.
     */
    public static Media loadMusic(String path) {
        try {
            var resource = MusicAsset.class.getResource(path);
            if (resource == null) {
                throw new RuntimeException("[MusicAsset] Failed to load music (Resource is not found): " + path);
            }

            return new Media(resource.toExternalForm());
        } catch (RuntimeException e) {
            throw new RuntimeException("[MusicAsset] Failed to load at path: " + path, e);
        }
    }

}
