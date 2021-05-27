package repository.databaseRepository;

import common.domain.Food;
import common.exceptions.PetShopException;
import common.exceptions.ValidatorException;
import common.domain.validators.Validator;
import repository.IRepository;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class FoodDatabaseRepository implements IRepository<Long, Food> {

    private final String url;
    private final String user;
    private final String password;
    private final Validator<Food> validator;

    public FoodDatabaseRepository(Validator<Food> validator, String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.validator = validator;

        String sqlCreateTableQuery =  """
                CREATE TABLE IF NOT EXISTS Food (
                ID INT PRIMARY KEY,
                Name VARCHAR(50),
                Producer VARCHAR(50),
                ExpirationDate Date
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
    public Optional<Food> findOne(Long aLong) {
        AtomicReference<Optional<Food>> foodToReturn = new AtomicReference<>();
        foodToReturn.set(Optional.empty());
        Stream.ofNullable(aLong)
                .findAny()
                .ifPresentOrElse(
                        (val) -> {
                            String sqlCommand = "SELECT * FROM Food WHERE ID = " + val.toString();
                            try (var connection = DriverManager.getConnection(url, user, password);
                                 var preparedStatement = connection.prepareStatement(sqlCommand);
                                 var rs = preparedStatement.executeQuery()) {
                                if (rs.next()) {
                                    Long id = rs.getLong("ID");
                                    String name = rs.getString("Name");
                                    String producer = rs.getString("Producer");
                                    Date expirationDate = rs.getDate("ExpirationDate");
                                    Food food = new Food(id, name, producer, expirationDate);
                                    foodToReturn.set(Optional.of(food));
                                }

                            } catch (SQLException throwables) {
                                throw new PetShopException("SQL Exception: " + throwables.getMessage());
                            }
                        },
                        () -> {
                            throw new IllegalArgumentException("ID must not be null");
                        }
                );
        return foodToReturn.get();
    }

    /**
     * @return all entities.
     */
    @Override
    public Iterable<Food> findAll() {
        Set<Food> foodList = new HashSet<>();
        String sqlCommand = "SELECT * FROM Food";
        try (var connection = DriverManager.getConnection(url, user, password);
             var preparedStatement = connection.prepareStatement(sqlCommand);
             var rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                Long id = rs.getLong("ID");
                String name = rs.getString("Name");
                String producer = rs.getString("Producer");
                Date expirationDate = rs.getDate("ExpirationDate");
                Food food = new Food(id, name, producer, expirationDate);
                foodList.add(food);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return foodList;
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
    public Optional<Food> save(Food entity) throws ValidatorException {
        validator.validate(entity);
        String sqlCommand = "INSERT INTO Food (ID, Name, Producer, ExpirationDate) VALUES (?, ?, ?, ?)";
        try (var connection = DriverManager.getConnection(url, user, password);
             var preparedStatement = connection.prepareStatement(sqlCommand)) {
            preparedStatement.setLong(1, entity.getId());
            preparedStatement.setString(2, entity.getName());
            preparedStatement.setString(3, entity.getProducer());
            preparedStatement.setDate(4, new java.sql.Date(entity.getExpirationDate().getTime()));

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
    public Optional<Food> delete(Long aLong) {
        Optional<Food> entity = findOne(aLong);
        AtomicReference<Optional<Food>> returnedEntity = new AtomicReference<>();
        entity.ifPresentOrElse((food) -> {
                    String sql = "DELETE FROM Food WHERE ID = ?";
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
    public Optional<Food> update(Food entity) throws ValidatorException {
        validator.validate(entity);
        String sqlCommand = "UPDATE Food SET Name = ?, Producer = ?, ExpirationDate = ? WHERE ID = ?";
        try (var connection = DriverManager.getConnection(url, user, password);
             var preparedStatement = connection.prepareStatement(sqlCommand)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getProducer());
            preparedStatement.setDate(3, new java.sql.Date(entity.getExpirationDate().getTime()));
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
