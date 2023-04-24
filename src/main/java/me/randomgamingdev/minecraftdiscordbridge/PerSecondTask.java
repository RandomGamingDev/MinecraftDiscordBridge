package me.randomgamingdev.minecraftdiscordbridge;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.Scanner;

public class PerSecondTask extends BukkitRunnable {
    private final MinecraftDiscordBridge plugin;
    private final JDA bot;
    private final DiscordCommands discordCommands;
    int cursor = 0;

    PerSecondTask(MinecraftDiscordBridge plugin, JDA bot, DiscordCommands discordCommands) {
        this.plugin = plugin;
        this.bot = bot;
        this.discordCommands = discordCommands;
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("logs/latest.log"));
        } catch (Exception exception) {
            System.out.println("Something went wrong while creating the MinecraftDiscordBridge scanner!");
        }
        while (scanner.hasNextLine()) {
            scanner.nextLine();
            cursor++;
        }
    }

    @Override
    public void run() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("logs/latest.log"));
        } catch (Exception exception) {
            System.out.println("Something went wrong while creating the MinecraftDiscordBridge scanner!");
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < cursor; i++)
            scanner.nextLine();
        while (scanner.hasNextLine()) {
            builder.append(scanner.nextLine() + '\n');
            cursor++;
        }

        String msg = builder.toString();
        if (msg.isEmpty())
            return;

        for (Long channelId : discordCommands.outChannels) {
            TextChannel channel = bot.getTextChannelById(channelId);
            if (channel != null)
                channel.sendMessage(msg).queue();
        }
    }
}
