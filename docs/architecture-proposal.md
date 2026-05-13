# Architektura: stan obecny i kierunek Clean Architecture

## Stan obecny

Projekt jest biblioteką warstwową z elementami Ports & Adapters:

- `domain.model` zawiera modele domenowe, np. użytkownika, poświadczenia, kolory i dane urządzenia.
- `domain.port` zawiera abstrakcyjne kontrakty `InputPort` i `OutputPort` oraz wyjątki portów.
- `application.service` zawiera publiczne interfejsy przypadków użycia.
- `application.service.impl` zawiera implementacje przypadków użycia, które orkiestrują logikę aplikacyjną i przyjmują zależności przez konstruktory.
- `application.context` zawiera lekki composition root: `ApplicationServices`, `Lazy` i `LazyApplicationServices`.
- `infrastructure` zawiera model żądania/odpowiedzi, fabrykę żądań, abstrakcje wysyłania/obsługi żądań, DTO transportowe oraz adaptery HTTP.
- `infrastructure.adapter.http` zawiera endpoint-specific implementacje portów wejściowych i wyjściowych.
- `infrastructure.dto` zawiera transportowe rekordy żądań, np. `PasswordDto`, `UserCreateRequest` i `AdminPasswordResetRequest`.

Aktualny układ jest bliższy Clean Architecture niż wcześniejszy wariant, ponieważ implementacje portów HTTP i DTO transportowe zostały przeniesione poza domenę. Nie jest to jednak jeszcze ścisła Clean Architecture, ponieważ warstwa aplikacyjna nadal importuje DTO z `infrastructure.dto`, a `domain.model.endpoint` nadal zawiera szczegóły komunikacji, takie jak zasoby, metody HTTP i cele żądań.

## Leniwa inicjalizacja i DI przez konstruktor

`LazyApplicationServices` pełni rolę composition root biblioteki:

1. Builder przyjmuje dostawców portów (`Supplier<? extends InputPort<?>>` / `Supplier<? extends OutputPort<?>>`).
2. Dostawcy są opakowywani w memoizujący `Lazy<T>`.
3. Konkretne serwisy aplikacyjne są tworzone dopiero przy pierwszym wywołaniu odpowiedniej metody z `ApplicationServices`.
4. Po utworzeniu instancja serwisu jest reużywana.
5. Zależności nadal są przekazywane do implementacji serwisów przez konstruktory, więc same serwisy nie zależą od kontenera ani od mechanizmu lazy loading.

Przykład użycia:

```java
ApplicationServices services = LazyApplicationServices.builder()
        .authOutputPort(() -> new AuthOutputPort(requestFactory, authHandler))
        .adminPasswordResetInputPort(() -> adminPasswordResetPort)
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

LoginService loginService = services.loginService();
```

## Dlaczego to nadal nie jest pełna Clean Architecture

Najważniejsze pozostałe naruszenia kierunku zależności:

1. `application.service` i `application.service.impl` używają typów z `infrastructure.dto`. Warstwa aplikacyjna nie powinna importować infrastruktury.
2. `domain.model.endpoint` opisuje endpointy HTTP, zasoby, metody i reguły dostępu do request targetów. To jest szczegół komunikacji zewnętrznej, a nie czysty model domenowy.
3. Generyczne `InputPort<T>` i `OutputPort<T>` są poprawnym krokiem separacji, ale nie opisują języka biznesowego tak dobrze jak dedykowane gatewaye, np. `UserGateway`, `RuntimeDataGateway` albo `PixelProgramGateway`.
4. `application.context` tworzy konkretne implementacje usług, więc powinien pozostać composition rootem na brzegu modułu, a nie zależnością używaną wewnątrz domeny lub logiki aplikacyjnej.

## Kroki do uzyskania Clean Architecture

1. Wprowadzić modele wejściowe przypadków użycia w warstwie aplikacyjnej albo domenowej i usunąć importy `infrastructure.dto` z `application.service` oraz `application.service.impl`.
2. Zostawić DTO transportowe wyłącznie w `infrastructure.dto`; adaptery HTTP powinny mapować modele aplikacyjne/domenowe na DTO transportowe i odwrotnie.
3. Przenieść szczegóły endpointów (`RequestTarget`, `Resource`, HTTP `Method` używane jako protokół transportowy) z domeny do infrastruktury lub do konfiguracji adapterów HTTP.
4. Zastąpić część generycznych portów dedykowanymi gatewayami opisującymi intencje domenowe, np. odczyt użytkowników, zapis danych runtime albo aktualizację programów pikseli.
5. Utrzymywać regułę zależności: domena nie importuje aplikacji ani infrastruktury; aplikacja importuje domenę i abstrakcje; infrastruktura implementuje porty i zależy od warstw wewnętrznych.
6. Traktować `LazyApplicationServices` jako wygodny composition root biblioteki. Aplikacja wyższego poziomu może użyć własnego kontenera DI, o ile nadal wstrzykuje zależności przez konstruktory.

## Rekomendowany docelowy podział pakietów

```text
pl.vtt.wpi.core
├── domain
│   ├── model
│   └── gateway        # dedykowane porty/gatewaye w języku domeny
├── application
│   ├── command/query  # modele wejściowe przypadków użycia, jeśli nie są domenowe
│   ├── service        # interfejsy przypadków użycia
│   └── service.impl   # implementacje przypadków użycia
└── infrastructure
    ├── adapter.http   # implementacje gatewayów/portów przez HTTP
    ├── dto            # DTO transportowe
    └── factory        # fabryki requestów i konfiguracja adapterów
```

## Podsumowanie

Obecna architektura jest warstwowa i korzysta z elementów Ports & Adapters. Po przeniesieniu adapterów HTTP i DTO do infrastruktury domena jest czystsza, ale pełna Clean Architecture wymaga jeszcze usunięcia zależności aplikacji od infrastruktury oraz wyniesienia szczegółów HTTP z domeny.
