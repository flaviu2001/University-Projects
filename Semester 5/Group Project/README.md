# Arcane programming

## Implementation documentation

## The what

Our application serves as a video game platform which enables you to purchase, review and play video games
while also doubling as a (semi) social network to talk to your friends, typically about games.

Below is a list of functionalities we have implemented:

- Secure login/register (complete with email confirmation)
- A store page for video games with a search functionality as well
- A user settings page to change details (such as name, email, password and so on)
- A user profile page that displays their information, owned games and published reviews
- Adding reviews to video games
- Specialized page with further information per game
- Purchase game
- Friend management (add/accept/refuse invitations)
- Messaging between friends
- Wish list for video games
- User search by their name

## The who

- Craciun Ioan-Flaviu as _Scrum master & Full stack developer_
- Raduta Lavinia-Elena as Frontend _developer_
- Daniel Suciu as _Backend developer_
- Andrei Zavo as _Frontend developer_
- Diana Tibre, Razvan Tirdea, Alex Sustic as _miscellaneous help_

## The how

The application consists of a server written in Java Spring Boot and a web application written in Angular
Material Typescript.

**_Server_**

- controller package: this package contains all Java classes that expose REST API calls by mapping URLs
    to Java methods
    ◦ AccountController: this classs exposes methods related to user actions such as login, register, verify
       email, change user data, get user by username, get list of friends for user, get list of friend invitations
       for user, add friend, accept invitation, reject invitation and search users.
    ◦ GameController: this class exposes methods related to games such as add/update/delete game,
       search game by name, get game by title, get wish list, get games from user, get game by id and get
       all games.
    ◦ MessageController: this class exposes methods related to messaging such as get conversation and
       send message.
    ◦ PurchaseController: this class exposes methods related to purchasing video games such as
       add/update/delete purchase, get purchase by id and getAllPurchases.
    ◦ ReviewController: this class exposes methods related to reviews such as add review (which doubles
       as update), delete review, get by id/title/username and get all reviews.
    ◦ WishController: this class exposes methods related to wish lists, such as get wishes, add and delete.


- converter package: this package contains converters between DTOs and Model objects. This is relevant
    because we do not use direct model classes as DTOs and we must convert to and from when exposing
    the communication to the internet. The classes and their implementation is straight forward and needs
    not mentioned.
- dto package: this package contains the dto for each of the class in the application model, its classes are
    fairly straight forward and contain the same fields as the model classes.
- filters package: CORS
- model package: this package contains the classes belonging to the model of the application. Its classes
    are simple and only contain fields, no relevant methods.
- repository package: this package contains the JPA repositories that store the objects, there exist a few
    custom queries made for this application.
- service package: this package implements the code logic called in the controllers. It is mainly an
    intermediary between the controller and repository but there is some custom logic applied to a few
    methods.
Very important for the server is the src/main/resources/application.properties file which is ignored by git, but
points to the postgres database. During our implementation we used local databases.

```
spring.datasource.url=jdbc:postgresql://localhost:5432/arcane
spring.datasource.username=postgres
spring.datasource.password=
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform = org.hibernate.dialect.PostgreSQLDialect
spring.jpa.generate-ddl=true
spring.jpa.hibernate.ddl-auto = update
spring.jpa.hibernate.naming.physical-
strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
```

This is what it ought to look like and you may modify it to suit your needs.

**_Client_**
The client is an angular application, thus it is a collection of components which in our case it is mostly pages. I
shall note the relevant aspects to understanding the architecture and design but I will not go into detail for every
individual page, as it is self explanatory by following the HTML and TS code.

- Routing: in app-routing.module.ts we define the routes that map URLs to the pages.
- In src/app/common we have models and services. Models contains the DTOs through which the server
    communicates and services contains the calls to the backend.
- Don’t forget when adding services to add them to providers in app.module.ts.
- Every page contains an <app-menu> tag that represents the navigation toolbar, this also should be
    present on every page.
- Angular material tags are used and bootstrap css classes are also used.

 
