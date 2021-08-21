# jakartaee9-servlet-starter-boilerplate(WIP)

![Compile and build](https://github.com/hantsy/jakartaee9-servlet-starter-boilerplate/workflows/Build/badge.svg)
![Unit Test](https://github.com/hantsy/jakartaee9-servlet-starter-boilerplate/workflows/test/badge.svg)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=hantsy_jakartaee9-servlet-starter-boilerplate&metric=alert_status)](https://sonarcloud.io/dashboard?id=hantsy_jakartaee9-servlet-starter-boilerplate)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=hantsy_jakartaee9-servlet-starter-boilerplate&metric=coverage)](https://sonarcloud.io/dashboard?id=hantsy_jakartaee9-servlet-starter-boilerplate)

Yeah, this is another starter boilerplate project for Jakarta EE (esp. Servlet/Java Web) developers.

This repository is a lightweight varient of the original [Jakarta EE 9 starter boilerplate](https://github.com/hantsy/jakartaee9-starter-boilerplate) which allows you to run applications on a Servlet 5.0 compatible container, such as Apache Tomcat, Eclipse Jetty, JBoss Undertow.

Currently the following features are added.

* Jakarta REST 3.0(Jersey 3.0)
* CDI 3.0 (Weld 4.0)
* Jakarta Server Faces 3.0(Mojarra 3.0)
* JSTL 2.0
* And transitive dependencies of above features, incuding Jakarta EL, Jarkarta Json Processing, Jakarta Json Binding, Jakarta Validation(Hibernate Validator), etc.

Most popular Servlet containers, such as Apache Tomcat, Eclipse Jetty, etc. have built-in Jakarta Servlet, Jakarta Server Pages, Jakarta EL and Jakarta WebSocket supports.

> For full fledged features support of Jakarta EE 9, please go to [hantsy/jakartaee9-starter-boilerplate](https://github.com/hantsy/jakartaee9-starter-boilerplate).

## Apache Tomcat 10

Run the following command to run the application on Tomcat 10.

```bash
mvn clean pacakge cargo:run
```

## Eclipse Jetty 

The new jetty-maven-plugin reorganises the former `run-fork` goal,etc and provides 3 modes.

* EMBEDDED
* FORKED
* EXTERNAL

By default, the mode is `EMBEDDED`, similar to the former simple `jetty:run` goal.

The `FORKED` mode uses a forked thread to run the application.

The `EXTERNAL` mode will run run the application on an external jetty distribution.

This application provides a configuration for `EXTERNAL` mode, check the *jetty-external* profile.

Download a copy of [Eclipse Jetty 11.0.2](https://www.eclipse.org/jetty) and set an environment variable `JETTY_HOME` to the location of jetty.
```bash
mvn clean jetty:run -Pjetty-external
```

> There is an issue when integrating Weld into Jetty 11.0.2, it will be fixed in 11.0.3, see [hantsy/jakartaee9-servlet-starter-boilerplate#1](https://github.com/hantsy/jakartaee9-servlet-starter-boilerplate/issues/1).

## Testing

The testing code is written with JUnit and Arquillian.

Run the following command to execute tests against a Tomcat 10 embedded adapter.

```bash 
mvn clean verify -Parq-tomcat-embedded
```

There is another `arq-weld` Maven profile which allows you to run tests on a Weld embedded adapter.

```bash 
mvn clean verify -Parq-weld
```

> Note: The `arq-weld` is only used to test CDI components.


## Resources

* [How to install CDI in Tomcat?](https://balusc.omnifaces.org/2013/10/how-to-install-cdi-in-tomcat.html)
* [Happy Jakarta EE 9 with Jersey 3.0.0](http://blog.supol.cz/?p=235)
* [Apache Tomcat 10 Documentation](https://tomcat.apache.org/tomcat-10.0-doc)
* [Weld Reference Documentation#Servlet containers (such as Tomcat or Jetty)](https://docs.jboss.org/weld/reference/latest/en-US/html/environments.html#weld-servlet)
* [Eclipse Jetty: Operations Guide](https://www.eclipse.org/jetty/documentation/jetty-11/operations-guide/index.html)
* [Eclipse Jetty: Programming Guide](http://www.eclipse.org/jetty/documentation/jetty-11/programming-guide/index.html)

  

