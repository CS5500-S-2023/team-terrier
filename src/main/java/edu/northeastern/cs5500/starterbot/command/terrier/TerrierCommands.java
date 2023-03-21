package edu.northeastern.cs5500.starterbot.command.terrier;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

@Singleton
public class TerrierCommands {
    @Nonnull Map<String, TerrierCommand> commandMap = new HashMap<>();
    @Nonnull Set<SubcommandGroupData> groups;

    @Inject
    public TerrierCommands(Set<TerrierCommand> commands, @Nonnull Set<SubcommandGroupData> groups) {
        for (TerrierCommand command : commands) {
            String key = command.getDescriptor().getName();
            if (commandMap.containsKey(key)) {
                throw new RuntimeException("Subcommand name collision found");
            }
            commandMap.put(key, command);
        }
        this.groups = groups;
    }

    public CommandData getDescriptors() {
        SlashCommandData descriptors = Commands.slash("terrier", "Terrier's here to help!");
        // Group commands and add dangling commands directly.
        for (TerrierCommand command : commandMap.values()) {
            SubcommandGroupData group = command.getGroup();
            SubcommandData descriptor = command.getDescriptor();
            if (group == null) {
                descriptors.addSubcommands(descriptor);
            } else {
                group.addSubcommands(descriptor);
            }
        }
        // Add groups.
        descriptors.addSubcommandGroups(groups);
        return descriptors;
    }
}
