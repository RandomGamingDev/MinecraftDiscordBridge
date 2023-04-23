package me.randomgamingdev.minecraftdiscordbridge;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bukkit.scheduler.BukkitRunnable;

public class PerSecondTask extends BukkitRunnable {
    private final MinecraftDiscordBridge plugin;
    private final JDA bot;
    private final DiscordCommands discordCommands;

    PerSecondTask(MinecraftDiscordBridge plugin, JDA bot, DiscordCommands discordCommands) {
        this.plugin = plugin;
        this.bot = bot;
        this.discordCommands = discordCommands;
    }

    @Override
    public void run() {
        String outStr = plugin.outputStream.toString();
        if (outStr.isEmpty())
            return;
        try {
            this.plugin.outputStream.flush();
        }
        catch (Exception exception) {
            System.out.println("Something went wrong while trying to flush the outputStream buffer");
        }
        for (Long channelId : discordCommands.outChannels) {
            TextChannel channel = bot.getTextChannelById(channelId);
            if (channel == null)
                continue;
            channel.sendMessage(new String(this.plugin.outputStream.toByteArray())).queue();
        }
        this.plugin.outputStream.reset();
    }
}
