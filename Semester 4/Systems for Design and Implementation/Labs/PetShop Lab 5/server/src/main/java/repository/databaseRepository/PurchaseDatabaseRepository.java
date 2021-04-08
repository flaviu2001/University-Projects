package repository.databaseRepository;

import common.domain.Pair;
import common.domain.Purchase;
import common.exceptions.PetShopException;
import common.exceptions.ValidatorException;
import common.domain.validators.Validator;
import repository.IRepository;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class PurchaseDatabaseRepository implements IRepository<Pair<Long, Long>, Purchase>{
    private final String url;
    private final String user;
    private final String password;
    private final Validator<Purchase> validator;

    public PurchaseDatabaseRepository(Validator<Purchase> validator, String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.validator = validator;

        String sqlCreateTableQuery =  """
                CREATE TABLE IF NOT EXISTS Purchases (
                CatId int,
                CustomerId int,
                Price int,
                Review int,
                DateAcquired Date,
                PRIMARY Key(CatId, CustomerId)
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
    public Optional<Purchase> findOne(Pair<Long, Long> longLongPair) {
        AtomicReference<Optional<Purchase>> purchaseToReturn = new AtomicReference<>();
        Stream.ofNullable(longLongPair)
                .findAny()
                .ifPresentOrElse(
                        (val) -> {
                            String sqlCommand = "SELECT * FROM Purchases WHERE CatId = " + val.getLeft().toString() + " AND " +
                                    "CustomerId = " + val.getRight().toString();
                            try (var connection = DriverManager.getConnection(url, user, password);
                                 var preparedStatement = connection.prepareStatement(sqlCommand);
                                 var rs = preparedStatement.executeQuery()) {
                                if (rs.next()) {
                                    Purchase purchase = getPurchaseFromResultSet(rs);
                                    purchaseToReturn.set(Optional.of(purchase));
                                }

                            } catch (SQLException throwables) {
                                throw new PetShopException("SQL Exception: " + throwables.getMessage());
                            }
                        },
                        () -> {
                            throw new IllegalArgumentException("ID must not be null");
                        }
                );
        return purchaseToReturn.get();
    }

    /**
     * @return all entities.
     */
    @Override
    public Iterable<Purchase> findAll() {
        Set<Purchase> purchaseList = new HashSet<>();
        String sqlCommand = "SELECT * FROM Purchases";
        try (var connection = DriverManager.getConnection(url, user, password);
             var preparedStatement = connection.prepareStatement(sqlCommand);
             var rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                Purchase purchase = getPurchaseFromResultSet(rs);
                purchaseList.add(purchase);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return purchaseList;
    }

    private Purchase getPurchaseFromResultSet(ResultSet rs) throws SQLException {
        Long catId = rs.getLong("CatId");
        Long customerId = rs.getLong("CustomerId");
        int review = rs.getInt("Review");
        int price = rs.getInt("Price");
        Date acquiredDate = rs.getDate("DateAcquired");
        return new Purchase(catId, customerId, price, acquiredDate, review);
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
    public Optional<Purchase> save(Purchase entity) throws ValidatorException {
        validator.validate(entity);
        String sqlCommand = "INSERT INTO Purchases (CatId, CustomerId, Price, DateAcquired, Review) VALUES (?, ?, ?, ?, ?)";
        try (var connection = DriverManager.getConnection(url, user, password);
             var preparedStatement = connection.prepareStatement(sqlCommand)) {
            preparedStatement.setLong(1, entity.getCatId());
            preparedStatement.setLong(2, entity.getCustomerId());
            preparedStatement.setInt(3, entity.getPrice());
            preparedStatement.setDate(4, new java.sql.Date(entity.getDateAcquired().getTime()));
            preparedStatement.setInt(5, entity.getReview());
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
    public Optional<Purchase> delete(Pair<Long, Long> longLongPair) {
        Optional<Purchase> entity = findOne(longLongPair);
        AtomicReference<Optional<Purchase>> returnedEntity = new AtomicReference<>();
        entity.ifPresentOrElse((catFood) -> {
                    String sql = "DELETE FROM Purchases WHERE CatId = ? AND Customerid = ?";
                    try (var connection = DriverManager.getConnection(url, user, password);
                         var preparedStatement = connection.prepareStatement(sql)) {
                        preparedStatement.setLong(1, catFood.getCatId());
                        preparedStatement.setLong(2, catFood.getCustomerId());
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
    public Optional<Purchase> update(Purchase entity) throws ValidatorException {
        validator.validate(entity);
        String sqlCommand = "UPDATE Purchases SET " +
                "Price = ?," +
                "DateAcquired = ?," +
                "Review = ?" +
                "WHERE CatId = ? AND CustomerId = ?";
        try (var connection = DriverManager.getConnection(url, user, password);
             var preparedStatement = connection.prepareStatement(sqlCommand)) {
            preparedStatement.setInt(1, entity.getPrice());
            preparedStatement.setDate(2, new java.sql.Date(entity.getDateAcquired().getTime()));
            preparedStatement.setInt(3, entity.getReview());
            preparedStatement.setLong(4, entity.getCatId());
            preparedStatement.setLong(5, entity.getCustomerId());
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
