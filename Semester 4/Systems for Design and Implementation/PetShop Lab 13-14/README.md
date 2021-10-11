# Lab 13-14: Microservices
- The deadline for this lab is week 14; for week 13, there is no deadline, for any lab
- Continue to work individually on the previous project (same repo)
- Refactor the previous project from a monolithic architecture to microservices

- At least two services should be implemented in Ruby, the other ones in Java, as seen in the diagram (see readme file for suggested documentation); I have been recently informed that there are some issues in Windows with configuring Rails and Postgres so maybe a Ruby version prior to 3 should be used
- The services must normally be completely independent (e.g. source control repository, application server, database etc completely independent); for the lab: in your repo, there will be one folder for the entire app and subfolders for each service, i.e., a modular solution (as it was the case until now) should not be attempted; consequently, the current separation in core and web is no longer needed; it is also allowed to have a single db, but each service should interact only with the tables from that business domain (as seen in the diagram)
- The existing Angular front-end should access the backend via a single entry point (API Gateway) as seen in the diagram; the gateway will forward the requests to the appropriate service/services; it may be implemented either in Java or Ruby (as any other service); the front-end will remain entirely in Angular i.e. Ruby/Rails should only be used in the backend
- Each service/component from the diagram (including the front-end) will be managed by Docker (i.e. will have its environment defined in its own Dockerfile) and all services/containers will run with Docker Compose; so, practically, with the `docker-compose up` command the entire application will be started 
