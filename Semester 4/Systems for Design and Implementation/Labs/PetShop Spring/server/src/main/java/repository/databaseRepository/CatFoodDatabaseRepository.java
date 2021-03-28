package repository.databaseRepository;

import common.domain.CatFood;
import common.domain.Pair;
import common.exceptions.PetShopException;
import common.exceptions.ValidatorException;
import common.domain.validators.Validator;
import repository.IRepository;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class CatFoodDatabaseRepository implements IRepository<Pair<Long, Long>, CatFood> {
    private final String url;
    private final String user;
    private final String password;
    private final Validator<CatFood> validator;

    public CatFoodDatabaseRepository(Validator<CatFood> validator, String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.validator = validator;

        String sqlCreateTableQuery =  """
                CREATE TABLE IF NOT EXISTS CatFoods (
                CatId int,
                FoodId int,
                PRIMARY Key(CatId, FoodId)
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
     * @param longLongPair must be not null.
     * @return an {@code Optional} encapsulating the entity with the given id.
     * @throws IllegalArgumentException if the given id is null.
     */
    @Override
    public Optional<CatFood> findOne(Pair<Long, Long> longLongPair) {
        AtomicReference<Optional<CatFood>> catFoodToReturn = new AtomicReference<>();
        catFoodToReturn.set(Optional.empty());
        Stream.ofNullable(longLongPair)
                .findAny()
                .ifPresentOrElse(
                        (val) -> {
                            String sqlCommand = "SELECT * FROM CatFoods WHERE CatId = " + val.getLeft().toString() + " AND " +
                                    "FoodId = " + val.getRight().toString();
                            try (var connection = DriverManager.getConnection(url, user, password);
                                 var preparedStatement = connection.prepareStatement(sqlCommand);
                                 var rs = preparedStatement.executeQuery()) {
                                if (rs.next()) {
                                    Long catId = rs.getLong("CatId");
                                    Long foodId = rs.getLong("FoodId");
                                    CatFood catFood = new CatFood(catId, foodId);
                                    catFoodToReturn.set(Optional.of(catFood));
                                }

                            } catch (SQLException throwables) {
                                throw new PetShopException("SQL Exception: " + throwables.getMessage());
                            }
                        },
                        () -> {
                            throw new IllegalArgumentException("ID must not be null");
                        }
                );
        return catFoodToReturn.get();
    }

    /**
     * @return all entities.
     */
    @Override
    public Iterable<CatFood> findAll() {
        Set<CatFood> catFoodList = new HashSet<>();
        String sqlCommand = "SELECT * FROM CatFoods";
        try (var connection = DriverManager.getConnection(url, user, password);
             var preparedStatement = connection.prepareStatement(sqlCommand);
             var rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                Long catId = rs.getLong("CatId");
                Long foodId = rs.getLong("FoodId");
                CatFood catFood = new CatFood(catId, foodId);
                catFoodList.add(catFood);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return catFoodList;
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
    public Optional<CatFood> save(CatFood entity) throws ValidatorException {
        validator.validate(entity);
        String sqlCommand = "INSERT INTO CatFoods (CatId, FoodId) VALUES (?, ?)";
        try (var connection = DriverManager.getConnection(url, user, password);
             var preparedStatement = connection.prepareStatement(sqlCommand)) {
            preparedStatement.setLong(1, entity.getCatId());
            preparedStatement.setLong(2, entity.getFoodId());
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
     * @param longLongPair must not be null.
     * @return an {@code Optional} - null if there is no entity with the given id, otherwise the removed entity.
     * @throws IllegalArgumentException if the given id is null.
     */
    @Override
    public Optional<CatFood> delete(Pair<Long, Long> longLongPair) {
        Optional<CatFood> entity = findOne(longLongPair);
        AtomicReference<Optional<CatFood>> returnedEntity = new AtomicReference<>();
        entity.ifPresentOrElse((catFood) -> {
                    String sql = "DELETE FROM CatFoods WHERE CatId = ? AND FoodId = ?";
                    try (var connection = DriverManager.getConnection(url, user, password);
                         var preparedStatement = connection.prepareStatement(sql)) {
                        preparedStatement.setLong(1, catFood.getCatId());
                        preparedStatement.setLong(2, catFood.getFoodId());
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
    public Optional<CatFood> update(CatFood entity) throws ValidatorException {
        validator.validate(entity);
        String sqlCommand = "UPDATE CatFoods SET FoodId = ? WHERE CatId = ?";
        try (var connection = DriverManager.getConnection(url, user, password);
             var preparedStatement = connection.prepareStatement(sqlCommand)) {
            preparedStatement.setLong(1, entity.getFoodId());
            preparedStatement.setLong(2, entity.getCatId());
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
