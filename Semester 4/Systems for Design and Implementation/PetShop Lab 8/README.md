# Lab 8: REST services
- continue to work in teams on the generated repository from the previous lab
- convert your previous project by replacing Spring Remoting with Spring RestTemplate
- use Spring --- xml config forbidden
- use Spring Data JPA (Hibernate) --- xml config forbidden
- Spring Boot is (for now) forbidden
- log messages using SLF4J
- project structure:
  1. A core module containing services, repositories, model classes
  2. A web module containing controllers exposed as RESTful Web Services
  3. A client module containing a console-based ui that accesses the RESTful Web Services using RestTemplate.
- for maximum number of points: filter and sorting have to take place at the repository level without explicitly writing any specific code for such features (this is a self study task – see readme for documentation); so, for example, at the service level it should be possible to call a method like repository.findStudentsByName(“Ana”), which returns all students with the name “Ana”, without providing an implementation (Java code) for this method. 
