# api-service-remindandinform Project

[RemindAndinform documentation](https://sundsvall.atlassian.net/wiki/spaces/SK/pages/823558153/RemindAndInform)

## Config

### Production-config

- **API Gateway:**                  api-i.sundsvall.se
    - **Endpoint:**                 Production
- **Server:**                       microservices.sundsvall.se
- **DB:**                           Maria DB
- **Dependency versions:**          Production

### Test-config

- **API Gateway:**                  api-i-test.sundsvall.se
    - **Endpoint:**                 Production
- **Server:**                       microservices-test.sundsvall.se
- **DB:**                           Maria DB
- **Dependency versions:**          Test

### Sandbox-config

- **API Gateway:**                  api-i-test.sundsvall.se
    - **Endpoint:**                 Sandbox
- **Server:**                       microservices-test.sundsvall.se
- **DB:**                           Maria DB
- **Dependency versions:**          Mocked (Wiremock)


## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Running the application on local computer as docker container

In order to run the docker container you must have Docker installed on your computer (for Windows: Docker Desktop).

Use this command to build and run the application with sandbox config, using a native build:  
`docker-compose -f src/main/docker/docker-compose-sandbox.yaml build && docker-compose -f src/main/docker/docker-compose-sandbox.yaml up`

Use this command to build and run the application with sandbox config, using JVM build:  
`docker-compose -f src/main/docker/docker-compose-sandbox-jvm.yaml build && docker-compose -f src/main/docker/docker-compose-sandbox-jvm.yaml up`

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/api-service-remindandinform-1.0.0-SNAPSHOT-runner`

You can also create a native executable packaged in a docker image using:  
`docker-compose -f src/main/docker/docker-compose.yaml build`
