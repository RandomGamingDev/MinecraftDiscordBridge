package me.randomgamingdev.minecraftdiscordbridge;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Scanner;

public final class MinecraftDiscordBridge extends JavaPlugin {
    //public ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    public JDA bot;
    public DiscordCommands discordCommands;
    @Override
    public void onEnable() {
        System.out.println("MinecraftDiscordBridge is starting up!");
        Scanner tokenFile = null;
        try {
            tokenFile = new Scanner(new File("MinecraftDiscordBridgeBotToken.txt"));
        }
        catch (Exception exception) {
            System.out.println("Something went wrong while trying to get the bot token");
        }
        String botToken = tokenFile.nextLine();
        JDABuilder botBuilder = JDABuilder.createDefault(botToken, GatewayIntent.getIntents(GatewayIntent.DEFAULT));
        botBuilder.enableIntents(GatewayIntent.MESSAGE_CONTENT);
        bot = botBuilder.build();
        discordCommands = new DiscordCommands(bot, this);
        bot.addEventListener(discordCommands);
        new PerTickTask(this, bot, discordCommands).runTaskTimer(this, 0, 20);
        this.getCommand("trustuser").setExecutor(new MinecraftCommandTrustUser(discordCommands));
        this.getCommand("untrustuser").setExecutor(new MinecraftCommandUntrustUser(discordCommands));
        this.getCommand("listtrusted").setExecutor(new MinecraftCommandListTrusted(discordCommands, this));
        this.getCommand("addout").setExecutor(new MinecraftCommandAddOut(discordCommands));
        this.getCommand("removeout").setExecutor(new MinecraftCommandRemoveOut(discordCommands));
        this.getCommand("listout").setExecutor(new MinecraftCommandListOut(discordCommands, this));

    }

    @Override
    public void onDisable() {
        System.out.println("MinecraftDiscordBridge is shutting down!");
        bot.shutdownNow();
    }
}
