package repository.databaseRepository;

import common.domain.Pair;
import common.domain.Purchase;
import common.domain.validators.Validator;
import common.exceptions.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.HashSet;
import java.util.Optional;

public class PurchaseDatabaseRepository{
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

    public Optional<Purchase> findOne(Pair<Long, Long> longLongPair) {
        return jdbcOperations.query("select * from purchase where catid=" + longLongPair.getLeft() +
                "&& purchase.customerid=" + longLongPair.getRight(), (rs, i)->
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

    public Iterable<Purchase> findAllEntities() {
        return new HashSet<>(jdbcOperations.query("select * from purchase", (rs, i) ->
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

    public Optional<Purchase> saveEntity(Purchase entity) throws ValidatorException {
        validator.validate(entity);
        Integer rowsAffected = jdbcOperations.update("insert into purchase (catid, customerid, price, dateacquired, review) VALUES (?, ?, ?, ?)",
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

    public Optional<Purchase> deleteEntity(Pair<Long, Long> longLongPair) {
        Optional<Purchase> toBeRemoved = findOne(longLongPair);
        toBeRemoved.ifPresent((catFood)->jdbcOperations.update("DELETE FROM purchase WHERE catid = ? && customerid = ?",
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

    public Optional<Purchase> update(Purchase entity) throws ValidatorException {
        validator.validate(entity);

        Integer rowsAffected = jdbcOperations.update(
                "UPDATE purchase SET price = ?, dateacquired = ?, review = ?" +
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
