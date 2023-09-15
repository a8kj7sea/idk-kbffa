package me.a8kj.knockffa.leaderboard;

import lombok.Getter;
import lombok.Setter;

public enum OrderBy {
    KILLS("player_kills"), DEATHS("player_deaths"), COINS("player_coins");

    @Getter
    @Setter
    private String columnName;

    OrderBy(String columnName) {
        setColumnName(columnName);
    }

   
}
