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

The library follows a layered architecture with constructor-based dependency injection. The public service interfaces describe application use cases, service implementations orchestrate those use cases, domain ports isolate request execution, and infrastructure abstractions model low-level request handling.

```text
pl.vtt.wpi.core
├── application
│   ├── config       # AuthorizationHolder (thread-local auth state)
│   ├── context      # ApplicationServices facade + lazy composition root
│   ├── exception    # Application-level exceptions
│   └── service
│       ├── ...      # Service interfaces (LoginService, RuntimeDataService, etc.)
│       └── impl     # Service implementations wired through constructors
├── domain
│   ├── dto          # Request DTOs used by application services and ports
│   ├── model        # Domain values and device state models
│   └── port         # Input/Output contracts + endpoint-specific port implementations
└── infrastructure
    ├── Request / Response
    ├── RequestFactory / RequestHandler / RequestSender
    └── factory      # SynchronizedRequestFactory
```

### Layer responsibilities

| Layer | Responsibility |
|---|---|
| `application.service` | Stable use-case API exposed to library consumers. |
| `application.service.impl` | Use-case orchestration, validation, exception mapping, and constructor-injected dependencies. |
| `application.context` | Optional composition helper that lazily creates default service implementations from supplied ports. |
| `domain.model` / `domain.dto` | Shared domain values, device data records, credentials, users, and request DTOs. |
| `domain.port` | Generic `InputPort` / `OutputPort` contracts and endpoint-specific port implementations. |
| `infrastructure` | Request/response abstractions and request factory/sender/handler contracts used by ports. |

Application services do not create their own ports. Instead, callers either instantiate service implementations directly with constructor injection or use `LazyApplicationServices` as a small composition root. `LazyApplicationServices` accepts `Supplier` instances for all required ports, resolves each supplier only when a dependent service is first requested, and then reuses the created service instance.

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

### Application Services and lazy composition

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

Concrete implementations are provided in `pl.vtt.wpi.core.application.service.impl`. They use constructor injection, so tests and applications can provide fake or real `InputPort` / `OutputPort` implementations explicitly.

For applications that want a single access point, `pl.vtt.wpi.core.application.context` provides:

- `ApplicationServices` — a facade exposing getters for all service interfaces.
- `LazyApplicationServices` — a builder-based implementation that creates service implementations on first use.
- `Lazy<T>` — a thread-safe memoizing supplier used internally by the composition root.

Example composition:

```java
ApplicationServices services = LazyApplicationServices.builder()
    .authOutputPort(() -> authOutputPort)
    .adminPasswordResetInputPort(() -> adminPasswordResetInputPort)
    .usersOutputPort(() -> usersOutputPort)
    .userCreateRequestInputPort(() -> userCreateRequestInputPort)
    .changePasswordInputPort(() -> changePasswordInputPort)
    .removeUserInputPort(() -> removeUserInputPort)
    .runtimeDataOutputPort(() -> runtimeDataOutputPort)
    .pixelProgramsOutputPort(() -> pixelProgramsOutputPort)
    .runtimeDataInputPort(() -> runtimeDataInputPort)
    .pixelProgramsInputPort(() -> pixelProgramsInputPort)
    .wifiConfigInputPort(() -> wifiConfigInputPort)
    .deviceInfoOutputPort(() -> deviceInfoOutputPort)
    .logsOutputPort(() -> logsOutputPort)
    .logsDeleteInputPort(() -> logsDeleteInputPort)
    .currentStateOutputPort(() -> currentStateOutputPort)
    .rebootInputPort(() -> rebootInputPort)
    .build();

RuntimeDataService runtimeDataService = services.runtimeDataService();
```

Only `runtimeDataService`, `runtimeDataOutputPort`, `pixelProgramsOutputPort`, and `runtimeDataInputPort` are resolved by the final line in this example; unrelated ports remain uninitialized until another service is requested.

### Domain Ports

The package `pl.vtt.wpi.core.domain.port` contains port contracts and endpoint-specific implementations:

- **Output ports**: `AuthOutputPort`, `DeviceInfoOutputPort`, `RuntimeDataOutputPort`, `CurrentStateOutputPort`, `PixelProgramsOutputPort`, `UsersOutputPort`
- **Input ports**: `RuntimeDataInputPort`, `WifiConfigInputPort`, `PixelProgramsInputPort`, `UserCreateInputPort`, `RestartInputPort`, `LogsDeleteInputPort`

These ports encapsulate endpoint/method selection and exception mapping (`InputPortException` / `OutputPortException`), making application services thinner and easier to test.

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
