package nl.martenm.currency.commands;

import nl.martenm.currency.CurrencyPlugin;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * @author MartenM
 * @since 30-3-2018.
 */
public class CurrencyCommand implements CommandExecutor {

    private CurrencyPlugin plugin;

    public CurrencyCommand(CurrencyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (args.length < 4) {
            return false;
        }

        OfflinePlayer target = plugin.getServer().getOfflinePlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Unable to get the UUID of that player.");
            return true;
        }

        int amount = 0;
        try {
            amount = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            return false;
        }

        String currency = args[2];
        if (!plugin.currencyExist(currency)) {
            sender.sendMessage(ChatColor.RED + "That currency does not exist.");
            return true;
        }


        switch (args[0]) {
            case "set":
                plugin.getCurrencyApi().setBalance(currency, target.getUniqueId(), amount).queue(result -> {
                    if (result) {
                        sender.sendMessage(ChatColor.GREEN + "Successfully changed the currency for the target player!");
                    }
                });
                break;
            case "remove":
                plugin.getCurrencyApi().removeBalance(currency, target.getUniqueId(), amount).queue(result -> {
                    if (result) {
                        sender.sendMessage(ChatColor.GREEN + "Successfully changed the currency for the target player!");
                    }
                });
                break;
            case "add":
                plugin.getCurrencyApi().addBalance(currency, target.getUniqueId(), amount).queue(result -> {
                    if (result) {
                        sender.sendMessage(ChatColor.GREEN + "Successfully changed the currency for the target player!");
                    }
                });
                break;
            default:
                return false;
        }
        return true;
    }
}
