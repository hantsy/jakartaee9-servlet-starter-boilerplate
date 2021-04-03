package com.example;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;


public class GreetingMessageTest {

    @Test
    public void testGreetingMessage() {
        var message = GreetingMessage.of("Say Hello to JatartaEE");
        assertThat(message.getMessage()).isEqualTo("Say Hello to JatartaEE");
    }
}
