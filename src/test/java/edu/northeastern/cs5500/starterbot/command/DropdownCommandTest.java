package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.Test;

public class DropdownCommandTest {

    private DropdownCommand command = new DropdownCommand();

    @Test
    void testNameMatchesData() {
        assertThat(command.getName()).isEqualTo(command.getCommandData().getName());
    }
}
