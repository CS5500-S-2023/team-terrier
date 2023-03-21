package edu.northeastern.cs5500.starterbot.command.terrier;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

@Singleton
public class WelcomeCommand implements TerrierCommand {
    @Inject
    public WelcomeCommand() {}

    @Nonnull
    private static final SubcommandData DESCRIPTOR =
            new SubcommandData("welcome", "Terrier barks!");

    @Override
    @Nonnull
    public SubcommandData getDescriptor() {
        return DESCRIPTOR;
    }
}
