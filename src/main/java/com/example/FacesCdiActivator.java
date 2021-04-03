package com.example;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.annotation.FacesConfig;
import jakarta.faces.annotation.FacesConfig.Version;

@FacesConfig(version = Version.JSF_2_3)
@ApplicationScoped
public class FacesCdiActivator {
    // ...
}