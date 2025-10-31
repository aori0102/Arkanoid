package org.Rendering;

import javafx.scene.media.Media;

/**
 * Unity-like automatic video preloader.
 * Works just like ImageAsset — videos are loaded automatically when this class is first accessed.
 */
public class VideoAsset {

    public enum VideoIndex {
        /// Main menu
        MainMenuBackground("/UI/MainMenuBackground.mp4");

        private final Media media;

        VideoIndex(String path) {
            Media loaded = null;
            try {
                var resource = VideoAsset.class.getResource(path);
                if (resource == null) {
                    throw new IllegalArgumentException("Video not found at path: " + path);
                }

                loaded = new Media(resource.toExternalForm());
                System.out.println("[VideoAsset] Loaded " + path);

            } catch (Exception e) {
                System.err.println(
                        "[VideoAsset] Failed to load video at index [" + this + "]: " + e.getMessage()
                );
            }

            this.media = loaded;
        }

        /**
         * @return The preloaded JavaFX Media instance for this video.
         */
        public Media getMedia() {
            return media;
        }
    }
}
