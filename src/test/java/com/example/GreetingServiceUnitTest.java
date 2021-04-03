package com.example;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GreetingServiceUnitTest {

    GreetingService service;

    @BeforeEach
    public void setup() {
        service = new GreetingService();
    }

    @Test
    public void testGreeting() {
        var message = service.buildGreetingMessage("JakartaEE");
        assertThat(message.getMessage()).startsWith("Say Hello to JakartaEE");
    }
}
