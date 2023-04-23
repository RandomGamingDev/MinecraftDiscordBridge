package me.randomgamingdev.minecraftdiscordbridge;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.FileWriter;

public class MinecraftCommandTrustUser implements CommandExecutor {
    private final DiscordCommands discordCommands;

    MinecraftCommandTrustUser(DiscordCommands discordCommands) {
        this.discordCommands = discordCommands;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean isPlayer = sender instanceof Player;
        if (!(sender instanceof ConsoleCommandSender) && !isPlayer)
            return false;

        if (args.length != 1)
            return false;

        String userIdStr = args[0];
        discordCommands.trustedUsers.add(Long.parseLong(userIdStr));
        try {
            FileWriter trustedUsersFileWriter = new FileWriter(discordCommands.trustedUsersFile, true);
            trustedUsersFileWriter.write(userIdStr + '\n');
            trustedUsersFileWriter.close();
            String msg = "Successfully added a user to " + discordCommands.trustedUsersFilename;
            if (isPlayer)
                ((Player)sender).sendMessage(msg);
            System.out.println(msg);
        }
        catch (Exception exception) {
            String errorMsg = "Something went wrong while trying to add a user to " + discordCommands.trustedUsersFilename;
            if (isPlayer)
                ((Player)sender).sendMessage(errorMsg);
            System.out.println(errorMsg);
        }

        return true;
    }
}
