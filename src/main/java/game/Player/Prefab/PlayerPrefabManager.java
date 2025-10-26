package game.Player.Prefab;

import game.Player.Player;
import game.Player.PlayerHealthLossVignette;
import game.Player.PlayerHealthUI;

public final class PlayerPrefabManager {

    public static void instantiatePlayer() {

        // Main object
        var playerObject = new PlayerPrefab().instantiatePrefab();
        var player = playerObject.getComponent(Player.class);

        // Health bar
        var healthBarObject = new PlayerHealthBarPrefab().instantiatePrefab();
        healthBarObject.setParent(playerObject);

        // Health loss vignette
        var healthLossVignetteObject = new PlayerHealthLossVignettePrefab().instantiatePrefab();
        var healthLossVignette = healthLossVignetteObject.getComponent(PlayerHealthLossVignette.class);
        healthLossVignette.linkPlayer(player);
        healthLossVignetteObject.setParent(playerObject);

    }

}