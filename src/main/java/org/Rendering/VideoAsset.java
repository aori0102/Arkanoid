package org.Rendering;

import javafx.scene.media.Media;

import java.util.EnumMap;

/**
 * Unity-like automatic video preloader.
 * Works just like ImageAsset — videos are loaded automatically when this class is first accessed.
 */
public class VideoAsset {

    public enum VideoIndex {
        /// Main menu
        MainMenuBackground("/UI/MainMenuBackground.mp4");

        public final String mediaPath;

        VideoIndex(String path) {
            this.mediaPath = path;
        }

        public Media getMedia() {
            return VideoAsset.getMedia(this);
        }

    }

    private static final EnumMap<VideoIndex, Media> videoMediaMap = new EnumMap<>(VideoIndex.class);

    private static Media getMedia(VideoIndex index) {
        return videoMediaMap.get(index);
    }

    public static void initializeVideoMedia() {

        for (var index : VideoIndex.values()) {

            Media loaded = null;
            try {
                var resource = VideoAsset.class.getResource(index.mediaPath);
                if (resource == null) {
                    throw new IllegalArgumentException("Video not found at path: " + index.mediaPath);
                }

                loaded = new Media(resource.toExternalForm());
                System.out.println("[VideoAsset] Loaded " + index.mediaPath);

            } catch (Exception e) {
                System.err.println(
                        "[VideoAsset] Failed to load video at index [" + index + "]: " + e.getMessage()
                );
            }

            videoMediaMap.put(index, loaded);

        }

    }

}
