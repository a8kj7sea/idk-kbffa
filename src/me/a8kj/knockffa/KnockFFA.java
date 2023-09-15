package me.a8kj.knockffa;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import lombok.Setter;
import me.a8kj.knockffa.configuration.Configuration;
import me.a8kj.knockffa.database.MySQLManager;
import me.a8kj.knockffa.database.management.PlayerData;

public class KnockFFA extends JavaPlugin {

    @Getter
    @Setter
    private static KnockFFA instance;

    @Getter
    @Setter
    private MySQLManager mySQLManager;

    @Getter
    @Setter
    private static Configuration configuration;

    @Getter
    @Setter
    private static FileConfiguration directConfig;

    @Getter
    @Setter
    private static PlayerData playerData;

    @Override
    public void onEnable() {
        init();

        if (getMySQLManager() != null) {
            mySQLManager.connect();
            setPlayerData(new PlayerData(mySQLManager));
            getPlayerData().init();
        }

    }

    @Override
    public void onDisable() {
        if (getMySQLManager() != null)
            mySQLManager.disconnect();
    }

    void init() {
        setInstance(this);
        setConfiguration(new Configuration("config.yml", this, true));
        setDirectConfig(configuration.getConfig());
        setMySQLManager(gmySqlClientData());
    }

    final MySQLManager gmySqlClientData() {
        MySQLManager manager = new MySQLManager();
        manager.setDatabase(directConfig.getString("MySQL-Client.database"));
        manager.setHost(directConfig.getString("MySQL-Client.host"));
        manager.setUsername(directConfig.getString("MySQL-Client.username"));
        manager.setPassword(directConfig.getString("MySQL-Client.password"));
        return manager;
    }
}
