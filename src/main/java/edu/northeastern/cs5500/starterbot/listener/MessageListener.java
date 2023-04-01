package edu.northeastern.cs5500.starterbot.listener;

import edu.northeastern.cs5500.starterbot.command.TerrierCommands;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

/**
 * This is essentially a callback scheduler that reacts to different events. For now only 3 type of
 * events are handled: 1. Slash commands 2. Button clicks 3. String selects
 */
public class MessageListener extends ListenerAdapter {
    @Inject TerrierCommands terrierCommands;

    @Inject
    public MessageListener() {
        super();
    }

    /** Slash interaction callback entry point. */
    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        MessageCreateData reply = terrierCommands.onSlashInteraction(event);
        event.reply(reply).queue();
    }

    public @Nonnull Collection<CommandData> allCommandData() {
        Collection<CommandData> commandData = new ArrayList<>();
        commandData.add(terrierCommands.getDescriptors());

        return commandData;
    }

    @Override
    public void onButtonInteraction(@Nonnull ButtonInteractionEvent event) {
        MessageCreateData reply = terrierCommands.onButtonInteraction(event);
        event.reply(reply).queue();
    }

    @Override
    public void onStringSelectInteraction(@Nonnull StringSelectInteractionEvent event) {
        event.reply("No handler for this category yet").queue();
    }
}
