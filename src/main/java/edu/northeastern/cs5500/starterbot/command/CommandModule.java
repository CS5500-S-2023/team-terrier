package edu.northeastern.cs5500.starterbot.command;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;

/**
 * This is essentially a config class for declaring which handlers are injected at runtime.
 *
 * <p>TODO: Maybe inject into a Map in the future?
 */
@Module
public class CommandModule {

    @Provides
    @IntoSet
    public SlashCommandHandler provideButtonCommand(ButtonCommand buttonCommand) {
        return buttonCommand;
    }

    @Provides
    @IntoSet
    public ButtonHandler provideButtonCommandClickHandler(ButtonCommand buttonCommand) {
        return buttonCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideDropdownCommand(DropdownCommand dropdownCommand) {
        return dropdownCommand;
    }

    @Provides
    @IntoSet
    public StringSelectHandler provideDropdownCommandMenuHandler(DropdownCommand dropdownCommand) {
        return dropdownCommand;
    }
}
