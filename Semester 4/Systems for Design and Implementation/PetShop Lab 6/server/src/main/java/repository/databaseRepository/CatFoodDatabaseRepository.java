package repository.databaseRepository;

import common.domain.Cat;
import common.domain.CatFood;
import common.domain.Food;
import common.domain.Pair;
import common.exceptions.PetShopException;
import common.exceptions.ValidatorException;
import common.domain.validators.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
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
    @Override
    public Optional<CatFood> findOne(Pair<Long, Long> longLongPair) {
        return jdbcOperations.query("select * from catfoods where catid=" + longLongPair.getLeft() +
                "&& foodid=" + longLongPair.getRight(), (rs, i)->
                new CatFood(
                        rs.getLong("CatId"),
                        rs.getLong("FoodId")))
                .stream().findFirst();
    }

    /*
    "SELECT * FROM CatFoods WHERE CatId = " + val.getLeft().toString() + " AND " +
                                    "FoodId = " + val.getRight().toString()
     */
    /**
     * @return all entities.
     */
    @Override
    public Iterable<CatFood> findAll() {
        return new HashSet<>(jdbcOperations.query("select * from catfoods", (rs, i) ->
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
    @Override
    public Optional<CatFood> save(CatFood entity) throws ValidatorException {
        validator.validate(entity);
        Integer rowsAffected = jdbcOperations.update("insert into catfoods (catid, foodid) VALUES (?, ?)",
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
    @Override
    public Optional<CatFood> delete(Pair<Long, Long> longLongPair) {
        Optional<CatFood> toBeRemoved = findOne(longLongPair);
        toBeRemoved.ifPresent((catFood)->jdbcOperations.update("DELETE FROM catfoods WHERE catid = ? && foodid = ?",
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
    @Override
    public Optional<CatFood> update(CatFood entity) throws ValidatorException {
        validator.validate(entity);
        Integer rowsAffected = jdbcOperations.update(
                "UPDATE catfoods SET foodid = ?WHERE catid = ?",
                entity.getFoodId(),
                entity.getCatId()
        );
        if(rowsAffected.equals(0))
            return Optional.empty();
        return Optional.of(entity);
    }
}
