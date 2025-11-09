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

    public enum VideoIndex {
        MainMenuBackground("/UI/MainMenuBackGround.mp4");

        public final String mediaPath;

        VideoIndex(String path) {
            this.mediaPath = path;
        }

        public String getVideoPath() {
            return VideoAsset.getPath(this);
        }
    }

    private static final EnumMap<VideoIndex, String> videoPathMap = new EnumMap<>(VideoIndex.class);

    private static String getPath(VideoIndex index) {
        return videoPathMap.get(index);
    }

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
