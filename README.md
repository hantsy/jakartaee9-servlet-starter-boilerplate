# jakartaee9-servlet-starter-boilerplate(WIP)

![Compile and build](https://github.com/hantsy/jakartaee9-servlet-starter-boilerplate/workflows/Build/badge.svg)
![it-tomcat](https://github.com/hantsy/jakartaee9-servlet-starter-boilerplate/actions/workflows/it-tomcat.yml/badge.svg)
![it-jetty](https://github.com/hantsy/jakartaee9-servlet-starter-boilerplate/actions/workflows/it-jetty.yml/badge.svg)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=hantsy_jakartaee9-servlet-starter-boilerplate&metric=alert_status)](https://sonarcloud.io/dashboard?id=hantsy_jakartaee9-servlet-starter-boilerplate)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=hantsy_jakartaee9-servlet-starter-boilerplate&metric=coverage)](https://sonarcloud.io/dashboard?id=hantsy_jakartaee9-servlet-starter-boilerplate)

Yeah, this is another starter boilerplate project for Jakarta EE (esp. Servlet/Java Web) developers.

This repository is a lightweight variant of the original [Jakarta EE 9 starter boilerplate](https://github.com/hantsy/jakartaee9-starter-boilerplate) which allows you to run applications on a Servlet 5.0 compatible container, such as Apache Tomcat, Eclipse Jetty, JBoss Undertow.

Currently, the following features are added.

* Jakarta REST 3.0(Jersey 3.0)
* CDI 3.0 (Weld 4.0)
* Jakarta Server Faces 3.0(Mojarra 3.0)
* And transitive dependencies of above features, incuding Jakarta EL, Jarkarta Json Processing, Jakarta Json Binding, Jakarta Validation(Hibernate Validator), etc.

Nowadays the most popular Servlet containers, such as Apache Tomcat, Eclipse Jetty, etc. have built-in Jakarta Servlet, Jakarta Server Pages, JSTL, Jakarta EL and Jakarta WebSocket supports.

> For full-fledged features support of Jakarta EE 9, please go to [hantsy/jakartaee9-starter-boilerplate](https://github.com/hantsy/jakartaee9-starter-boilerplate).

## Apache Tomcat 10

Run the following command to run the application on Tomcat 10.

```bash
mvn clean pacakge cargo:run
```

## Eclipse Jetty 11

The new `jetty-maven-plugin` reorganizes the former `run-fork` goal and provides 3 modes to run a Jetty server.

* EMBEDDED
* FORKED
* EXTERNAL

The default mode is `EMBEDDED` similar to the simplest `jetty:run` goal in the previous version.

The `FORKED` mode uses a forked thread to run the application.

The `EXTERNAL` mode runs the application on an external Jetty server.

This sample project provides a configuration for `EXTERNAL` mode. Check the *jetty-external* Maven profile.

Download a copy of the latest [Eclipse Jetty 11](https://www.eclipse.org/jetty) and set an environment variable `JETTY_HOME` to the location of the Jetty distribution.

```bash
mvn clean jetty:run -Pjetty-external
```

> There is an issue when integrating Weld into Jetty 11.0.2, it was fixed in 11.0.3, see [hantsy/jakartaee9-servlet-starter-boilerplate#1](https://github.com/hantsy/jakartaee9-servlet-starter-boilerplate/issues/1).

## Testing

The testing codes write with JUnit and Arquillian.

Run the following command to execute tests against a Tomcat 10 Embedded adapter.

```bash 
mvn clean verify -Parq-tomcat-embedded
```
Alternatively, run the following command to execute tests against a Jetty 11 Embedded adapter.

```bash 
mvn clean verify -Parq-jetty-embedded
```

There is another `arq-weld` Maven profile with which you can run tests on a Weld embedded adapter.

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

  

