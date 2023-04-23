package bot.discord.terrier;

import bot.discord.terrier.command.CommandModule;
import bot.discord.terrier.dao.common.DaoProdModule;
import bot.discord.terrier.listener.MessageListener;
import dagger.Component;
import java.util.Collection;
import java.util.EnumSet;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

@Component(modules = {CommandModule.class, DaoProdModule.class})
@Singleton
interface BotComponent {
    public Bot bot();
}

@Singleton
public class Bot {

    @Inject MessageListener messageListener;

    @Inject
    Bot() {}

    /**
     * Attempts to get BOT_TOKEN environment variable. Throws exception if variable isn't set.
     *
     * @return BOT_TOKEN
     */
    @Nonnull
    private String getBotToken() {
        String token = new ProcessBuilder().environment().get("BOT_TOKEN");
        if (token == null) {
            throw new IllegalArgumentException(
                    "The BOT_TOKEN environment variable is not defined.");
        }
        return token;
    }

    /**
     * Bot does 2 things: 1. Register our message listener to handle interactions. 2. Notify Discord
     * of available commands.
     */
    public void start() {
        String token = getBotToken();

        @SuppressWarnings("null")
        @Nonnull
        Collection<GatewayIntent> intents = EnumSet.noneOf(GatewayIntent.class);
        JDA jda = JDABuilder.createLight(token, intents).addEventListeners(messageListener).build();

        CommandListUpdateAction commands = jda.updateCommands();
        commands.addCommands(messageListener.allCommandData());
        commands.queue();
    }
}
