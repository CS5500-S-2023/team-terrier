package bot.discord.terrier.command;

import bot.discord.terrier.dao.RoomDao;
import bot.discord.terrier.model.Room;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

@Singleton
public class ListCommand implements TerrierCommand, SlashHandler {
    @Nonnull private static final String OPTION_NAME = "regex";

    @Nonnull
    private static final SubcommandData DESCRIPTOR =
            new SubcommandData("list", "Woof! Terrier looks for rooms!")
                    .addOption(OptionType.STRING, OPTION_NAME, "Terrier knows regex!", false);

    @Inject RoomDao roomDao;

    @Inject
    public ListCommand() {
        /** Injected default constructor */
    }

    @Override
    @Nonnull
    public SubcommandData getDescriptor() {
        return DESCRIPTOR;
    }

    @Override
    @Nonnull
    public MessageCreateData onSlashInteraction(
            long snowflakeId, @Nonnull List<OptionMapping> options) {
        String regex = ".*";
        for (OptionMapping mapping : options) {
            if (OPTION_NAME.equals(mapping.getName())) {
                regex = mapping.getAsString();
                break;
            }
        }
        MessageCreateBuilder builder =
                new MessageCreateBuilder().setContent("Terrier found these rooms:");
        for (Button button : getRooms(regex)) {
            builder.addActionRow(button);
        }
        if (builder.getComponents().isEmpty()) {
            builder.setContent("Terrier couldn't find any rooms!");
        }
        return builder.build();
    }

    @Nonnull
    public List<Button> getRooms(String regex) {
        List<Button> buttons = new ArrayList<>();
        for (Room room : roomDao.getRoomsByRegex(regex, 5)) {
            buttons.add(Button.success(room.getRoomName(), room.getRoomName()));
        }
        return buttons;
    }
}
