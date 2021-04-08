package repository.databaseRepository;

import common.domain.Cat;
import common.exceptions.PetShopException;
import common.exceptions.ValidatorException;
import common.domain.validators.Validator;
import repository.IRepository;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class CatDatabaseRepository implements IRepository<Long, Cat> {

    private final String url;
    private final String user;
    private final String password;
    private final Validator<Cat> validator;

    public CatDatabaseRepository(Validator<Cat> validator, String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.validator = validator;

        String sqlCreateTableQuery = """
                CREATE TABLE IF NOT EXISTS Cats (
                ID INT PRIMARY KEY,
                Name VARCHAR(50),
                Breed VARCHAR(50),
                CatYears SMALLINT
                )""";
        try (var connect = DriverManager.getConnection(url, user, password)) {
            var preparedStatement = connect.prepareStatement(sqlCreateTableQuery);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            throw new PetShopException("SQL Exception: " + exception);
        }
    }

    /**
     * Find the entity with the given {@code id}.
     *
     * @param aLong must be not null.
     * @return an {@code Optional} encapsulating the entity with the given id.
     * @throws IllegalArgumentException if the given id is null.
     */
    @Override
    public Optional<Cat> findOne(Long aLong) {
        AtomicReference<Optional<Cat>> catToReturn = new AtomicReference<>();
        catToReturn.set(Optional.empty());
        Stream.ofNullable(aLong)
                .findAny()
                .ifPresentOrElse(
                        (val) -> {
                            String sqlCommand = "SELECT * FROM Cats WHERE ID = " + val.toString();
                            try (var connection = DriverManager.getConnection(url, user, password);
                                 var preparedStatement = connection.prepareStatement(sqlCommand);
                                 var rs = preparedStatement.executeQuery()) {
                                Stream.of(rs.next()).filter((cond) -> cond).findAny().ifPresent((value) -> {
                                    try {
                                        Long id = rs.getLong("ID");
                                        String name = rs.getString("Name");
                                        String breed = rs.getString("Breed");
                                        int catYears = rs.getInt("CatYears");
                                        Cat cat = new Cat(id, name, breed, catYears);
                                        catToReturn.set(Optional.of(cat));
                                    } catch (SQLException exception) {
                                        exception.printStackTrace();
                                    }
                                });

                            } catch (SQLException throwables) {
                                throw new PetShopException("SQL Exception: " + throwables.getMessage());
                            }
                        },
                        () -> {
                            throw new IllegalArgumentException("ID must not be null");
                        }
                );
        return catToReturn.get();
    }

    /**
     * @return all entities.
     */
    @Override
    public Iterable<Cat> findAll() {
        Set<Cat> catList = new HashSet<>();
        String sqlCommand = "SELECT * FROM Cats";

        try (var connection = DriverManager.getConnection(url, user, password);
             var preparedStatement = connection.prepareStatement(sqlCommand);
             var rs = preparedStatement.executeQuery()) {

            try {
                //noinspection EndlessStream
                Stream.generate(() -> {
                    try {
                        return rs.next();
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                        return false;
                    }
                }).forEach (i -> {
                    Stream.of(i).filter((val) -> !val).findAny().ifPresent((val) -> {
                        throw new PetShopException("Exit");
                    });
                    try {
                        Long id = rs.getLong("ID");
                        String name = rs.getString("Name");
                        String breed = rs.getString("Breed");
                        int catYears = rs.getInt("CatYears");
                        Cat cat = new Cat(id, name, breed, catYears);
                        catList.add(cat);
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                    }
            });


            } catch (PetShopException ignored) {}
//            while (rs.next()) {
//                Long id = rs.getLong("ID");
//                String name = rs.getString("Name");
//                String breed = rs.getString("Breed");
//                int catYears = rs.getInt("CatYears");
//                Cat cat = new Cat(id, name, breed, catYears);
//                catList.add(cat);
//            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return catList;

    }

    /**
     * Saves the given entity.
     *
     * @param entity must not be null.
     * @return an {@code Optional} - null if the entity was saved otherwise (e.g. id already exists) returns the entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidatorException       if the entity is not valid.
     */
    @Override
    public Optional<Cat> save(Cat entity) throws ValidatorException {
        validator.validate(entity);
        String sqlCommand = "INSERT INTO Cats (ID, Name, Breed, CatYears) VALUES (?, ?, ?, ?)";
        try (var connection = DriverManager.getConnection(url, user, password);
             var preparedStatement = connection.prepareStatement(sqlCommand)) {
            preparedStatement.setLong(1, entity.getId());
            preparedStatement.setString(2, entity.getName());
            preparedStatement.setString(3, entity.getBreed());
            preparedStatement.setInt(4, entity.getCatYears());

            preparedStatement.executeUpdate();
            return Optional.empty();
        } catch (SQLIntegrityConstraintViolationException sqlIntegrityConstraintViolationException) {
            return Optional.ofNullable(entity);
        } catch (SQLException exception) {
            throw new PetShopException("SQLException: " + exception.getMessage());
        }
    }

    /**
     * Removes the entity with the given id.
     *
     * @param aLong must not be null.
     * @return an {@code Optional} - null if there is no entity with the given id, otherwise the removed entity.
     * @throws IllegalArgumentException if the given id is null.
     */
    @Override
    public Optional<Cat> delete(Long aLong) {
        Optional<Cat> entity = findOne(aLong);
        AtomicReference<Optional<Cat>> returnedEntity = new AtomicReference<>();
        System.out.println(entity.isPresent());
        entity.ifPresentOrElse((cat) -> {
                    String sql = "DELETE FROM Cats WHERE ID = ?";
                    try (var connection = DriverManager.getConnection(url, user, password);
                         var preparedStatement = connection.prepareStatement(sql)) {
                        preparedStatement.setLong(1, aLong);

                        preparedStatement.executeUpdate();
                        returnedEntity.set(entity);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                },
                () -> returnedEntity.set(Optional.empty())

        );
        return returnedEntity.get();

    }

    /**
     * Updates the given entity.
     *
     * @param entity must not be null.
     * @return an {@code Optional} - null if the entity was updated otherwise (e.g. id does not exist) returns the
     * entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidatorException       if the entity is not valid.
     */
    @Override
    public Optional<Cat> update(Cat entity) throws ValidatorException {
        validator.validate(entity);
        String sqlCommand = "UPDATE Cats SET Name = ?, Breed = ?, CatYears = ? WHERE ID = ?";
        try (var connection = DriverManager.getConnection(url, user, password);
             var preparedStatement = connection.prepareStatement(sqlCommand)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getBreed());
            preparedStatement.setInt(3, entity.getCatYears());
            preparedStatement.setLong(4, entity.getId());
            preparedStatement.executeUpdate();
            return Optional.of(entity);
        } catch (SQLIntegrityConstraintViolationException sqlIntegrityConstraintViolationException) {
            System.out.println("Integrity constraint violation");
            return Optional.empty();
        } catch (SQLException exception) {
            throw new PetShopException("SQLException: " + exception.getMessage());
        }
    }
}
