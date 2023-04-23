# MinecraftDiscordBridge
A basic bridge between Minecraft and Discord that I created.

Please note:
This will not capture all console output
This captures console output using System.setOut, System.setErr and the events for player messages and player and console commands.
To see all the commands for registering console channels use `!help`
To execute console commands in a console channel use the "$" symbol a prefix to the command in the channel.
Remember to put your bot token in a file named `MinecraftDiscordBridgeBotToken.txt` with the bot token.
To see the commands check out plugin.yml.
Please note that becuase of how console input is captured some console output will appear as though it's from the bridge plugin and will always have Level.INFO.
