package repository.csvRepository;

import domain.BaseEntity;
import domain.exceptions.PetShopException;
import domain.validators.Validator;
import repository.PersistentRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CSVRepository<ID, E extends BaseEntity<ID>> extends PersistentRepository<ID, E, String> {
    private final Path filePath;

    public CSVRepository(Validator<E> validator, String filePath) {
        super(validator);
        this.filePath = Paths.get(filePath);
        loadData();
    }

    /**
     * Loads the data from the csv file
     */
    @Override
    protected void loadData() {
        try(BufferedReader reader = Files.newBufferedReader(filePath)){
            List<E> entities = reader.lines().map(this::extractEntity).collect(Collectors.toList());
            loadEntities(entities);
        } catch (IOException e) {
            throw new PetShopException(e.getMessage());
        }
    }

    /**
     * Saves the data to the csv file
     */
    @Override
    protected void saveData() {
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(filePath))) {
            findAll().forEach(entity -> writer.println(convertEntity(entity)));
        } catch (IOException e) {
            throw new PetShopException(e.getMessage());
        }
    }
}
