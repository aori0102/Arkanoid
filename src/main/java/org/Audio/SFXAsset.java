package org.Audio;

import javafx.scene.media.AudioClip;

public class SFXAsset {

    public enum SFXIndex {
        None("");

        private AudioClip audioClip;

        SFXIndex(String path) {
            audioClip = null;
            if (path == null || path.isEmpty()) return;
            try {
                audioClip = loadSFX(path);
                System.out.println("[SFXAsset] Loading SFX: " + path);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            System.out.println("[SFXAsset] Loaded SFX: " + path);
        }

        public AudioClip getAudioClip() {
            return audioClip;
        }
    }

    public static AudioClip loadSFX(String path) {
        try {
            var resource = SFXAsset.class.getResource(path);
            if (resource == null) {
                throw new RuntimeException("[SFXAsset] Failed to load SFXAsset (Path is null): " + path);
            }

            return new AudioClip(resource.toExternalForm());
        } catch (RuntimeException e) {
            throw new RuntimeException("[SFXAsset] Failed to load SFXAsset: " + path, e);
        }
    }


}
