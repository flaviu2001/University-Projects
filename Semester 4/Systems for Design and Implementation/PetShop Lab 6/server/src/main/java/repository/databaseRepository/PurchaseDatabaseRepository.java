package repository.databaseRepository;

import common.domain.CatFood;
import common.domain.Pair;
import common.domain.Purchase;
import common.exceptions.PetShopException;
import common.exceptions.ValidatorException;
import common.domain.validators.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
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
    @Autowired
    JdbcOperations jdbcOperations;

    private final Validator<Purchase> validator;

    public PurchaseDatabaseRepository(Validator<Purchase> validator) {
        this.validator = validator;
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
        return jdbcOperations.query("select * from purchases where catid=" + longLongPair.getLeft() +
                "&& purchases.customerid=" + longLongPair.getRight(), (rs, i)->
                new Purchase(
                        rs.getLong("CatId"),
                        rs.getLong("CustomerId"),
                        rs.getInt("Review"),
                        rs.getDate("DateAcquired"),
                        rs.getInt("Price")))
                .stream().findFirst();
    }

    /**
     * @return all entities.
     */
    @Override
    public Iterable<Purchase> findAll() {
        return new HashSet<>(jdbcOperations.query("select * from purchases", (rs, i) ->
                new Purchase(
                        rs.getLong("CatId"),
                        rs.getLong("CustomerId"),
                        rs.getInt("Review"),
                        rs.getDate("DateAcquired"),
                        rs.getInt("Price"))
        ));
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
        Integer rowsAffected = jdbcOperations.update("insert into purchases (catid, customerid, price, dateacquired, review) VALUES (?, ?, ?, ?)",
                entity.getCatId(),
                entity.getCustomerId(),
                entity.getPrice(),
                entity.getDateAcquired(),
                entity.getReview()
                );
        if(rowsAffected.equals(1))
            return Optional.empty();
        return Optional.of(entity);
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
        Optional<Purchase> toBeRemoved = findOne(longLongPair);
        toBeRemoved.ifPresent((catFood)->jdbcOperations.update("DELETE FROM purchases WHERE catid = ? && customerid = ?",
                catFood.getCatId(), catFood.getCustomerId()));
        return toBeRemoved;
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

        Integer rowsAffected = jdbcOperations.update(
                "UPDATE purchases SET price = ?, dateacquired = ?, review = ?" +
                        "WHERE catid = ? && customerid = ?",
                entity.getPrice(),
                entity.getDateAcquired(),
                entity.getReview(),
                entity.getCatId(),
                entity.getCustomerId()
        );
        if(rowsAffected.equals(0))
            return Optional.empty();
        return Optional.of(entity);
    }
}
