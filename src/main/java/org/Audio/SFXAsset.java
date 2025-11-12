package org.Audio;

import javafx.scene.media.AudioClip;

/**
 * Utility class for managing and preloading Sound Effects (SFX) assets using JavaFX {@link AudioClip}.
 * <p>
 * This class defines an enumeration {@link SFXIndex} to catalog all SFX files
 * by their classpath resource path and handles the loading of these resources
 * into JavaFX {@link AudioClip} objects upon initialization. {@code AudioClip} is
 * suitable for short, low-latency sounds like SFX.
 */
public class SFXAsset {

    /**
     * Enumeration of all Sound Effects (SFX) assets used in the application.
     * Each constant stores the classpath resource path to the SFX file
     * and holds the loaded JavaFX {@link AudioClip} object.
     */
    public enum SFXIndex {
        BossCharging("/Audio/SFX/BossCharging.wav"),
        BossNormalAttack("/Audio/SFX/BossNormalAttack.wav"),
        BossUltimate(("/Audio/SFX/BossUltimate.wav")),
        ButtonClick("/Audio/SFX/ButtonClick.wav"),
        ButtonHover("/Audio/SFX/ButtonHover.wav"),
        OnLaserBeamShoot("/Audio/SFX/LaserBeamOnShoot.wav"),
        OnBrickHit("/Audio/SFX/OnBrickHit.wav"),
        OnPaddleHit_Ball("/Audio/SFX/OnPaddleHit(Ball).wav"),
        OnPerkReceived("/Audio/SFX/OnPerkReceived.wav"),
        OnPlayerHit_Enemy("/Audio/SFX/OnPlayerHit(Enemy).wav"),
        OnPowerReceived("/Audio/SFX/OnPowerReceived.wav"),
        IntroductionVoice(("/Audio/SFX/IntroductionVoice.wav")),

        None("");

        private AudioClip audioClip;

        /**
         * Private constructor for {@code SFXIndex}.
         * Attempts to load the SFX resource into a JavaFX {@link AudioClip} object.
         *
         * @param path The classpath resource path of the SFX file (e.g., "/Audio/SFX/sound.wav").
         */
        SFXIndex(String path) {
            audioClip = null;
            if (path == null || path.isEmpty()) return;
            try {
                audioClip = loadSFX(path);
                System.out.println("[SFXAsset] Loading SFX: " + path);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                return;
            }

            System.out.println("[SFXAsset] Loaded SFX: " + path);
        }

        /**
         * Retrieves the loaded JavaFX {@link AudioClip} object for this SFX track.
         *
         * @return The loaded {@link AudioClip} object, or {@code null} if loading failed or if the index is {@link #None}.
         */
        public AudioClip getAudioClip() {
            return audioClip;
        }
    }

    /**
     * Loads a sound effect resource from the classpath and creates a JavaFX {@link AudioClip} object.
     *
     * @param path The classpath resource path of the SFX file (e.g., "/Audio/SFX/sound.wav").
     * @return A new JavaFX {@link AudioClip} object representing the SFX file.
     * @throws RuntimeException if the resource is not found or if the {@link AudioClip} object cannot be created.
     */
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
