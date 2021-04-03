package com.example;

import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class GreetingService {
    private static final Logger LOGGER = Logger.getLogger(GreetingService.class.getName());
    
    public GreetingMessage buildGreetingMessage(String name) {
        var message = GreetingMessage.of("Say Hello to " + name + " at " + LocalDateTime.now());
        LOGGER.log(Level.INFO, "build message: {0}", message);
        return message;
    }
}
