package edu.northeastern.cs5500.starterbot.command.terrier;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

@Singleton
@Slf4j
public class TerrierCommands {
    @Inject @Nonnull Set<SubcommandGroupData> groups;
    @Nonnull Map<String, TerrierCommand> commandMap = new HashMap<>();
    @Nonnull Map<String, SlashHandler> slashMap = new HashMap<>();

    /**
     * Map out commands in format <Subcommand Name, Trait>.
     *
     * @param commands
     * @param slashHandlers
     */
    @Inject
    public TerrierCommands(Set<TerrierCommand> commands, Set<SlashHandler> slashHandlers) {
        // Map TerrierCommand traits.
        for (TerrierCommand command : commands) {
            String key = command.getDescriptor().getName();
            if (commandMap.containsKey(key)) {
                throw new RuntimeException("Subcommand name collision found");
            }
            commandMap.put(key, command);
        }

        // Map SlashHandlers traits.
        for (SlashHandler handler : slashHandlers) {
            String key = handler.getDescriptor().getName();
            slashMap.put(key, handler);
        }
    }

    /**
     * Returns the commands that terrier supports.
     *
     * @return
     */
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

    /**
     * Handles incoming slash events and returns a testable reply.
     *
     * @param event
     * @return
     */
    @Nonnull
    public MessageCreateData onSlashInteraction(@Nonnull SlashCommandInteractionEvent event) {
        String key = event.getSubcommandName();
        if (!slashMap.containsKey(key)) {
            event.reply("Terrier doesn't recognize this subcommand!").queue();
            log.info("Unrecognized subcommand: " + key);
        }
        log.info("/" + key);
        return slashMap.get(key).onSlashInteraction(event.getIdLong(), event.getOptions());
    }
}
