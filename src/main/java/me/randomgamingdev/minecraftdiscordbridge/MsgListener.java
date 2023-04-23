package me.randomgamingdev.minecraftdiscordbridge;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class MsgListener implements Listener {
    private MinecraftDiscordBridge plugin;

    MsgListener(MinecraftDiscordBridge plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        try {
            plugin.outputStream.write((String.format(event.getFormat(),
                    event.getPlayer().getDisplayName(),
                    event.getMessage()) + '\n')
                            .getBytes());
        }
        catch (Exception exception) {
            System.out.println("There was a problem writing a message to the outputStream");
        }
    }

    @EventHandler
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        try {
            plugin.outputStream.write((String.format("%s issued server command: %s",
                    event.getPlayer().getDisplayName(),
                    event.getMessage()) + '\n')
                        .getBytes());
        }
        catch (Exception exception) {
            System.out.println("There was a problem writing a player command execution instance to the outputStream");
        }
    }

    @EventHandler
    public void onServerCommandEvent(ServerCommandEvent event) {
        if (!(event.getSender() instanceof ConsoleCommandSender))
            return;

        try {
            plugin.outputStream.write((String.format("%s issued server command: %s",
                    event.getSender().getName(),
                    event.getCommand()) + '\n')
                        .getBytes()
            );
        }
        catch (Exception exception) {
            System.out.println("There was a problem writing a non-player command execution instance to the outputStream");
        }
    }
}
