# Spring Boot Async MDC

This project demonstrates how to preserve Mapped Diagnostic Context (MDC) values in methods annotated with `@Async`
in a Spring Boot application.

## Overview

In a typical Spring Boot application, MDC values are used to enrich log entries with context-specific information.
However, MDC values are not automatically propagated to asynchronous methods annotated with `@Async`. 
This project showcases how to address this challenge.

## Features

- **MDC Propagation**: Demonstrates how to propagate MDC values to `@Async` methods.
- **Spring Boot Configuration**: Outlines the necessary Spring Boot configuration to enable MDC propagation.
- **Example Usage**: Provides code examples to illustrate the feature.

## Prerequisites

- Java 21
- Gradle

## Getting Started

### Clone the Repository

```sh
git clone https://github.com/ehayik/spring-boot-async-mdc.git
cd spring-boot-async-mdc
```

### Build the Project

```sh
./gradlew build
```

### Run the Application

```sh
./gradlew bootRun
```

## Usage

### Important Classes and Annotations

- **`@Async`**: Annotation to mark methods to be executed asynchronously.
- **`MdcTaskDecorator`**: Custom task decorator to propagate MDC values.

### Example

#### Service Class

```java
@Slf4j
@Service
class UserService {

    @Async
    @SneakyThrows
    CompletableFuture<User> createAsync(User newUser) {
        log.info("Simulating a slow, time-consuming process to create user {}.", newUser.username());
        Thread.sleep(5000);
        return CompletableFuture.completedFuture(newUser);
    }
}
```

#### Task Decorator Class

```java
@Component
public class MdcTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
            try {
                if (contextMap != null) {
                    MDC.setContextMap(contextMap);
                }
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}
```

#### Configuration Class

```java
@Configuration
@RequiredArgsConstructor
class LoggingConfiguration implements WebMvcConfigurer {

    private final TraceIdInterceptor traceIdInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(traceIdInterceptor);
    }
}
```