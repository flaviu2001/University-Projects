package repository.databaseRepository;

import common.domain.Cat;
import common.domain.validators.Validator;
import common.exceptions.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import repository.IRepository;

import java.util.HashSet;
import java.util.Optional;

public class CatDatabaseRepository implements IRepository<Long, Cat> {
    @Autowired
    private JdbcOperations jdbcOperations;

    private final Validator<Cat> validator;


    public CatDatabaseRepository(Validator<Cat> validator) {
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
    public Optional<Cat> findOne(Long aLong) {
        return jdbcOperations.query("select * from cats where id=" + aLong, (rs, i)->
                new Cat(
                    rs.getLong("ID"),
                    rs.getString("Name"),
                    rs.getString("Breed"),
                    rs.getInt("CatYears")))
                .stream().findFirst();
    }

    /**
     * @return all entities.
     */
    @Override
    public Iterable<Cat> findAll() {
        return new HashSet<>(jdbcOperations.query("select * from cats", (rs, i) ->
                new Cat(
                    rs.getLong("ID"),
                    rs.getString("Name"),
                    rs.getString("Breed"),
                    rs.getInt("CatYears"))
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
    public Optional<Cat> save(Cat entity) throws ValidatorException {
        validator.validate(entity);
        Integer rowsAffected = jdbcOperations.update("INSERT INTO Cats (ID, Name, Breed, CatYears) VALUES (?, ?, ?, ?)",
                entity.getId(),
                entity.getName(),
                entity.getBreed(),
                entity.getCatYears());
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
    public Optional<Cat> delete(Long aLong) {
        Optional<Cat> toBeRemoved = findOne(aLong);
        toBeRemoved.ifPresent((cat)->jdbcOperations.update("DELETE FROM Cats WHERE ID = ?", cat.getId()));
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
    public Optional<Cat> update(Cat entity) throws ValidatorException {
        validator.validate(entity);
        Integer rowsAffected = jdbcOperations.update(
                "UPDATE Cats SET Name = ?, Breed = ?, CatYears = ? WHERE ID = ?",
                    entity.getName(),
                    entity.getBreed(),
                    entity.getCatYears(),
                    entity.getId()
                );
        if(rowsAffected.equals(0))
            return Optional.empty();
        return Optional.of(entity);
    }
}
