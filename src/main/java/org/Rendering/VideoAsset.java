package org.Rendering;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.EnumMap;

/**
 * Unity-like automatic video path preloader for VLCJ-based video playback.
 * This version extracts resource videos to temporary files,
 * so VLCJ can play them both from IDE and from packaged JARs.
 */
public class VideoAsset {

    /**
     * Enumeration of all video assets used in the application.
     * Each constant stores the internal resource path to the video file.
     */
    public enum VideoIndex {
        MainMenuBackground("/UI/MainMenuBackGround.mp4");

        public final String mediaPath;

        VideoIndex(String path) {
            this.mediaPath = path;
        }

        /**
         * Retrieves the resolved file system path for this video asset.
         * This path points to the temporary file created during initialization.
         *
         * @return The absolute path to the extracted video file.
         */
        public String getVideoPath() {
            return VideoAsset.getPath(this);
        }
    }

    private static final EnumMap<VideoIndex, String> videoPathMap = new EnumMap<>(VideoIndex.class);

    /**
     * Internal method to retrieve the resolved path for a given video index.
     *
     * @param index The {@link VideoIndex} constant to look up.
     * @return The absolute path to the temporary video file.
     */
    private static String getPath(VideoIndex index) {
        return videoPathMap.get(index);
    }

    /**
     * Initializes all video assets by extracting them from the classpath
     * (JAR or filesystem) into temporary files. This method must be called once
     * before any video is played.
     */
    public static void initializeVideoMedia() {
        for (var index : VideoIndex.values()) {
            String resolvedPath = null;
            try {
                URL resource = VideoAsset.class.getResource(index.mediaPath);
                if (resource == null) {
                    throw new IllegalArgumentException("Video not found: " + index.mediaPath);
                }

                var input = VideoAsset.class.getResourceAsStream(index.mediaPath);
                if (input == null) throw new IllegalStateException("Stream is null for " + index.mediaPath);

                File temp = File.createTempFile("video_", ".mp4");
                temp.deleteOnExit();

                try (var output = new FileOutputStream(temp)) {
                    input.transferTo(output);
                }

                resolvedPath = temp.getAbsolutePath();
                System.out.println("[VideoAsset] Loaded " + index.mediaPath + " -> " + resolvedPath);

            } catch (Exception e) {
                System.err.println("[VideoAsset] Failed to load video [" + index + "]: " + e.getMessage());
            }

            videoPathMap.put(index, resolvedPath);
        }
    }
}
