package game.Config;

import com.google.gson.Gson;
import game.GameManager.GameManager;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ConfigManager extends MonoBehaviour {

    public final Path CONFIG_FILE = GameManager.PLAYER_DATA_DIRECTORY.resolve("Config.json");

    private static ConfigManager instance = null;

    private final Config config = new Config();

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public ConfigManager(GameObject owner) {
        super(owner);
        if (instance != null) {
            throw new ReinitializedSingletonException("ConfigManager is a singleton!");
        }
        instance = this;
        loadConfig();
    }

    @Override
    public void onDestroy() {
        saveConfig();
        instance = null;
    }

    private void loadConfig() {

        try {

            if (!Files.exists(CONFIG_FILE)) {
                System.out.println("[ConfigManager] Config file not found. Creating empty config.");
                return;
            }

            Gson gsonLoader = new Gson();
            String json = Files.readString(CONFIG_FILE);

            config.overrideConfig(gsonLoader.fromJson(json, Config.class));

            System.out.println("[ConfigManager] Config file loaded.");
            System.out.println(config);

        } catch (Exception e) {
            System.err.println("[ConfigManager] Error while loading configs: " + e.getMessage());
        }

    }

    private void saveConfig() {

        try {

            Gson gsonSaver = new Gson();
            String json = gsonSaver.toJson(config);
            Files.writeString(CONFIG_FILE, json);
            System.out.println("[ConfigManager] Config file saved to \"" + CONFIG_FILE + "\".");

        } catch (Exception e) {
            System.err.println("[ConfigManager] Error while saving configs: " + e.getMessage());
        }

    }

}