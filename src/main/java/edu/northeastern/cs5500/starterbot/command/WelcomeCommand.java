package edu.northeastern.cs5500.starterbot.command;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

@Singleton
@Slf4j
public class WelcomeCommand implements SlashCommandHandler {

    @Inject
    public WelcomeCommand() {
        /* Injected default constructor. */
    }

    @Override
    @Nonnull
    public String getName() {
        return "welcome";
    }

    @Override
    @Nonnull
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Terrier says woof!");
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        log.info("event: /" + getName());

        event.reply(getReply()).queue();
    }

    @Nonnull
    public MessageCreateData getReply() {
        return new MessageCreateBuilder().setContent("Woof!").build();
    }
}
