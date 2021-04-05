package com.example;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class HelloTest {
    
    @ParameterizedTest
    @MethodSource("provideQueryCriteria")
    public void testCreateMessage(String name, String result) {
        var service = mock(GreetingService.class);
        given(service.buildGreetingMessage(name)).willReturn(GreetingMessage.of("Say Hello to " + name));
        
        var hello = new Hello(service);
        
        hello.setName(name);
        hello.createMessage();
        assertThat(hello.getName()).isEqualTo(name);
        assertThat(hello.getMessage().getMessage()).isEqualTo(result);
        verify(service, times(1)).buildGreetingMessage(anyString());
        verifyNoMoreInteractions(service);
    }
    
    static Stream<Arguments> provideQueryCriteria() {
        return Stream.of(
                Arguments.of("Tomcat", "Say Hello to Tomcat"),
                Arguments.of("JakartaEE", "Say Hello to JakartaEE")
        );
    }
}
