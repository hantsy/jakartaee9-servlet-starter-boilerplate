# Testing Jakarta EE 9 Web Application with Servlet Container

In the last post [Building JakartaEE 9 Web Applicaiton with Servlet Container](./build.md), we have described how to start a Jakarta EE 9 web application with the core Jakarta EE components, including CDI, Jakarta Faces, Jakarta Servlet, Jakarta Pages, Jakarta REST etc and run it in a Jakarta EE 9 compatiable Servlet container. In this post we will discuss how to test these components in a Servlet Container with the Arquillian testing framework.

The Arquillian project provides official support for Apache Tomcat and Eclipse Jetty, more information please go to [Arquillian Container Tomcat](https://github.com/arquillian/arquillian-container-tomcat) and [Arquillian Container Jetty](https://github.com/arquillian/arquillian-container-jetty). Currently both projects provide an *embeded* container adapter which supports the latest Apache Tomcat 10 and Eclipse Jetty 11, but there is no managed and remote adapters available.

If you are new to Arquillian testing framework, please read the official [Getting Started Guides](https://arquillian.org/guides/) or explore my previous Arqillian articles of [testing Jakarta EE 8 applicaitons](https://hantsy.github.io/jakartaee8-starter-boilerplate/) and [Jakarta EE 9 applications](https://hantsy.github.io/jakartaee9-starter-boilerplate/).

## Configuring Arquillian

Firstly add Arquillian Core and JUnit *BOM* to the *dependencyManagement* section of the project *pom.xml* file.

```xml
<dependencyManagement>
    // ... 
    <dependencies>
        <dependency>
            <groupId>org.jboss.arquillian</groupId>
            <artifactId>arquillian-bom</artifactId>
            <version>${arquillian-bom.version}</version>
            <scope>import</scope>
            <type>pom</type>
        </dependency>
        <dependency>
            <groupId>org.junit</groupId>
            <artifactId>junit-bom</artifactId>
            <version>${junit-jupiter.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

Add the following dependencies in the project *dependencies* section.

```xml
<dependencies>
    //...
    <dependency>
        <groupId>org.jboss.arquillian.protocol</groupId>
        <artifactId>arquillian-protocol-servlet-jakarta</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.jboss.shrinkwrap.resolver</groupId>
        <artifactId>shrinkwrap-resolver-impl-maven</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```      
You can also add the following testing utility libs to improve your testing codes.

```xml
<dependencies>
    //....
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-params</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-core</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.hamcrest</groupId>
        <artifactId>hamcrest</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.assertj</groupId>
        <artifactId>assertj-core</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>    
```

Next we will configure Arquillian Tomcat Embedded Adapter to run the testing codes agaisnt an embedded Apache Tomcat container. 

## Configuring Arquillian Tomcat Embedded Adapter

Create a new Maven profile to centralize all configurations of the Arquillian tomcat embeded adapter.

```xml
<profile>
    <id>arq-tomcat-embedded</id>
    <properties>
        <skipTests>false</skipTests>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.jboss.arquillian.container</groupId>
            <artifactId>arquillian-tomcat-embedded-10</artifactId>
            <version>${arquillian-tomcat.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.apache.tomcat.embed</groupId>
            <artifactId>tomcat-embed-core</artifactId>
            <version>${tomcat.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.apache.tomcat.embed</groupId>
            <artifactId>tomcat-embed-jasper</artifactId>
            <version>${tomcat.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.apache.tomcat.embed</groupId>
            <artifactId>tomcat-embed-websocket</artifactId>
            <version>${tomcat.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
</profile>
```
Now we will write some testing codes.

## Testing Jakarta Components

For simple POJOs, you can write a simple JUnit test for verify the funcationality. For example, `GreetingMessage` is a simple POJO used to assemble a readable greeting message. We can write a simple JUnit test to check if it is working as expected. 

```java
public class GreetingMessageTest {

    @Test
    public void testGreetingMessage() {
        var message = GreetingMessage.of("Say Hello to JatartaEE");
        assertThat(message.getMessage()).isEqualTo("Say Hello to JatartaEE");
    }
}
```

The `GreetingService` bean itself is just implementing a simple funcationality that is used to build a greeting message using the `buildGreetingMessage` method that accepts an argument to setup the target of greeting. Just like previous testing example, create a simple JUnit test to verify if it is functional as expected.

```java
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
```

The `Hello` bean dependes on `GreetingService` bean. To test the functionality of `Hello` in a Unit Test, we can use Mockito to isolate the dependency - `GreetingService`. In the following `HelloTest`, we created a mocked object of `GreetingService` in the tests. 

```java
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
```

We have tested the simple POJOs in unit tests, for other Jakarta EE components, such as Servlet, Jakarta Pages, etc, we have to verify the functionality in a Servlet container, we will write integation tests using Arquillian for this scenario.

To run unit tests and integration tests in different phase, we can configure `maven-surefire-plugin` and `maven-failsafe-plugin` as the following, and make sure integration tests run in `integration-test` phase. 

```xml
<plugins>
    //...
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>${maven-surefire-plugin.version}</version>
        <configuration>
            <skipTests>${skipTests}</skipTests>
        </configuration>
        <executions>
            <execution>
                <id>default-test</id>
                <phase>test</phase>
                <goals>
                    <goal>test</goal>
                </goals>
                <configuration>
                    <excludes>
                        <exclude>**/it/**</exclude>
                    </excludes>
                </configuration>
            </execution>
        </executions>
    </plugin>
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-failsafe-plugin</artifactId>
        <version>${maven-failsafe-plugin.version}</version>
        <configuration>
            <skipITs>${skipTests}</skipITs>
        </configuration>
        <executions>
            <execution>
                <id>integration-test</id>
                <phase>integration-test</phase>
                <goals>
                    <goal>integration-test</goal>
                    <goal>verify</goal>
                </goals>
                <configuration>
                    <includes>
                        <include>**/it/**</include>
                    </includes>
                </configuration>
            </execution>
        </executions>
    </plugin>
</plugins>
```

Firstly, let's have a look at the `GreetingServiceTest` that is used for testing the simple CDI bean `GreetingService`.

```java 
@ExtendWith(ArquillianExtension.class)
public class GreetingServiceTest {
    
    private final static Logger LOGGER = Logger.getLogger(GreetingServiceTest.class.getName());
    
    @Deployment
    public static WebArchive createDeployment() {
        var war = ShrinkWrap.create(WebArchive.class)
                .addClass(GreetingMessage.class)
                .addClass(GreetingService.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource("test-web.xml", "web.xml")
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/jetty-env.xml"), "jetty-env.xml");
        
        Deployments.addExtraJars(war);
        
        LOGGER.log(Level.INFO, "war deployment: {0}", war.toString(true));
        return war;
    }
    
    @Inject
    GreetingService service;
    
    @Test
    @DisplayName("testing buildGreetingMessage")
    public void should_create_greeting() {
        LOGGER.log(Level.INFO, " Running test:: GreetingServiceTest#should_create_greeting ... ");
        var message = service.buildGreetingMessage("Jakarta EE");
        assertTrue(message.getMessage().startsWith("Say Hello to Jakarta EE at "),
                "message should start with \"Say Hello to Jakarta EE at \"");
    }
}
```

As you see, an Arquillian integration test is annotated with `@ExtendWith(ArquillianExtension.class)`, which is a standard JUnit 5 extension. 

In an Arquillian test, you have to create a minimal deployment archive via a static `@Deployment` annotated method. In the `@Deployment` method, you can prepare the resource that will be packaged and deployed to the target runtime before running test cases. 

In the test class, you can inject available beans like a CDI bean, for example, we inject `GreetingService` here, then in the test method, use `GreetingService` bean to verify the functionality.

Open your terminal, execute the following command to run `GreetingServiceTest`.

```bash 
mvn clean verify -Parq-tomcat-embeded -Dit.test=GreetingServiceTest
```

When running Arquillian test, it will package the deployment resources into a deployable archive, and deploy it to the target container, then run the test in the container, the JUnit client agent will gather the running result through a proxy that interacts with tests in the container. 

Let's move to test `GreetingResource`. 

```java
@ExtendWith(ArquillianExtension.class)
public class GreetingResourceTest {
    
    private final static Logger LOGGER = Logger.getLogger(GreetingResourceTest.class.getName());
    
    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        var war = ShrinkWrap.create(WebArchive.class)
                .addClass(GreetingMessage.class)
                .addClass(GreetingService.class)
                .addClasses(GreetingResource.class)
                .addClasses(RestActivator.class)
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
        final var greetingTarget = this.client.target(new URL(this.base, "api/greeting/JakartaEE").toExternalForm());
        try (final Response greetingGetResponse = greetingTarget.request()
                .accept(MediaType.APPLICATION_JSON)
                .get()) {
            assertThat(greetingGetResponse.getStatus()).isEqualTo(200);
            assertThat(greetingGetResponse.readEntity(GreetingMessage.class).getMessage())
                    .startsWith("Say Hello to JakartaEE");
        }
    }
}
```

Unlike `GreetingServiceTest`, to test the funtionality of `GreetingResource`, we use Jakarta REST Client API to interact with the HTTP APIs in a client view. 

Adding a `testable=false` attribute in the `@Deployment` annotation means all tests will be run in the client mode.

Alternatively, you can also add a single `@RunAsClient` on the test method to run it locally. 

The `@ArquillianResource` will inject the base URL of the deployment archive in the container after it is deployed. 

Execute the following command to run `GreetingServiceTest`.

```bash 
mvn clean verify -Parq-tomcat-embeded -Dit.test=GreetingResourceTest
```

> If a `@Deployment(testable=true)` is applied on the deployment method, thus all tests run as client mode, we can NOT inject beans in the test class as the previous example.

Similarly, we can create *client mode* tests to verify the functionalities of simple Jakarta Servlet, Jakarta Faces, Jakarta Pages, etc. The complete codes can be found [here](https://github.com/hantsy/jakartaee9-servlet-starter-boilerplate).

> To verify the HTML elements and Ajax interactions in the rendered HTML pages, please check [Arquillian Extension Drone](https://github.com/arquillian/arquillian-extension-drone) and [Arquillian Graphene](https://github.com/arquillian/arquillian-graphene).

## Configuring Jetty Embedded Adapter

Add a new Maven profile for configuring Arquillian Jetty Embedded Adapter.

```xml
<profile>
    <id>arq-jetty-embedded</id>
    <properties>
        <skipTests>false</skipTests>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.jboss.arquillian.container</groupId>
            <artifactId>arquillian-jetty-embedded-11</artifactId>
            <version>${arquillian-jetty.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.eclipse.jetty</groupId>
            <artifactId>jetty-annotations</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.eclipse.jetty</groupId>
            <artifactId>jetty-plus</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.eclipse.jetty</groupId>
            <artifactId>jetty-deploy</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.eclipse.jetty</groupId>
            <artifactId>jetty-servlet</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.eclipse.jetty</groupId>
            <artifactId>jetty-webapp</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.eclipse.jetty</groupId>
            <artifactId>jetty-cdi</artifactId>
            <scope>test</scope>
            <!-- to remove when 11.0.10 released -->
            <!-- see https://github.com/eclipse/jetty.project/pull/7991 -->
            <version>${jetty.version}</version>
        </dependency>
        <dependency>
            <groupId>org.eclipse.jetty.websocket</groupId>
            <artifactId>websocket-jakarta-server</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.eclipse.jetty</groupId>
            <artifactId>apache-jsp</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-simple</artifactId>
            <version>1.7.36</version>
            <scope>test</scope>
        </dependency>
        <!-- see: https://github.com/arquillian/arquillian-container-jetty/pull/108 -->
        <!--<dependency>
            <groupId>org.jboss.arquillian.testenricher</groupId>
            <artifactId>arquillian-testenricher-cdi-jakarta</artifactId>
        </dependency>-->
    </dependencies>
</profile>
```

Run the previous tests against the `arq-jetty-embedded` profile, for example.

```bash 
mvn clean verify -Parq-jetty-embeded -Dit.test=GreetingResourceTest
```

## Configuring Arquillian Weld Embedded

Arquillian project provides an official extensions to test CDI beans in an embedded Weld container. 

Create a new Maven profile to configure Arquillian Weld Embedded Adapter, and ues `maven-failsafe-plugin` to filter out the tests of Jakarta Servlet, Jakarta Faces, etc.

```xml
<profile>
    <id>arq-weld</id>
    <properties>
        <skipTests>false</skipTests>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.jboss.arquillian.container</groupId>
            <artifactId>arquillian-weld-embedded</artifactId>
            <version>${arquillian-weld-embedded.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.jboss.weld</groupId>
            <artifactId>weld-core-impl</artifactId>
            <version>${weld.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-failsafe-plugin</artifactId>
                <version>${maven-failsafe-plugin.version}</version>
                <configuration>
                    <systemPropertyVariables>
                        <arquillian.launch>arq-weld</arquillian.launch>
                    </systemPropertyVariables>
                    <excludes>
                        <exclude>**/it/GreetingResourceTest*</exclude>
                        <exclude>**/it/GreetingServletTest*</exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</profile>
```

Execute the following command to run the `GreetingServiceTest` .

```bash
mvn clean verify -Parq-weld -Dit.test=GreetingServiceTest
```

> The testing codes of Jakarta Servlet, Jakarta Pages and Jakarta Faces require a Servlet container. 

Get the [complete source codes](https://github.com/hantsy/jakartaee9-servlet-starter-boilerplate) from my Github account.
