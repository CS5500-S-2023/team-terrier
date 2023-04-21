package bot.discord.terrier.command.room;

import javax.inject.Inject;
import javax.inject.Singleton;

import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

@Singleton
public class RoomGroup extends SubcommandGroupData {
    @Inject
    public RoomGroup() {
        super("room", "Terrier manages rooms!");
    }
}
