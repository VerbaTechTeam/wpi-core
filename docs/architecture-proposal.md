# Propozycja: leniwa inicjalizacja, DI przez konstruktor i kierunek Clean Architecture

## Aktualna architektura

Projekt jest biblioteką warstwową z elementami architektury portów i adapterów:

- `domain.model` przechowuje modele domenowe, np. użytkownika, poświadczenia i dane urządzenia.
- `domain.port` definiuje kontrakty `InputPort` i `OutputPort`, a implementacje portów specyficzne dla endpointów znajdują się w `infrastructure.adapter.http`.
- `application.service` definiuje przypadki użycia widoczne dla klienta biblioteki.
- `application.service.impl` orkiestruje przypadki użycia i komunikuje się z portami przez konstruktory.
- `infrastructure` definiuje model żądania/odpowiedzi oraz abstrakcje wysyłania i obsługi żądań.

To nadal nie jest jeszcze ścisła Clean Architecture. Implementacje portów HTTP znajdują się już poza domeną, ale warstwa aplikacyjna importuje transportowe DTO z `infrastructure.dto`, a `domain.model.endpoint` nadal przechowuje szczegóły endpointów i metod komunikacji. W Clean Architecture kierunek zależności powinien prowadzić do środka: aplikacja nie powinna importować infrastruktury, a szczegóły protokołu HTTP powinny pozostać w adapterach zewnętrznych.

## Implementacja leniwej inicjalizacji i DI przez konstruktor

Dodany został prosty composition root w `application.context`:

1. `ApplicationServices` jest fasadą udostępniającą interfejsy przypadków użycia.
2. `LazyApplicationServices` przyjmuje dostawców portów w builderze.
3. Każdy dostawca portu jest opakowany w memoizujący `Lazy<T>`.
4. Konkretne serwisy są tworzone dopiero przy pierwszym wywołaniu odpowiedniej metody fasady.
5. Gdy serwis jest tworzony, zależności są przekazywane przez jego konstruktor, więc same implementacje usług pozostają niezależne od kontenera.

Przykład użycia po stronie aplikacji-klienta:

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

## Co zrobić, aby architekturę można było uznać za Clean Architecture

1. Pozostawić implementacje komunikacji z endpointami w zewnętrznym pakiecie adapterów, np. `infrastructure.adapter.http`, i nie cofać ich do domeny.
2. Pozostawić w domenie wyłącznie stabilne modele i abstrakcyjne porty, bez zależności od `RequestFactory`, `RequestHandler`, `RequestSender`, URL-i ani metod HTTP.
3. Rozdzielić porty use-case od portów gateway. Interfejsy przypadków użycia mogą pozostać w `application.service`, natomiast gatewaye powinny wyrażać język domeny, np. `UserGateway`, `RuntimeDataGateway`, `PixelProgramGateway`.
4. Nie przeciekać transportowych DTO z `infrastructure.dto` do warstwy aplikacyjnej. Jeśli `PasswordDto`, `UserCreateRequest` albo `AdminPasswordResetRequest` są częścią kontraktu przypadków użycia, powinny zostać zastąpione modelami wejściowymi aplikacji, a adapter HTTP powinien mapować je na własne DTO transportowe.
5. Przenieść szczegóły endpointów, metod HTTP i URL-i z domeny do adapterów infrastrukturalnych albo do konfiguracji adaptera.
6. Utrzymywać composition root na brzegu systemu. `LazyApplicationServices` może być wygodnym, lekkim composition rootem biblioteki, ale pełna aplikacja powinna konfigurować konkretne adaptery infrastrukturalne poza domeną i aplikacją.
7. Zachować regułę zależności: domena nie importuje aplikacji ani infrastruktury, aplikacja importuje domenę i porty abstrakcyjne, infrastruktura implementuje porty zdefiniowane wewnątrz.

## Dlaczego DI przez konstruktor jest dobrym kierunkiem

DI przez konstruktor sprawia, że zależności są jawne, obiekt nie może istnieć bez wymaganych portów, testy mogą przekazywać fałszywe implementacje, a implementacje usług nie muszą znać żadnego kontenera. Leniwy composition root uzupełnia ten model o opóźnienie kosztu inicjalizacji do momentu faktycznego użycia danej usługi lub portu.
