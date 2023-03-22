package edu.northeastern.cs5500.starterbot.command;

import com.google.common.truth.Truth;
import edu.northeastern.cs5500.starterbot.command.terrier.WelcomeCommand;
import org.junit.jupiter.api.Test;

public class WelcomeCommandTest {

    private WelcomeCommand command = new WelcomeCommand();

    @Test
    void testWelcome() {
        Truth.assertThat(command.getGroup()).isNull();
        Truth.assertThat(command.getDescriptor().getName()).isEqualTo("welcome");
        Truth.assertThat(command.onSlashInteraction(0, null).getContent()).isNotNull();
    }
}
