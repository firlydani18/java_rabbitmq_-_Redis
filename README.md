# User Management System

## Overview
This is a Spring Boot application designed to manage books and users. It integrates with Redis for caching and RabbitMQ for messaging. The project is structured to handle CRUD operations for books and users while incorporating caching and asynchronous messaging for improved performance and scalability.

---

## Features
- **Book Management**: Add, update, delete, and view books.
- **User Management**: Add, update, delete, and view users.
- **Redis Integration**: Provides caching to optimize performance.
- **RabbitMQ Integration**: Enables message-based communication between components.

---

## Technologies Used
- **Spring Boot**: Core framework.
- **Spring Data JPA**: For database interaction.
- **Redis**: For caching.
- **RabbitMQ**: For message queuing.
- **H2 Database**: For development and testing.
- **Maven**: Dependency management.
- **JUnit**: For unit testing.

---

## Project Structure

```
src
└── main
    ├── java
    │   └── com
    │       └── example
    │           └── demo
    │               ├── exception
    │               ├── model
    │               ├── rabbitconfiguration
    │               ├── rabbitproducer
    │               ├── repository
    │               ├── services
    │               │   └── implementation
    │               └── UserService.java
    └── resources
        └── application.properties
```

---

## How to Run
### Prerequisites
- **Java 17** or higher
- **Maven 3.6+**
- **Redis Server** running on localhost (port 6379)
- **RabbitMQ Server** running on localhost (default port 5672)

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/demo-project.git
   cd demo-project
   ```
2. Build the application:
   ```bash
   mvn clean install
   ```
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```
4. Access the application:
   - API Endpoints: `http://localhost:8080`

---

## API Endpoints
### Book Management
| Method | Endpoint            | Description            |
|--------|---------------------|------------------------|
| GET    | `/books`            | Retrieve all books     |
| POST   | `/books`            | Add a new book         |
| PUT    | `/books/{id}`       | Update an existing book|
| DELETE | `/books/{id}`       | Delete a book          |

### User Management
| Method | Endpoint            | Description            |
|--------|---------------------|------------------------|
| GET    | `/users`            | Retrieve all users     |
| POST   | `/users`            | Add a new user         |
| PUT    | `/users/{id}`       | Update an existing user|
| DELETE | `/users/{id}`       | Delete a user          |

---

## Redis Configuration
Redis is used for caching frequently accessed data like books and users.
- Configuration file: `RedisConfig.java`
- Default Host: `localhost`
- Default Port: `6379`

---

## RabbitMQ Configuration
RabbitMQ is used for sending and consuming messages asynchronously.
- Configuration file: `RabbitConfig.java`
- Default Host: `localhost`
- Default Port: `5672`

### Producer
- **File**: `NotificationProducer.java`
- **Functionality**: Sends messages to the RabbitMQ queue.

### Consumer
- **File**: `MessageConsumer.java`
- **Functionality**: Processes messages received from the RabbitMQ queue.

---

## Testing
- **Unit Tests**: Located in `src/test/java`
- Run tests with:
  ```bash
  mvn test
  ```

---


## License
This project is licensed under the MIT License. See the `LICENSE` file for details.

---

## Contributing
1. Fork the repository.
2. Create a new branch (`feature/your-feature-name`).
3. Commit your changes.
4. Push to your branch.
5. Create a pull request.




