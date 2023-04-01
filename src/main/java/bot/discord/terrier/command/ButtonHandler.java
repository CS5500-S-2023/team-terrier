package bot.discord.terrier.command;

import java.util.List;
import javax.annotation.Nonnull;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

/** This trait specifies that a command should handle button interactions. */
public interface ButtonHandler {
    /**
     * Specifies which button ids map to this handler.
     *
     * @return
     */
    @Nonnull
    public abstract List<String> getButtonNames();

    /**
     * @param snowflakeId Global unique id of user that pressed button.
     * @param button
     * @return
     */
    @Nonnull
    public MessageCreateData onButtonInteraction(long snowflakeId, @Nonnull Button button);
}
