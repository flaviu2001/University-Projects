package repository;

import domain.BaseEntity;
import domain.validators.Validator;

import java.util.Optional;

public abstract class PersistentRepository<ID, E extends BaseEntity<ID>, DTO> extends InMemoryRepository<ID, E>{
    public PersistentRepository(Validator<E> validator) {
        super(validator);
    }

    /**
     * Loads the data from the data source in memory.
     */
    protected abstract void loadData();

    /**
     * Saves the data from memory to the data source
     */
    protected abstract void saveData();

    /**
     * Maps a data transfer object to a entity
     * @param dataTransferObject is the intermediary between data source and the program
     * @return the entity object corresponding to the data transfer object
     */
    protected abstract E extractEntity(DTO dataTransferObject);

    /**
     * Maps a entity to a data transfer object
     * @param entity which extends BaseEntity<ID>
     * @return the data transfer object corresponding to the entity
     */
    protected abstract DTO convertEntity(E entity);

    /**
     * Saves entity object in memory then saves the data if a change was made
     * @param entity entity to be saved
     * @return optional containing null if entity was saved, and entity if it wasn't saved
     */
    @Override
    public Optional<E> save(E entity){
        Optional<E> optionalE = super.save(entity);
        if(optionalE.isEmpty())
            saveData();
        return optionalE;
    }

    /**
     * Deletes entity object with given id in memory then saves the data if a change was made
     * @param id identifier of the entity to be saved
     * @return optional containing null if entity was not found, and entity if it was deleted
     */
    @Override
    public Optional<E> delete(ID id) {
        Optional<E> optionalE = super.delete(id);
        if(optionalE.isPresent())
            saveData();
        return optionalE;
    }

    /**
     * Updates entity object in memory then saves the data if a change was made
     * @param entity entity to be updated
     * @return optional containing null if entity was not found, and entity if it was updated
     */
    @Override
    public Optional<E> update(E entity){
        Optional<E> optionalE = super.update(entity);
        if(optionalE.isPresent())
            saveData();
        return optionalE;
    }
}
