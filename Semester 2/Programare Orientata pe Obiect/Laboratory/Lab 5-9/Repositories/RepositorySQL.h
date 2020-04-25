//
// Created by jack on 4/23/20.
//

#ifndef IMRE5_REPOSITORYSQL_H
#define IMRE5_REPOSITORYSQL_H

#include "RepositoryFile.h"

class RepositorySQL : public RepositoryFile{
private:
    /**
     * Saves the state of a turret list to the file
     */
    void save_to_file(std::vector<Turret> &turret_list) override;
public:
    RepositorySQL();
    ~RepositorySQL() override;

    /**
     * Returns the list of turrets in a DynamicArray object from the repository.
     */
    std::vector<Turret> get_turret_list() override;

    /**
     * Sets the file name where the repository is stored.
     */
    void set_file_name(const std::string &file_name_to_set) override;

    /**
     * Adds a turret to the main_repository
     * @param turret: a Turret object
     */
    void add(const Turret &turret) override;

    /**
     * Removes the first apparition(should be only one) of the turret with given location from the main_repository
     * @param location: location of the turret to be removed
     */
    void remove(const std::string &location) override;

    /**
     * Updates the turret from main_repository with the given location.
     * @param location: string - new location of the turret
     * @param new_size: string - new size of the turret
     * @param new_aura_level: int - new level aura of the turret
     * @param new_separate_parts: int - new number of separate parts of the turret
     * @param new_vision: string - new vision of the turret
     */
    void update(const std::string &location, const std::string &new_size, const int &new_aura_level,
                                const int &new_separate_parts, const std::string &new_vision) override;

    /**
     * Returns true if the turret with the given location exists.
     * @param location: location after which to search for the turret
     */
    bool has_turret(const std::string &location) override;

    /**
     * Returns the turret with the given location from the repository if it exists.
     * @param location: location after which to search for the turret
     */
    Turret find_turret(const std::string &location) override;
};


#endif //IMRE5_REPOSITORYSQL_H
