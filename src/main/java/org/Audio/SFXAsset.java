package org.Audio;

import javafx.scene.media.AudioClip;

public class SFXAsset {

    public enum SFXIndex {
        BossCharging("/Audio/SFX/BossCharging.wav"),
        BossNormalAttack("/Audio/SFX/BossNormalAttack.wav"),
        BossUltimate(("/Audio/SFX/BossUltimate.wav")),
        ButtonClick("/Audio/SFX/ButtonClick.wav"),
        ButtonHover("/Audio/SFX/ButtonHover.wav"),
        LaserBeamOnShoot("/Audio/SFX/LaserBeamOnShoot.wav"),
        OnBrickHit("/Audio/SFX/OnBrickHit.wav"),
        OnPaddleHit_Ball("/Audio/SFX/OnPaddleHit(Ball).wav"),
        OnPerkReceived("/Audio/SFX/OnPerkReceived.wav"),
        OnPlayerHit_Enemy("/Audio/SFX/OnPlayerHit(Enemy).wav"),
        OnPowerReceived("/Audio/SFX/OnPowerReceived.wav"),
        PowerUpOnReceive("/Audio/SFX/PowerUpOnReceive.wav"),

        None("");

        private AudioClip audioClip;

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
