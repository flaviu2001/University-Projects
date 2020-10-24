//
// Created by jack on 4/22/20.
//

#ifndef IMRE5_REPOSITORYFILE_H
#define IMRE5_REPOSITORYFILE_H

#include "../Utilities/Domain.h"
#include "Repository.h"
#include <string>
#include <vector>

class RepositoryFile : public Repository{
protected:
    std::string file_name;
    int size;

    virtual void save_to_file(std::vector<Turret>&) = 0;

public:
    RepositoryFile();
    ~RepositoryFile() override;

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
    void update(const std::string &location,
                const std::string &new_size,
                const int &new_aura_level,
                const int &new_separate_parts,
                const std::string &new_vision) override;

    /**
     * Returns the name of the file that contains the repository.
     */
    std::string get_file_name() override;

    void set_file_name(const std::string&) override = 0;

    bool is_file() override;

    /**
     * Returns the number of elements in the repository.
     */
    int length() override;
};



#endif //IMRE5_REPOSITORYFILE_H
