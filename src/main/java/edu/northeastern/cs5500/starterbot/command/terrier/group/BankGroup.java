package edu.northeastern.cs5500.starterbot.command.terrier.group;

import javax.inject.Inject;
import javax.inject.Singleton;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

@Singleton
public class BankGroup extends SubcommandGroupData {
    @Inject
    public BankGroup() {
        super("bank", "Terrier does banking!");
    }
}
