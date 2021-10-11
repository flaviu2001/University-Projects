package repository.databaseRepository;

import common.domain.Food;
import common.domain.validators.Validator;
import common.exceptions.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import repository.IRepository;

import java.util.HashSet;
import java.util.Optional;

public class FoodDatabaseRepository implements IRepository<Long, Food> {
    @Autowired
    private JdbcOperations jdbcOperations;

    private final Validator<Food> validator;

    public FoodDatabaseRepository(Validator<Food> validator) {
        this.validator = validator;
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
        return jdbcOperations.query("select * from food where id=" + aLong, (rs, i)->
                new Food(
                        rs.getLong("ID"),
                        rs.getString("Name"),
                        rs.getString("Producer"),
                        rs.getDate("ExpirationDate")))
                .stream().findFirst();
    }

    /**
     * @return all entities.
     */
    @Override
    public Iterable<Food> findAll() {
        return new HashSet<>(jdbcOperations.query("select * from food", (rs, i) ->
                new Food(
                        rs.getLong("ID"),
                        rs.getString("Name"),
                        rs.getString("Producer"),
                        rs.getDate("ExpirationDate"))
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
    public Optional<Food> save(Food entity) throws ValidatorException {
        validator.validate(entity);
        Integer rowsAffected = jdbcOperations.update("INSERT INTO Food (ID, Name, Producer, ExpirationDate) VALUES (?, ?, ?, ?)",
                entity.getId(),
                entity.getName(),
                entity.getProducer(),
                entity.getExpirationDate());
        if(rowsAffected.equals(1))
            return Optional.empty();
        return Optional.of(entity);
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
        Optional<Food> toBeRemoved = findOne(aLong);
        toBeRemoved.ifPresent((food)->jdbcOperations.update("DELETE FROM Food WHERE ID = ?", food.getId()));
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
    public Optional<Food> update(Food entity) throws ValidatorException {
        validator.validate(entity);
        Integer rowsAffected = jdbcOperations.update(
                "UPDATE Food SET Name = ?, Producer = ?, ExpirationDate = ? WHERE ID = ?",
                entity.getName(),
                entity.getProducer(),
                entity.getExpirationDate(),
                entity.getId()
        );
        if(rowsAffected.equals(0))
            return Optional.empty();
        return Optional.of(entity);
    }
}
