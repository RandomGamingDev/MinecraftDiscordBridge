package me.randomgamingdev.minecraftdiscordbridge;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Scanner;

public class MinecraftCommandListOut implements CommandExecutor {
    private final DiscordCommands discordCommands;
    private final MinecraftDiscordBridge plugin;

    MinecraftCommandListOut(DiscordCommands discordCommands, MinecraftDiscordBridge plugin) {
        this.discordCommands = discordCommands;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean isPlayer = sender instanceof Player;
        if (!(sender instanceof ConsoleCommandSender) && !isPlayer)
            return false;

        StringBuilder outChannelsMsg = new StringBuilder();
        try {
            Scanner outChannelsScanner = new Scanner(discordCommands.outChannelsFile);
            while (outChannelsScanner.hasNextLine())
                outChannelsMsg.append(outChannelsScanner.nextLine() + '\n');
            String msg = outChannelsMsg.toString();
            if (isPlayer)
                ((Player)sender).sendMessage(msg);
            System.out.println(msg);
        }
        catch (Exception exception) {
            String errorMsg = "Something went wrong while trying to send the trusted users list";
            if (isPlayer)
                ((Player)sender).sendMessage(errorMsg);
            System.out.println(errorMsg);
        }

        return true;
    }
}
