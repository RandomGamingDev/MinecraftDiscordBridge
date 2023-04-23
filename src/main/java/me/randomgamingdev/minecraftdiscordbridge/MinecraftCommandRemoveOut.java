package me.randomgamingdev.minecraftdiscordbridge;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.FileWriter;

public class MinecraftCommandRemoveOut implements CommandExecutor {
    private final DiscordCommands discordCommands;

    MinecraftCommandRemoveOut(DiscordCommands discordCommands) {
        this.discordCommands = discordCommands;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean isPlayer = sender instanceof Player;
        if (!(sender instanceof ConsoleCommandSender) && !isPlayer)
            return false;

        if (args.length != 1)
            return false;

        String channelIdStr = args[0];
        discordCommands.outChannels.remove(Long.parseLong(channelIdStr));
        StringBuilder outChannelsFileContents = new StringBuilder();
        for (Long user : discordCommands.outChannels)
            outChannelsFileContents.append(user);
        try {
            FileWriter outChannelsFileWriter = new FileWriter(discordCommands.outChannelsFile);
            outChannelsFileWriter.write(outChannelsFileContents.toString() + '\n');
            outChannelsFileWriter.close();
            String msg = "Successfully removed a channel from " + discordCommands.outChannelFilemane;
            if (isPlayer)
                ((Player)sender).sendMessage(msg);
            System.out.println(msg);
        }
        catch (Exception exception) {
            String errorMsg = "Something went wrong while trying to remove a channel from " + discordCommands.outChannelFilemane;
            if (isPlayer)
                ((Player)sender).sendMessage(errorMsg);
            System.out.println(errorMsg);
        }

        return true;
    }
}
