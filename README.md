# WPI Core

**WPI Core** is a Java library providing foundational abstractions for secure API communication, including authentication, endpoint access control, and request/response modeling, designed to be consumed by higher-level WPI client implementations.

Part of the **Waterflow Pixel (WP)** ecosystem developed by [VerbaTechTeam](https://github.com/VerbaTechTeam) — *We create words that devices understand.*

---

## Ecosystem

`wpi-core` is the foundational module of the **Waterflow Pixel Interface (WPI)** layer — the communication and logic tier of the Waterflow Pixel system. The broader ecosystem includes:

| Repository | Layer | Description |
|---|---|---|
| **wpi-core** *(this repo)* | WPI | Core API abstractions in Java — auth, endpoints, request model |
| [waterflow-pixel-unit](https://github.com/VerbaTechTeam/waterflow-pixel-unit) | WPU | MicroPython firmware for Raspberry Pi Pico 2W with built-in REST HTTP server for direct LED strip control |

For a full overview of the system architecture and roadmap (including the planned **WPC** controller layer and **wpu-emulator**), see the [VerbaTechTeam organization profile](https://github.com/VerbaTechTeam).

---

## Requirements

- Java 25+
- Maven 3.x

## Installation

> **Note:** The project is in early development. Maven distribution is not yet available.

Clone the repository and install the artifact to your local Maven repository:

```bash
git clone https://github.com/VerbaTechTeam/wpi-core.git
cd wpi-core
mvn install -DskipTests
```

Then reference it in your project's `pom.xml`:

```xml
<dependency>
    <groupId>pl.vtt</groupId>
    <artifactId>wpi-core</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

## Architecture

The library follows a clean, layered architecture:

```
pl.vtt.wpi.core
├── application
│   ├── config       # AuthorizationHolder (thread-local auth state)
│   ├── exception    # Domain exceptions (e.g. IncorrectUsernameOrPasswordException)
│   ├── service      # Service interfaces (LoginService)
│   │   └── impl     # Internal implementations (not exported)
│   └── util         # RequestFactory, RequestAgent, ResponseProxy, ResponseDeserializer interfaces
└── domain
    └── model
        ├── Authorization.java
        ├── Credentials.java
        ├── Request.java
        └── endpoint
            ├── EndpointDescriptor.java
            ├── Method.java
            ├── RequestTarget.java
            ├── Resource.java
            └── UserGroup.java
```

## Key Concepts

### Authentication

Login is handled by `LoginService`. On success, the resulting `Credentials` (username + token) are stored as a Basic Auth header in `AuthorizationHolder` — a thread-local holder used to attach authorization to outgoing requests.

```java
// Provided by a higher-level module
LoginService loginService = ...;

Credentials credentials = loginService.login("admin", "password");
loginService.logout();
```

### Endpoints & Access Control

Endpoints are defined as `RequestTarget` enum entries. Each entry declares:

| Property | Description |
|---|---|
| `Resource` | URL path (with optional format args) |
| `allowedMethods` | Permitted HTTP methods |
| `requiredAnyGroups` | User groups allowed to access the endpoint |
| `mutating` | Whether the operation modifies state |

Available user groups: `ADMIN`, `DESIGNER`, `EDITOR`.

Access can be checked at runtime:

```java
boolean allowed = RequestTarget.DATA_UPDATE.allow(Method.PUT, userGroups);
```

### Request Model

A `Request<T>` carries the target URL, authorization header, and an optional typed payload:

```java
record Request<T>(Method method, String url, Authorization authorization, T payload) {}
```

### Implementing a Client

`wpi-core` exposes only the `LoginService` interface — the implementation is internal to the module. A higher-level module (e.g. `wpi-desktop`) is responsible for instantiating the service and exposing it to the client.

From the client's perspective, usage is limited to the public interface:

```java
// Provided by a higher-level module
LoginService loginService = ...;

Credentials credentials = loginService.login("admin", "password");
loginService.logout();
```

## Building & Testing

```bash
# Build and run tests
mvn test

# Package
mvn package
```

Tests are written with JUnit Jupiter 5 and cover login success, invalid credentials, null/blank inputs, and logout behavior.

## CI

GitHub Actions runs the build and test suite on every push and on pull requests targeting `main`. See [`.github/workflows/ci.yml`](.github/workflows/ci.yml).

## License

Proprietary — VTT. All rights reserved.
