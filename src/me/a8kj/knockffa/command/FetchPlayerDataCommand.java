package me.a8kj.knockffa.command;

import static me.a8kj.knockffa.util.StringUtils.colorize;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
//import org.bukkit.command.TabCompleter; // (Soon)
import org.bukkit.entity.Player;

import me.a8kj.knockffa.KnockFFA;
import me.a8kj.knockffa.database.management.PlayerData.Data;

public class FetchPlayerDataCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(colorize("&cThis command just for player usage !"));
            return false;
        }
        Player player = (Player) sender;

        switch (args.length) {
            case 0:
                sendFormattedMessage(player, player);
                return true;
            case 1:
                Player targetPlayer = Bukkit.getOfflinePlayer(args[0]).getPlayer();
                sendFormattedMessage(targetPlayer, player);
                return true;

        }

        return true;

    }

    void sendFormattedMessage(Player fetchedPlayer, CommandSender sender) {
        Data playerData = KnockFFA.getPlayerData().getDataToPlayer(fetchedPlayer);
        sender.sendMessage(colorize("&8&l--------------------------------------------"));
        sender.sendMessage(colorize(" &8» &b&l" + fetchedPlayer.getName() + " &7, stats :"));
        sender.sendMessage(colorize(" &8» &c&l" + playerData.getKills() + " kills. "));
        sender.sendMessage(colorize(" &8» &c&l" + playerData.getDeaths() + " deaths. "));
        sender.sendMessage(colorize(" &8» &c&l" + playerData.getCoins() + " coins. "));
        sender.sendMessage(colorize("&8&l--------------------------------------------"));
    }

}
