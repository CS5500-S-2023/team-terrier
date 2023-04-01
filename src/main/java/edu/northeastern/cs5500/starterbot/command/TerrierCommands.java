package edu.northeastern.cs5500.starterbot.command;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

@Singleton
@Slf4j
public class TerrierCommands {
    @Inject @Nonnull Set<SubcommandGroupData> groups;
    @Nonnull Map<String, TerrierCommand> commandMap = new HashMap<>();
    @Nonnull Map<String, SlashHandler> slashMap = new HashMap<>();
    @Nonnull Map<String, ButtonHandler> buttonMap = new HashMap<>();

    /**
     * Map out commands in format <Subcommand Name, Trait>.
     *
     * @param commands
     * @param slashHandlers
     */
    @Inject
    public TerrierCommands(
            @Nonnull Set<TerrierCommand> commands,
            @Nonnull Set<SlashHandler> slashHandlers,
            @Nonnull Set<ButtonHandler> buttonHandlers) {
        // Map TerrierCommand traits.
        for (TerrierCommand command : commands) {
            String key = command.getDescriptor().getName();
            if (commandMap.containsKey(key)) {
                throw new IllegalArgumentException("Subcommand name collision found");
            }
            commandMap.put(key, command);
        }

        // Map SlashHandlers traits.
        for (SlashHandler handler : slashHandlers) {
            String key = handler.getDescriptor().getName();
            if (slashMap.containsKey(key)) {
                throw new IllegalArgumentException("Subcommand name collision found");
            }
            slashMap.put(key, handler);
        }

        // Map ButtonHandlers traits.
        for (ButtonHandler handler : buttonHandlers) {
            for (String key : handler.getButtonNames()) {
                if (buttonMap.containsKey(key)) {
                    throw new IllegalArgumentException("Button name collision found");
                }
                buttonMap.put(key, handler);
            }
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
            log.info("Unrecognized subcommand: " + key);
            return new MessageCreateBuilder()
                    .setContent("Terrier doesn't recognize this subcommand!")
                    .build();
        }
        log.info("/" + event.getFullCommandName());
        return slashMap.get(key)
                .onSlashInteraction(event.getUser().getIdLong(), event.getOptions());
    }

    @Nonnull
    public MessageCreateData onButtonInteraction(@Nonnull ButtonInteractionEvent event) {
        String key = event.getButton().getId();
        if (!buttonMap.containsKey(key)) {
            log.info("Unrecognized button id: " + key);
            return new MessageCreateBuilder()
                    .setContent("Terrier doesn't recognize this button!")
                    .build();
        }
        log.info("button: " + key);
        return buttonMap
                .get(key)
                .onButtonInteraction(event.getUser().getIdLong(), event.getButton());
    }
}
