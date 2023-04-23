package me.randomgamingdev.minecraftdiscordbridge;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Server;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Scanner;

public class DiscordCommands extends ListenerAdapter {
    private final JDA bot;
    private final MinecraftDiscordBridge plugin;
    public final String trustedUsersFilename = "trustedUsers.txt";
    public final String outChannelFilemane = "outputChannels.txt";
    public final File trustedUsersFile = new File(trustedUsersFilename);
    public final File outChannelsFile = new File(outChannelFilemane);
    public final HashSet<Long> trustedUsers = new HashSet<Long>();
    public final HashSet<Long> outChannels = new HashSet<Long>();

    private void Load() {
        try {
            trustedUsersFile.createNewFile();
            Scanner trustedUsersScanner = new Scanner(trustedUsersFile);
            while (trustedUsersScanner.hasNextLine())
                trustedUsers.add(Long.parseLong(trustedUsersScanner.nextLine()));
        }
        catch (Exception exception) {
            System.out.println("Something went wrong when reading from " + trustedUsersFilename);
        }

        try {
            outChannelsFile.createNewFile();
            Scanner outChannelsScanner = new Scanner(outChannelsFile);
            while (outChannelsScanner.hasNextLine())
                outChannels.add(Long.parseLong(outChannelsScanner.nextLine()));
        }
        catch (Exception exception) {
            System.out.println("Something went wrong when reading from " + outChannelFilemane);
        }
    }

    DiscordCommands(JDA bot, MinecraftDiscordBridge plugin) {
        this.bot = bot;
        this.plugin = plugin;
        Load();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User author = event.getAuthor();
        if (author.getIdLong() == bot.getSelfUser().getIdLong())
            return;

        Message msg = event.getMessage();
        String dispMsg = msg.getContentDisplay();
        if (dispMsg.isEmpty()) // https://github.com/DV8FromTheWorld/JDA/wiki/19%29-Troubleshooting#nothing-happens-when-using-x
            return;
        String rawMsg = msg.getContentRaw();
        char msgHead = rawMsg.charAt(0);
        String rawContent = rawMsg.substring(1);
        String rawCmd = rawContent.split(" ")[0];
        MessageChannelUnion channel = event.getChannel();

        if (msgHead == '!') {
            final String trustUser = "trustUser";
            final String untrustUser = "untrustUser";
            final String listTrusted = "listTrusted";
            final String addOut = "addOut";
            final String removeOut = "removeOut";
            final String listOut = "listOut";

            switch (rawCmd) {
                case "help":
                    msg.reply("The commands are: \n" +
                            "!`trustUser`: Trust a user with console perms\n" +
                            "!`untrustUser`: Remove a user's console perms\n" +
                            "!`listTrusted`: List all trusted users\n" +
                            "!`addOut`: Add an output channel\n" +
                            "!`removeOut`: Remove an output channel\n" +
                            "!`listOut`: List all output channels\n").queue();
                    break;
                case trustUser:
                {
                    String userIdStr = rawContent.substring(1 + trustUser.length());
                    trustedUsers.add(Long.parseLong(userIdStr));
                    try {
                        FileWriter trustedUsersFileWriter = new FileWriter(trustedUsersFile, true);
                        trustedUsersFileWriter.write(userIdStr + '\n');
                        trustedUsersFileWriter.close();
                        System.out.println("Successfully added a user to " + trustedUsersFilename);
                    }
                    catch (Exception exception) {
                        System.out.println("Something went wrong while trying to add a user to " + trustedUsersFilename);
                    }
                }
                    break;
                case untrustUser:
                {
                    String userIdStr = rawContent.substring(1 + untrustUser.length());
                    trustedUsers.remove(Long.parseLong(userIdStr));
                    StringBuilder trustedUsersFileContents = new StringBuilder();
                    for (Long user : trustedUsers)
                        trustedUsersFileContents.append(user);
                    try {
                        FileWriter trustedUsersFileWriter = new FileWriter(trustedUsersFile);
                        trustedUsersFileWriter.write(trustedUsersFileContents.toString() + '\n');
                        trustedUsersFileWriter.close();
                        System.out.println("Successfully remove a user from" + trustedUsersFilename);
                    }
                    catch (Exception exception) {
                        System.out.println("Something went wrong while trying to remove a user from " + trustedUsersFilename);
                    }
                    break;
                }
                case listTrusted:
                {
                    StringBuilder trustedUsersMsg = new StringBuilder();
                    try {
                        Scanner trustedUsersScanner = new Scanner(trustedUsersFile);
                        while (trustedUsersScanner.hasNextLine())
                            trustedUsersMsg.append(trustedUsersScanner.nextLine() + '\n');
                    }
                    catch (Exception exception) {
                        System.out.println("Something went wrong while trying to send the trusted users list");
                    }
                    msg.reply(trustedUsersMsg.toString()).queue();
                }
                    break;
                case addOut:
                {
                    String channelIdStr = rawContent.substring(1 + addOut.length());
                    outChannels.add(Long.parseLong(channelIdStr));
                    try {
                        FileWriter outChannelsFileWriter = new FileWriter(outChannelsFile, true);
                        outChannelsFileWriter.write(channelIdStr + '\n');
                        outChannelsFileWriter.close();
                        System.out.println("Successfully add a channel to " + outChannelFilemane);
                    }
                    catch (Exception exception) {
                        System.out.println("Something went wrong while trying to add a channel to " + outChannelFilemane);
                    }
                    break;
                }
                case removeOut:
                {
                    String channelIdStr = rawContent.substring(1 + removeOut.length());
                    outChannels.remove(Long.parseLong(channelIdStr));
                    StringBuilder outChannelsFileContents = new StringBuilder();
                    for (Long user : outChannels)
                        outChannelsFileContents.append(user);
                    try {
                        FileWriter outChannelsFileWriter = new FileWriter(outChannelsFile);
                        outChannelsFileWriter.write(outChannelsFileContents.toString() + '\n');
                        outChannelsFileWriter.close();
                        System.out.println("Successfully removed a channel from " + outChannelFilemane);
                    }
                    catch (Exception exception) {
                        System.out.println("Something went wrong while trying to remove a channel from " + outChannelFilemane);
                    }
                }
                    break;
                case listOut:
                {
                    StringBuilder outChannelsMsg = new StringBuilder();
                    try {
                        Scanner outChannelsScanner = new Scanner(outChannelsFile);
                        while (outChannelsScanner.hasNextLine())
                            outChannelsMsg.append(outChannelsScanner.nextLine() + '\n');
                    }
                    catch (Exception exception) {
                        System.out.println("Something went wrong while trying to send the trusted users list");
                    }
                    msg.reply(outChannelsMsg.toString()).queue();
                }
                    break;
            }
            return;
        }

        if (!outChannels.contains(channel.getIdLong()))
            return;

        if (!trustedUsers.contains(author.getIdLong()))
            return;

        Server server = plugin.getServer();

        switch (msgHead) {
            case '$':
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        server.dispatchCommand(server.getConsoleSender(), rawCmd);
                    }
                }.runTask(plugin);
                break;
            default:
                server.broadcastMessage(String.format("[Discord User %s] %s",
                        author.getName(),
                        dispMsg));
                break;
        }
    }
}
