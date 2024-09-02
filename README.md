<h1 align="center">VANN: Backend</h1>

<p align="center">
  <a href="https://openjdk.org/"><img src="https://img.shields.io/badge/Java 21-ed8b00.svg?style=flat&labelColor=555&logo=OpenJDK&logoColor=white"></a>
  <a href="https://www.postgresql.org/"><img src="https://img.shields.io/badge/PostgreSQL-4169E1.svg?style=flat&labelColor=555&logo=PostgreSQL&logoColor=white"></a>
  <a href="https://www.postman.com/"><img src="https://img.shields.io/badge/Postman-FF6C37.svg?style=flat&labelColor=555&logo=Postman&logoColor=white"></a>
  <a href="https://spring.io/projects/spring-framework"><img src="https://img.shields.io/badge/Spring-6DB33F.svg?style=flat&labelColor=555&logo=Spring&logoColor=white"></a>
</p>

This is a project to develop an e-commerce site backend server with Java Spring Boot and PostgreSQL — the latter may be substituted out for another SQL database by amending the `application.properties` database settings.

It is a work in progress: it does not feature authentication functionality for example, and all features are subject to change as they are improved upon and refactored.

The corresponding frontend is under development with JavaScript and React and is located [here](https://github.com/essteer/vann-app).

## Model overview

The core layers of this backend server are organised as entities, repositories, services and controllers. Each layer is organised as a subdirectory of `src/main/java/com/vann/`.

### Entity layer

Entities represent the core concepts of the shop model. The full list is as follows:

- `Cart`, `Category`, `Customer`, `Invoice`, `InvoiceItem`, `Product`.

Several of these are currently in a simplified form and will be expanded upon in subsequent versions. Instances of each of the entities are allocated UUIDs before they become useable.

The `Customer` in its current form has only name and email attributes, with the sole constraint that the email must be unique to that customer.

The `Category` entity is constrained by the `CategoryType` enum under `com/vann/model/enums/CategoryType.java`. Each `Product` has optional `Colour` and `Size` enum attributes, and a mandatory `Category`.

Instances of the `Cart` entity have a one-to-one relationship with a `Customer` instance. A `Cart` holds `Product` instances, and upon checkout an `Invoice` is created that consists of `InvoiceItems` — these are essentially snapshots of the state of their corresponding `Product` instance at the time of checkout, with additional details such as quantity.

### Repository layer

Each entity class has a corresponding repository interface. 

- `CartRepo`, `CategoryRepo`, `CustomerRepo`, `InvoiceRepo`, `InvoiceItemRepo`, `ProductRepo`

The repositories hold instances of their respective entities, and provide a means of searching and retrieving them from the database.

The repositories are largely implemented by the Spring Data JPA framework, though some such as `InvoiceRepo` contain custom methods queries in addition to the defaults.

### Service layer

The service layer constitutes the core business logic of the application. Each entity has a corresponding service:

- `CartService`, `CategoryService`, `CustomerService`, `InvoiceService`, `InvoiceItemService`, `ProductService`

The service classes:

- perform CRUD (create, read, update, delete) operations on entity classes, 
- interact with repositories to retrieve entity instances or save updates to them, and
- receive requests from and return response to the controller layer. 

### Controller layer

The controller classes are API endpoints for external interaction with this application.

- `CartController`, `CategoryController`, `CustomerController`, `InvoiceController`, `ProductController`

There is no `InvoiceItemController` since invoice items are derived from cart and product information when an invoice is created — direct manipulation is not desired.

In general, the controller classes facilitate CRUD operations via API calls. However, endpoints are not provided for all of these methods for all controllers, and of those that are provided, several are for internal use only and are not exposed for outside interaction.

The controllers with the greatest exposure are those that facilitate a typical online shopping experience: getting product information, adding or removing products to carts, and converting carts into invoices upon checkout.

Other endpoints exist for purposes such as category creation and invoice deletion, but these are intended for the sole use of the database owner.

## Setup and installation

This program was developed with Java 21.0.3, Spring Boot version 3.3.3, and PostgreSQL 16.3.

### Codebase

<a href="https://github.com/essteer/vann"><img src="https://img.shields.io/badge/GitHub-181717.svg?style=flat&labelColor=555&logo=GitHub&logoColor=white"></a>

To clone this repo, open the terminal on your machine and run the following command from within the desired workspace:

```console
$ git clone git@github.com:essteer/vann
```

The required dependencies are included in `pom.xml`. Modern IDEs with Java and Spring support should build from this automatically. 

### SQL database

<a href="https://www.postgresql.org/"><img src="https://img.shields.io/badge/PostgreSQL-4169E1.svg?style=flat&labelColor=555&logo=PostgreSQL&logoColor=white"></a>

This backend server is built on a [PostgreSQL](https://www.postgresql.org/) database with configuration in `application.properties` as follows:

```properties
### POSTGRESQL DATABASE ###
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.url="jdbc:postgresql://localhost:5432/${DATABASE_NAME}"
spring.datasource.username=${POSTGRESQL_USERNAME}
spring.datasource.password=${POSTGRESQL_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

The PostgreSQL username and password indicated above should be for a user with write permissions to the database indicated as `DATABASE_NAME` at the end of the `datasource.url` line. First log into Postgres and create the database: 

```ssl
postgres=> CREATE DATABASE database_name;
CREATE DATABASE
```

The `CREATE DATABASE` response indicates that the database was successfully created, but you can enter `\l` to see a list of databases. To delete the database, run `DROP DATABASE database_name;`.

From then on database interactions can be handled entirely via Spring.

If preferred, the database can be run MySQL or H2 instead. Suggested settings indicated below — I haven't tested these myself, but these are what the working Postgres config is based on.

#### MySQL

To use MySQL replace the Postgres content in `application.properties` and `pom.xml` as indicated below.

In `application.properties`:

 ```properties
 ### MYSQL DATABASE ###
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/${DATABASE_NAME}
spring.datasource.username=${MYSQL_USERNAME}
spring.datasource.password=${MYSQL_PASSWORD}
spring.jpa.database=mysql
spring.jpa.hibernate.ddl-auto=create-drop
```

In `pom.xml`:

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

#### H2 database

In `application.properties`:

```properties
### H2 DATABASE ###
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.url=jdbc:h2:file:./h2/rest;AUTO_SERVER=true
spring.datasource.username=${H2_USERNAME}
spring.datasource.password=${H2_PASSWORD}
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
spring.h2.console.path=/h2
spring.jpa.hibernate.ddl-auto=update
```

In `pom.xml`:

```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

## Operation

<a href="https://spring.io/projects/spring-framework"><img src="https://img.shields.io/badge/Spring-6DB33F.svg?style=flat&labelColor=555&logo=Spring&logoColor=white"></a>

The entrypoint of the application is `VannApplication.java`, located at `src/main/java/com/vann/VannApplication.java`. Additional runtime logic is contained in `DataLoader.java` at the same directory level.

Run `VannApplication.java` to launch the program: to run using an IDE, open that file and click run. To run from the terminal, navigate to the project root directory and enter this command:

```console
$ mvn spring-boot: run
```

The `application.properties` file (in `src/main/resources/application.properties`) is configured to run the application on port 9001. Amend the value of the line `server.port=9001` to use a different port — but be sure to also change this in the frontend config as well if using that along with this.

### Endpoints

Once the backend is running interact with the server via the terminal, the frontend, or a platform such as Postman. The available endpoints can be determined from the `Controller` files located at `src/main/java/com/vann/controller/`.

On a Linux terminal, for example, a curl request to get a list of all products would work as follows:

```console
$ curl -H 'Content-Type: application/json' -X GET 'http://localhost:9001/api/v1/products'
```

If the database contains more than a tiny amount of data, that request will likely return a wall of JSON that is difficult to read.

If your machine has the `jq` tool or Python installed you can pipe the JSON response to those for readable output:

```console
$ curl -H 'Content-Type: application/json' -X GET 'http://localhost:9001/api/v1/products' | jq
$ curl -H 'Content-Type: application/json' -X GET 'http://localhost:9001/api/v1/products' | python3 -m json.tool
```

### Frontend

The frontend repository is operational under development alongside this backend server, and is located [here](https://github.com/essteer/vann-app).

## Tests

<a href="https://junit.org/junit5/"><img src="https://img.shields.io/badge/JUnit5-25A162.svg?style=flat&labelColor=555&logo=JUnit5&logoColor=white"></a>
<a href="https://www.postman.com/"><img src="https://img.shields.io/badge/Postman-FF6C37.svg?style=flat&labelColor=555&logo=Postman&logoColor=white"></a>

A test suite implemented via [Junit](https://junit.org/junit5/) and [Mockito](https://site.mockito.org/
) is included under `src/test` and contains tests for each of the `Entity` and `Controller` models. The `Service` classes operate between these two layers and so should be reliably covered by those tests, but dedicated tests will be introduced soon.

The Junit and Mockito libraries come included with the `spring-boot-starter-test` dependency, so are not referenced separately in the `pom.xml`.

A collection of API tests for use with Postman is included in a JSON file under `src/test/postman`.

## Logs

<a href="https://logging.apache.org/log4j/2.x/"><img src="https://img.shields.io/badge/Apache-D22128.svg?style=flat&labelColor=555&logo=Apache&logoColor=white"></a>

This repo makes use of Apache log4j version 2 for server logs. The configuration file is under `src/main/resources/log4j2-spring.xml`.

In the program, logs are handled centrally via the static `LogHandler` class located at `src/main/java/com/vann/utils/LogHandler.java`. Other classes make calls to methods of the `LogHandler` class and pass arguments as relevant.