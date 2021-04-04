package com.example.it;

import org.jboss.shrinkwrap.api.container.LibraryContainer;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

import java.io.File;

public class Deployments {
    public static void addExtraJars(LibraryContainer<?> war) {
        File[] extraJars =
                Maven.resolver()
                        .loadPomFromFile("pom.xml")
                        .importCompileAndRuntimeDependencies()
                        .resolve(
                                "org.assertj:assertj-core",
                                "org.hamcrest:hamcrest-core",
                                "org.mockito:mockito-core")
                        .withTransitivity()
                        .asFile();
        war.addAsLibraries(extraJars);
    }
}
