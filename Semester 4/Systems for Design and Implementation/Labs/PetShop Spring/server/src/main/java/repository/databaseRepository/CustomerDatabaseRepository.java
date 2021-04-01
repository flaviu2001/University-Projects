package repository.databaseRepository;

import common.domain.Cat;
import common.domain.Customer;
import common.exceptions.PetShopException;
import common.exceptions.ValidatorException;
import common.domain.validators.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import repository.IRepository;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class CustomerDatabaseRepository implements IRepository<Long, Customer> {

    @Autowired
    private JdbcOperations jdbcOperations;

    private final Validator<Customer> validator;

    public CustomerDatabaseRepository(Validator<Customer> validator) {
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
    public Optional<Customer> findOne(Long aLong) {
        return jdbcOperations.query("select * from customers where id=" + aLong, (rs, i)->
                new Customer(
                        rs.getLong("ID"),
                        rs.getString("Name"),
                        rs.getString("PhoneNumber")))
                .stream().findFirst();
    }

    /**
     * @return all entities.
     */
    @Override
    public Iterable<Customer> findAll() {
        return new HashSet<>(jdbcOperations.query("select * from customers", (rs, i) ->
                new Customer(
                        rs.getLong("ID"),
                        rs.getString("Name"),
                        rs.getString("PhoneNumber"))
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
    public Optional<Customer> save(Customer entity) throws ValidatorException {
        validator.validate(entity);
        Integer rowsAffected = jdbcOperations.update("INSERT INTO customers (ID, Name, phonenumber) VALUES (?, ?, ?)",
                entity.getId(),
                entity.getName(),
                entity.getPhoneNumber());
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
    public Optional<Customer> delete(Long aLong) {
        Optional<Customer> toBeRemoved = findOne(aLong);
        toBeRemoved.ifPresent((customer)->jdbcOperations.update("DELETE FROM Customers WHERE ID = ?", customer.getId()));
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
    public Optional<Customer> update(Customer entity) throws ValidatorException {
        validator.validate(entity);
        Integer rowsAffected = jdbcOperations.update(
                "UPDATE customers SET Name = ?, phonenumber = ? WHERE ID = ?",
                entity.getName(),
                entity.getPhoneNumber(),
                entity.getId()
        );
        if(rowsAffected.equals(0))
            return Optional.empty();
        return Optional.of(entity);
    }
}
