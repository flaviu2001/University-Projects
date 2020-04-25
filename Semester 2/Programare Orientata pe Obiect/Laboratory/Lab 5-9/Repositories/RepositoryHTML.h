//
// Created by jack on 4/22/20.
//

#ifndef IMRE5_REPOSITORYHTML_H
#define IMRE5_REPOSITORYHTML_H

#include "../Utilities/Domain.h"
#include "RepositoryFile.h"
#include <string>
#include <vector>

class RepositoryHTML : public RepositoryFile{
private:
    /**
     * Saves the state of a turret list to the file
     */
    void save_to_file(std::vector<Turret> &turret_list) override;
public:
    RepositoryHTML();
    ~RepositoryHTML() override;

    /**
     * Returns the list of turrets in a DynamicArray object from the repository.
     */
    std::vector<Turret> get_turret_list() override;

    /**
     * Sets the name of the file that the repository is stored in and initialises it
     */
    void set_file_name(const std::string &file_name_to_set) override;
};

#endif //IMRE5_REPOSITORYHTML_H
