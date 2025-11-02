package org.Audio;

import javafx.scene.media.Media;


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

        public Media getMedia() {
            return media;
        }
    }

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
