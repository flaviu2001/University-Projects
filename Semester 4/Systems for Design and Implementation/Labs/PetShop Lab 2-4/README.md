# General requirements

 - design the solution for your problem using a CASE tool (use cases, class diagram, sequence diagram for each use case)
 - use feature driven development
 - layered architecture
 - data validation
 - all functions will be documented and tested

-   use Java 8 features (lambda expressions, streams etc); the program should be written without if statements and loops
-   persistence: ‘in memory’, text files, xml, db (jdbc); you may use any RDBMS, but we only offer support for PostgreSQL; MS SQL Server is forbidden

# Details:
The first example for lab2-4 is: *catalog1_I1_inmemory_infile.zip*

**You will have to use the repository interface from the example (no changes allowed there).** Everything else is up to you (considering the _grading scheme_).

The following  **iteration plan**  contains the  _minimal_  features such that the  _starting_  grade (points) for each iteration is 10:

**I1**  (deadline week 2):

- two features (e.g: addStudent and printAllStudents)

- java doc in html format (see the example from the group - project_root/doc/index.html - repository interface)

- only inmemory repository is enough

!!! focus on working with git - working on only one branch (master/develop) is enough

→ the project  _should_ be of type gradle

**I2**  (deadline week 3):

- three features

!!! focus on working with git - feature branches must be used

→ the project  _must_ be of type gradle

**I3**  (deadline week 4):

- all features

**Grading scheme**

Architecture and coding style: 5.5p

Functionality: 4.5p

------

Functionality (4.5p):

- CRUD operations: 3p

- filter: 1p

- report: 0.5p

------

Architecture and coding style (5.5p):

- layered architecture; proper operation/responsibility separation between layers; single responsibility principle etc

- using Java 8 language features - where applicable; note that there are some redundant ifs and loops in my example (actually there are some other issues as well); the application should be written without any ifs and loops (using any if or loop will have to be justified for each occurrence)

- guard against null - where applicable

- data validation

- custom exceptions

- proper exception handling: exceptions should be wrapped in custom exception classes, thrown further, caught in the ui, where a message is presented to the user; the application should never crash, it should always resume; exceptions may also be only logged (for now ex.printStanckTrace() would do), but this decision will have to be justified for each occurrence; having more than one exception in a method signature will have to be justified for each occurrence

- using try-with-resource where applicable

- using NIO.2 where applicable

- using custom generic classes

- the application must be structured as in the example: src/main/java, src/test/java - mixing the tests with the actual code is forbidden

- using java doc comments for the important parts of the application; generate java doc (intellij idea: tools -> generate java doc).

-> there will be a 0.5p penalty of each type of mistake (only applicable starting from I2 (week 3)).

--------

Only in week 4 (I3 - the last iteration):

- if the file persistence is missing: 1p penalty

- if the xml persistence is missing: 2p penalty

- if the db persistence is missing: 3p penalty.

**Week 2**:

You will probably encounter difficulties in working with git. If that is the case, don't worry it's normal at first; soon enough, it will be ok.

Next week, I would like to focus on these issues at the lab and on any possible difficulties that you foresee in meeting the requirements for lab2-4.

Please note that we will approach the xml and db topics at the lecture and detailed examples on the group regarding these topics will follow.


 
