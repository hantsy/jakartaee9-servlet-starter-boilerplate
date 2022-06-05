package com.example.it;

import com.example.GreetingMessage;
import com.example.GreetingPagesServlet;
import com.example.GreetingService;
import com.example.GreetingServlet;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(ArquillianExtension.class)
public class GreetingPagesServletTest {
    
    private final static Logger LOGGER = Logger.getLogger(GreetingPagesServletTest.class.getName());
    
    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        var war = ShrinkWrap.create(WebArchive.class)
                .addClass(GreetingMessage.class)
                .addClass(GreetingService.class)
                .addClasses(GreetingPagesServlet.class)
                // add hello-pages.jspx
                .addAsWebResource(new File("src/main/webapp/hello-pages.jspx"), "hello-pages.jspx")
                // Enable CDI (Optional since Java EE 7.0)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource("test-web.xml", "web.xml")
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/jetty-env.xml"), "jetty-env.xml");
 
        
        Deployments.addExtraJars(war);
        
        LOGGER.log(Level.INFO, "war deployment: {0}", war.toString(true));
        return war;
    }
    
    @ArquillianResource
    private URL base;
    
    private Client client;
    
    @BeforeEach
    public void setup() {
        LOGGER.info("call BeforeEach");
        this.client = ClientBuilder.newClient();
        //removed the Jackson json provider registry, due to OpenLiberty 21.0.0.1 switched to use Resteasy.
    }
    
    @AfterEach
    public void teardown() {
        LOGGER.info("call AfterEach");
        if (this.client != null) {
            this.client.close();
        }
    }
    
    @Test
    @DisplayName("Given a name:`JakartaEE` should return `Say Hello to JakartaEE`")
    public void should_create_greeting() throws MalformedURLException {
        LOGGER.log(Level.INFO, " client: {0}, baseURL: {1}", new Object[]{client, base});
        final var greetingTarget = this.client.target(new URL(this.base, "GreetingPagesServlet?name=Tomcat").toExternalForm());
        try (final Response greetingGetResponse = greetingTarget.request()
                .accept(MediaType.TEXT_HTML)
                .get()) {
            assertThat(greetingGetResponse.getStatus()).isEqualTo(200);
            assertThat(greetingGetResponse.readEntity(String.class))
                    .contains("Say Hello to Tomcat");
        }
    }
}
