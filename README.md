# Car Rental Technical Exercise

A small, object-oriented car rental simulation focused on domain modeling and testability.

## Implemented requirements

- Reserve a car by type, desired start date/time, and number of days
- Supported car types: `SEDAN`, `SUV`, `VAN`
- Limited fleet capacity per car type
- Unit tests proving reservation behavior and edge cases
- REST API for creating reservations

## Design highlights

- `CarRentalService` contains reservation orchestration logic
- `FleetInventory` encapsulates available capacity per car type
- `Reservation` models a booked time window with overlap rules
- `ReservationRepository` abstraction keeps storage concerns separate
- `InMemoryReservationRepository` provides a simple simulation-ready implementation

The overlap model uses a half-open interval: `[start, end)`. This allows back-to-back reservations at the exact end/start boundary.

## REST API

- Endpoint: `POST /api/reservations`
- Request payload:

```json
{
  "carType": "SUV",
  "startDateTime": "2026-11-25T10:00:00",
  "numberOfDays": 2
}
```

- Success response: `201 Created` with reservation data
- No availability response: `409 Conflict`
- Invalid payload response: `400 Bad Request`

- Endpoint: `GET /api/reservations/{id}`
- Success response: `200 OK` with reservation data
- Not found response: `404 Not Found`

- Endpoint: `GET /api/availability?carType=SUV&startDateTime=2026-11-25T10:00:00&numberOfDays=2`
- Success response: `200 OK` with `capacity`, `reserved`, and `available`
- Invalid query response: `400 Bad Request`

## Run application

```bash
./gradlew bootRun
```

## Try the API

```bash
curl -i -X POST http://localhost:8080/api/reservations \
  -H 'Content-Type: application/json' \
  -d '{"carType":"SUV","startDateTime":"2026-11-25T10:00:00","numberOfDays":2}'

curl -i http://localhost:8080/api/reservations/11111111-1111-1111-1111-111111111111

curl -i "http://localhost:8080/api/availability?carType=SUV&startDateTime=2026-11-25T10:00:00&numberOfDays=2"
```

## Run tests

```bash
./gradlew test
```

## Key classes

- `src/main/java/com/demo/carrental/domain/CarRentalService.java`
- `src/main/java/com/demo/carrental/domain/FleetInventory.java`
- `src/main/java/com/demo/carrental/domain/Reservation.java`
- `src/main/java/com/demo/carrental/domain/InMemoryReservationRepository.java`
- `src/main/java/com/demo/carrental/api/ReservationController.java`
- `src/test/java/com/demo/carrental/domain/CarRentalServiceTest.java`

