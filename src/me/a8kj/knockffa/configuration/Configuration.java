package me.a8kj.knockffa.configuration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import lombok.Setter;

public class Configuration {
    @Getter
    @Setter
    private File file;
    @Getter
    @Setter
    private FileConfiguration config;

    private void init(boolean defaultSave, String configName, JavaPlugin plugin) throws IOException {
        file.getParentFile().mkdirs();
        if (!file.exists()) {
            if (!defaultSave) {
                if (!file.createNewFile()) {
                    plugin.getLogger().log(Level.WARNING,
                            "The config file does not created , please make sure you create one");
                } else {
                    plugin.getLogger().info("The config file was created successfully !");
                }
            } else {
                plugin.saveResource(configName, true);
            }
        }
    }

    public Configuration(String fileName, JavaPlugin plugin, boolean saveDefault) {
        setFile(new File(plugin.getDataFolder(), fileName));
        try {
            init(saveDefault, fileName, plugin);
        } catch (IOException e) {
            e.printStackTrace();
        }
        load();
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException var1) {
            var1.printStackTrace();
        }
    }

    public void load() {
        setConfig((FileConfiguration) YamlConfiguration.loadConfiguration(file));
    }

}
