package repository.databaseRepository;

import common.domain.CatFood;
import common.domain.Pair;
import common.domain.validators.Validator;
import common.exceptions.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.HashSet;
import java.util.Optional;

public class CatFoodDatabaseRepository {
    @Autowired
    JdbcOperations jdbcOperations;

    private final Validator<CatFood> validator;

    public CatFoodDatabaseRepository(Validator<CatFood> validator) {
        this.validator = validator;
    }

    /**
     * Find the entity with the given {@code id}.
     *
     * @param longLongPair must be not null.
     * @return an {@code Optional} encapsulating the entity with the given id.
     * @throws IllegalArgumentException if the given id is null.
     */

    public Optional<CatFood> findOne(Pair<Long, Long> longLongPair) {
        return jdbcOperations.query("select * from catfood where id=" + longLongPair.getLeft() +
                "&& foodid=" + longLongPair.getRight(), (rs, i)->
                new CatFood(
                        rs.getLong("CatId"),
                        rs.getLong("FoodId")))
                .stream().findFirst();
    }

    /**
     * @return all entities.
     */

    public Iterable<CatFood> findAllEntities() {
        return new HashSet<>(jdbcOperations.query("select * from catfood", (rs, i) ->
                new CatFood(
                        rs.getLong("CatId"),
                        rs.getLong("FoodId"))
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

    public Optional<CatFood> saveEntity(CatFood entity) throws ValidatorException {
        validator.validate(entity);
        Integer rowsAffected = jdbcOperations.update("insert into catfood (catid, foodid) VALUES (?, ?)",
                entity.getCatId(),
                entity.getFoodId());
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

    public Optional<CatFood> deleteEntity(Pair<Long, Long> longLongPair) {
        Optional<CatFood> toBeRemoved = findOne(longLongPair);
        toBeRemoved.ifPresent((catFood)->jdbcOperations.update("DELETE FROM catfood WHERE catid = ? && foodid = ?",
                catFood.getCatId(), catFood.getFoodId()));
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

    public Optional<CatFood> update(CatFood entity) throws ValidatorException {
        validator.validate(entity);
        Integer rowsAffected = jdbcOperations.update(
                "UPDATE catfood SET foodid = ?WHERE catid = ?",
                entity.getFoodId(),
                entity.getCatId()
        );
        if(rowsAffected.equals(0))
            return Optional.empty();
        return Optional.of(entity);
    }
}
