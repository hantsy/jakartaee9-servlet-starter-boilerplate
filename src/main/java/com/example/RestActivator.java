package com.example;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.glassfish.jersey.server.ResourceConfig;

import java.util.Set;

@ApplicationPath("/api")
public class RestActivator extends ResourceConfig {
    public RestActivator() {
        packages("com.example");
    }

}
