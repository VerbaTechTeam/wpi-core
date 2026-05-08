# WPI Core

**WPI Core** is a Java library providing foundational abstractions for secure API communication, including authentication, endpoint access control, request/response modeling, and application-level service contracts for WPI clients.

Part of the **Waterflow Pixel (WP)** ecosystem developed by [VerbaTechTeam](https://github.com/VerbaTechTeam) — *We create words that devices understand.*

---

## Ecosystem

`wpi-core` is the foundational module of the **Waterflow Pixel Interface (WPI)** layer — the communication and logic tier of the Waterflow Pixel system. The broader ecosystem includes:

| Repository | Layer | Description |
|---|---|---|
| **wpi-core** *(this repo)* | WPI | Core API abstractions in Java — auth, endpoints, request model, service contracts |
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

The library follows a layered architecture:

```text
pl.vtt.wpi.core
├── application
│   ├── config       # AuthorizationHolder (thread-local auth state)
│   ├── exception    # Application-level exceptions
│   └── service
│       ├── ...      # Service interfaces (LoginService, RuntimeDataService, etc.)
│       └── impl     # Internal service implementations
├── domain
│   ├── model
│   └── port         # InputPort / OutputPort contracts
└── infrastructure
    ├── dto          # Transport request DTOs
    ├── adapter.http # HTTP implementations of input/output ports
    ├── Request / Response
    ├── RequestFactory / RequestHandler / RequestSender
    └── factory      # SynchronizedRequestFactory
```

## Key Concepts

### Authentication

Login is handled by `LoginService`. Internally, `LoginServiceImpl` delegates authorization to a dedicated output port (`AuthOutputPort`), so request execution is decoupled from service orchestration.

On success, resulting `Credentials` (username + token) are stored in `AuthorizationHolder` — a thread-local holder used to attach authorization to outgoing requests.

```java
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

A `Request<T>` carries timestamp, HTTP method, target URL, authorization header, and an optional typed payload:

```java
Request<T> request = new Request<>(
    null,          // timestamp (null => now)
    Method.GET,
    "https://host/api/data",
    authorization,
    payload
);
```

### Application Services

The package `pl.vtt.wpi.core.application.service` currently exposes interfaces for:

- `LoginService`
- `AdminPasswordService`
- `UserManagementService`
- `RuntimeDataService`
- `PixelProgramService`
- `NetworkConfigurationService`
- `DeviceInfoService`
- `DebugService`
- `RebootService`

Concrete implementations are provided in `pl.vtt.wpi.core.application.service.impl`.

### Ports and HTTP Adapters

The package `pl.vtt.wpi.core.domain.port` contains generic port contracts:

- **Port contracts**: `InputPort`, `OutputPort`

Endpoint-specific HTTP implementations are provided by `pl.vtt.wpi.core.infrastructure.adapter.http`:

- **HTTP port adapters**: `AuthOutputPort`, `DeviceInfoOutputPort`, `RuntimeDataOutputPort`, `CurrentStateOutputPort`, `PixelProgramsOutputPort`, `UsersOutputPort`, `RuntimeDataInputPort`, `WifiConfigInputPort`, `PixelProgramsInputPort`, `UserCreateInputPort`, `RestartInputPort`, `LogsDeleteInputPort`

These adapters encapsulate endpoint/method selection and exception mapping (`InputPortException` / `OutputPortException`), making application services thinner and easier to test.

## Building & Testing

```bash
# Build and run tests
mvn test

# Package
mvn package
```

Unit tests are written with JUnit Jupiter 5 and currently include coverage for port-level behavior and application-service logic (including `UserCreateInputPort` / `UserCreateRequest`, login flow, and related service orchestration paths).

## CI

GitHub Actions runs the build and test suite on every push and on pull requests targeting `main`. See [`.github/workflows/ci.yml`](.github/workflows/ci.yml).

## License

Proprietary — VTT. All rights reserved.
