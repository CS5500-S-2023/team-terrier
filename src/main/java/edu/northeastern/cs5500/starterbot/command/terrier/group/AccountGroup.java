package edu.northeastern.cs5500.starterbot.command.terrier.group;

import javax.inject.Inject;
import javax.inject.Singleton;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

@Singleton
public class AccountGroup extends SubcommandGroupData {
    @Inject
    public AccountGroup() {
        super("account", "Terrier does banking!");
    }
}
