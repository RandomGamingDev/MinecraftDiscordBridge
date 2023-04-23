package me.randomgamingdev.minecraftdiscordbridge;

import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DualStream extends PrintStream {
    public Logger logger;

    public DualStream(final Logger plugin, final PrintStream externalOutput) {
        super(externalOutput);
        this.logger = plugin;
    }

    @Override
    public void println() {
        super.println();
        logger.log(Level.INFO, "\n");
    }

    @Override
    public void println(final Object output) {
        super.println(output);
        logger.log(Level.INFO, output.toString());
    }

    @Override
    public void println(final String output) {
        super.println(output);
        logger.log(Level.INFO, output);
    }

    @Override
    public void print(final Object output) {
        super.print(output);
        logger.log(Level.INFO, output.toString());
    }

    @Override
    public void print(final String output) {
        super.print(output);
        logger.log(Level.INFO, output);
    }

    @Override
    public PrintStream printf(final String output, final Object... variables) {
        super.printf(output, variables);
        logger.log(Level.INFO, output, variables);
        return this;
    }
}