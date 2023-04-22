package bot.discord.terrier.listener;

import bot.discord.terrier.command.common.ButtonHandler;
import bot.discord.terrier.command.common.SlashHandler;
import bot.discord.terrier.command.common.TerrierCommand;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

@Singleton
@Slf4j
public class MessageListener extends ListenerAdapter {
    @Inject @Nonnull Set<SubcommandGroupData> groups = new HashSet<>();
    @Inject @Nonnull Map<String, TerrierCommand> commandMap = new HashMap<>();
    @Inject @Nonnull Map<String, SlashHandler> slashMap = new HashMap<>();
    @Inject @Nonnull Map<String, ButtonHandler> buttonMap = new HashMap<>();

    @Inject
    public MessageListener() {
        super();
    }

    @Nonnull
    public CommandData allCommandData() {
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

    /** Slash interaction callback entry point. */
    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        String key = event.getSubcommandName();
        if (!slashMap.containsKey(key)) {
            log.info("Unrecognized subcommand: " + key);
            event.reply("Terrier doesn't recognize this subcommand!").queue();
        }
        log.info("/" + event.getFullCommandName());
        MessageCreateData reply =
                slashMap.get(key)
                        .onSlashInteraction(event.getUser().getIdLong(), event.getOptions());
        event.reply(reply).queue();
    }

    @Override
    public void onButtonInteraction(@Nonnull ButtonInteractionEvent event) {
        String key = event.getButton().getId();
        if (!buttonMap.containsKey(key)) {
            log.info("Unrecognized button id: " + key);
            event.reply("Terrier doesn't recognize this button!").queue();
        }
        log.info("button: " + key);
        MessageCreateData reply =
                buttonMap
                        .get(key)
                        .onButtonInteraction(event.getUser().getIdLong(), event.getButton());
        event.reply(reply).queue();
    }

    @Override
    public void onStringSelectInteraction(@Nonnull StringSelectInteractionEvent event) {
        event.reply("No handler for this category yet").queue();
    }
}
