package me.a8kj.knockffa.database.management;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import javax.annotation.Nonnegative;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.a8kj.knockffa.database.MySQLManager;
import me.a8kj.knockffa.leaderboard.OrderBy;

public class PlayerData {

    public static class Data {
        @Getter
        @Setter
        private int kills, deaths, coins;

        public Data(int kills, int deaths, int coins) {
            setKills(kills);
            setDeaths(deaths);
            setCoins(coins);
        }

        public Data() {
            setKills(0);
            setDeaths(0);
            setCoins(1500);
        }
    }

    @Getter
    @Setter
    private MySQLManager mySQL;

    public PlayerData(@NonNull MySQLManager mySQL) {
        setMySQL(mySQL);
    }

    public void init() {
        mySQL.createTable("player_data",
                "player_name varchar(25) not null , " +
                        " player_kills int(15) unsigned , " +
                        " player_deaths int(15) unsigned , " +
                        " player_coins int(15)");
    }

    public boolean isFirstTimeToPlayer(@NonNull Player player) {
        ResultSet rs = mySQL.query("select * from player_data where player_name='" + player.getName() + "'");
        try {
            if (rs.next()) {
                addPlayerToDatabase(player);
                return rs.getString("player_name") == null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addPlayerToDatabase(@NonNull Player player) {
        String sqlStatement = "insert into player_data (player_name,player_kills,player_deaths,player_coins) values (`{player_name}`,0,0,1500);"
                .replace("{player_name}", player.getName());
        if (mySQL.update(sqlStatement) == -1) {
            Bukkit.getServer().getLogger().log(Level.WARNING, "Error while adding {} to database",
                    player.getName());
        } else {
            Bukkit.getServer().getLogger()
                    .log(Level.INFO, "new player with {} name was added to database", player.getName());
        }
    }

    public void addData(@NonNull Player player, @NonNull String dataName, int amount) {
        int oldDataAmount = getData(player, dataName);
        if (isFirstTimeToPlayer(player))
            addPlayerToDatabase(player);
        else
            mySQL.update("update player_data set " + dataName + "=" + oldDataAmount + amount + " where player_name=`"
                    + player.getName() + "`");
    }

    public void setData(@NonNull Player player, @NonNull String dataName, @Nonnegative int amount) {
        if (isFirstTimeToPlayer(player))
            addPlayerToDatabase(player);
        else
            mySQL.update("update player_data set " + dataName + "=" + amount + " where player_name=`"
                    + player.getName() + "`");
    }

    public Integer getData(@NonNull Player player, @NonNull String dataName) {
        if (!isFirstTimeToPlayer(player)) {
            ResultSet rs = mySQL.query("select * from player_data where player_name='" + player.getName() + "'");
            try {
                while (rs.next()) {
                    return rs.getInt(dataName);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return -1;
            }
        } else {
            addPlayerToDatabase(player);
            Bukkit.getServer().getLogger().log(Level.WARNING,
                    "Error while get {} date from database, the player not found",
                    player.getName());
            return -1;
        }
        return -1;
    }

    public Data getDataToPlayer(@NonNull Player player) {
        Data data = new Data(0, 0, 1500);
        if (!isFirstTimeToPlayer(player)) {
            data.setKills(getData(player, "player_kills"));
            data.setDeaths(getData(player, "player_deaths"));
            data.setCoins(getData(player, "player_coins"));
        } else {
            addPlayerToDatabase(player);
            Bukkit.getServer().getLogger().log(Level.WARNING,
                    "Error while get {} date from database, the player not found",
                    player.getName());
            return data;
        }
        return data;
    }

    public List<String> getTop(int limit, OrderBy orderBy) {
        List<String> list = Collections.emptyList();

        String sqlStatement = String.format("select player_name , %s from player_data order by %s desc limit %s",
                orderBy.getColumnName(), orderBy.getColumnName(), limit);

        ResultSet resultSet = mySQL.query(sqlStatement);

        int index = 0;
        try {
            while (resultSet.next()) {

                list.set(++index, "&8#" + index + " &b" + resultSet.getString("player_name") + "&8 - &c"
                        + resultSet.getInt(orderBy.getColumnName()));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;

    }

}
