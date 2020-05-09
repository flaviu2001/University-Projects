//
// Created by jack on 4/11/20.
//

#ifndef IMRE5_REPOSITORYCSV_H
#define IMRE5_REPOSITORYCSV_H

#include "../Utilities/Domain.h"
#include "RepositoryFile.h"
#include <string>
#include <vector>

class RepositoryCSV : public RepositoryFile{
private:
    /**
     * Saves the state of a turret list to the file
     */
    void save_to_file(std::vector<Turret> &turret_list) override;
public:
    RepositoryCSV();
    ~RepositoryCSV() override;

    /**
     * Returns the list of turrets in a DynamicArray object from the repository.
     */
    std::vector<Turret> get_turret_list() override;

    /**
     * Sets the file name where the repository is stored.
     */
    void set_file_name(const std::string &file_name_to_set) override;
};

#endif //IMRE5_REPOSITORYCSV_H
