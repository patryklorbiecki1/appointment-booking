# Appointment Booking System – Backend

This project is a backend application built with Java 17 and Spring Boot, allowing users to book appointments with specialists, manage schedules, and add notes to appointments.

## Features

* User registration and login
* Create, edit, and mark appointments as `RESERVED` or `COMPLETED`
* Retrieve upcoming appointments for specialists
* Add notes to appointments
* Database integration (Spring Data JPA + Hibernate)
* Handling appointment statuses and basic validation
* DTOs and mappers for data transfer

## Technologies

* Java 17
* Spring Boot
* Gradle

## Project Structure

```
src/main/java
└── com.example.appointment_booking
    ├── controller      # REST API controllers
    ├── domain
    │   ├── model      # JPA entities
    │   ├── repository # JPA repositories
    │   └── dto        # DTOs and mappers
    └── service        # Business logic
```

## Example Endpoints

### Mark Appointment as Completed

```
POST /appointments/{id}/complete
Response: 200 OK
{
  "id": 1,
  "status": "COMPLETED",
  "specialistId": 2,
  "userId": 3,
  "startDateTime": "2025-08-21T14:00:00"
}
```

### Get Upcoming Appointments for Specialist

```
GET /appointments/upcoming/{specialistId}
Response: 200 OK
[
  {
    "id": 1,
    "status": "RESERVED",
    "specialistId": 2,
    "userId": 3,
    "startDateTime": "2025-08-21T14:00:00"
  },
  {
    "id": 2,
    "status": "RESERVED",
    "specialistId": 2,
    "userId": 4,
    "startDateTime": "2025-08-22T09:00:00"
  }
]
```

### Add Notes to Appointment

```
POST /appointments/{id}/notes
Request:
{
  "note": "Bring all previous medical reports"
}
Response: 200 OK
{
  "id": 1,
  "notes": ["Bring all previous medical reports"]
}
```

## Installation

1. Clone the repository:

```bash
git clone https://github.com/patryklorbiecki1/appointment-booking.git
```

2. Run the project in your IDE (e.g., IntelliJ) or using:

```bash
./mvnw spring-boot:run
```

3. Configure your database connection in `application.properties`.

## Usage

* Send requests to the API via Postman or Swagger (if added)
* Backend supports the full appointment lifecycle – from reservation to completion and notes

## Future Plans

* Frontend in React or Angular
* Expand specialist dashboard with appointment statistics
* Support appointment cancellation and confirmation
