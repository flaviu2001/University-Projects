package repository.databaseRepository;

import common.domain.Customer;
import common.exceptions.PetShopException;
import common.exceptions.ValidatorException;
import common.domain.validators.Validator;
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

    private final String url;
    private final String user;
    private final String password;
    private final Validator<Customer> validator;

    public CustomerDatabaseRepository(Validator<Customer> validator, String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.validator = validator;

        String sqlCreateTableCustomerQuery = """
                CREATE TABLE IF NOT EXISTS Customers (
                ID INT PRIMARY KEY,
                Name VARCHAR(50),
                PhoneNumber VARCHAR(11)
                )""";

        try (var connection = DriverManager.getConnection(this.url, this.user, this.password)){
            var preparedStatement = connection.prepareStatement(sqlCreateTableCustomerQuery);
            preparedStatement.executeUpdate();
        }catch (SQLException exception){
            throw new PetShopException("SQL Exception: " + exception);
        }
    }

    private Customer getCustomerFromResultSet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("ID");
        String name = resultSet.getString("Name");
        String phoneNumber = resultSet.getString("PhoneNumber");
        return new Customer(id, name, phoneNumber);
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
        AtomicReference<Customer> customerWithGivenId = new AtomicReference<>();

        Stream.ofNullable(aLong)
                .findAny()
                .ifPresentOrElse(
                        (id) -> {
                            String findCustomerWithGivenIdCommand =
                                    "SELECT * FROM Customers WHERE ID = " + id.toString();
                            try (var connection = DriverManager.getConnection(url, user, password);
                                 var preparedStatement = connection.prepareStatement(findCustomerWithGivenIdCommand);
                                 var resultSet = preparedStatement.executeQuery()
                            ){
                                Stream.of(resultSet.next())
                                        .filter((r) -> r)
                                        .forEach( (result) -> {
                                                    try {
                                                        Customer customer = getCustomerFromResultSet(resultSet);
                                                        customerWithGivenId.set(customer);
                                                    } catch (SQLException exception) {
                                                        throw new PetShopException("Sql exception: " + exception.getMessage());
                                                    }
                                                }
                                        );

                            }catch (SQLException exception){
                                throw new PetShopException("Sql exception: " + exception.getMessage());
                            }

                        }
                        ,
                        () -> {
                            throw new IllegalArgumentException("Id must not be null");
                        }
                );

        return Optional.of(customerWithGivenId.get());
    }

    /**
     * @return all entities.
     */
    @Override
    public Iterable<Customer> findAll() {
        Set<Customer> customers = new HashSet<>();
        String selectCustomersCommand = "SELECT * FROM Customers";

        try (var connection = DriverManager.getConnection(url, user, password);
            var preparedStatement = connection.prepareStatement(selectCustomersCommand);
            var resultSet = preparedStatement.executeQuery()
        ){
            while (resultSet.next()){
                Customer customer = getCustomerFromResultSet(resultSet);
                customers.add(customer);
            }

        }catch (SQLException exception){
            throw new PetShopException("SQL Exception: " + exception);
        }

        return customers;
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

        String insertCustomerCommand = "INSERT INTO Customers(ID, Name, PhoneNumber) VALUES (?, ?, ?)";

        try (var connection = DriverManager.getConnection(url, user, password);
             var preparedStatement = connection.prepareStatement(insertCustomerCommand)
        ){
            preparedStatement.setLong(1, entity.getId());
            preparedStatement.setString(2, entity.getName());
            preparedStatement.setString(3, entity.getPhoneNumber());

            preparedStatement.executeUpdate();
        }catch (SQLException exception){
            throw new PetShopException("Sql exception: " + exception.getMessage());
        }
        return Optional.empty();
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
        Optional<Customer> customerWithGivenId = findOne(aLong);
        AtomicReference<Customer> removedCustomer = new AtomicReference<>();


        customerWithGivenId.ifPresentOrElse( (customer) -> {
            String deleteCustomerCommand = "DELETE FROM Customers WHERE ID = ?";
                    try (var connection = DriverManager.getConnection(url, user, password);
                         var preparedStatement = connection.prepareStatement(deleteCustomerCommand)
                    ){
                        preparedStatement.setLong(1, aLong);
                        preparedStatement.executeUpdate();
                        removedCustomer.set(customer);
                    }catch (SQLException exception){
                        throw new PetShopException("Sql exception: " + exception.getMessage());
                    }
        }, () -> removedCustomer.set(null)
        );

        return Optional.of(removedCustomer.get());
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
        Optional<Customer> customerWithGivenId = findOne(entity.getId());
        AtomicReference<Customer> updatedCustomer = new AtomicReference<>();

        customerWithGivenId.ifPresentOrElse( (customer) -> {
                    String updateCustomerCommand = "UPDATE Customers SET Name = ?, PhoneNumber = ? WHERE ID = ?";
                    try (var connection = DriverManager.getConnection(url, user, password);
                         var preparedStatement = connection.prepareStatement(updateCustomerCommand)
                    ){
                        preparedStatement.setString(1, entity.getName());
                        preparedStatement.setString(2, entity.getPhoneNumber());
                        preparedStatement.setLong(3, entity.getId());
                        preparedStatement.executeUpdate();
                        updatedCustomer.set(customer);
                    }catch (SQLException exception){
                        throw new PetShopException("Sql exception: " + exception.getMessage());
                    }
                }, () -> updatedCustomer.set(null)
        );

        return Optional.of(updatedCustomer.get());
    }
}
