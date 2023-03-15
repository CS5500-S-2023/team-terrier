package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.Test;

public class WelcomeCommandTest {

    private WelcomeCommand command = new WelcomeCommand();

    @Test
    void testNameMatchesData() {
        assertThat(command.getName()).isEqualTo(command.getCommandData().getName());
    }

    @Test
    void testReplyContent() {
        assertThat(command.getReply().getContent()).isEqualTo("Woof!");
    }
}
