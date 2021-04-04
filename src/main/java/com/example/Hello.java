package com.example;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@RequestScoped
public class Hello {
    
    private String name;
    private GreetingMessage message;
    
    @Inject
    private GreetingService greetingService;
    
    public Hello() {
    }
    
    public Hello(GreetingService greetingService) {
        this.greetingService = greetingService;
    }
    
    public void createMessage() {
        message = greetingService.buildGreetingMessage(name);
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public GreetingMessage getMessage() {
        return message;
    }
    
}